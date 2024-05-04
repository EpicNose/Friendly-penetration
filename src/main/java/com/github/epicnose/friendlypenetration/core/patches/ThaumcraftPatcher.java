package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.core.patches.base.ModPatcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;

public class ThaumcraftPatcher extends ModPatcher {

    public ThaumcraftPatcher() {
        super("Thaumcraft", "Thaumcraft");
        this.classes.put("thaumcraft.common.entities.InventoryMob", (classNode) -> patchGolemInventory(classNode));
        this.classes.put("lotr.common.LOTRBannerProtection$2", (classNode) -> patchGolemBannerProtection(classNode));
    }

    private void patchGolemInventory(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "getSizeInventory", "func_70302_i_", "()I");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.IRETURN) {
                // Remove the +1 nodes
                ASMUtils.removePreviousNodes(method.instructions, node, 2);
                
                break;
            }
        }
        

        UCPCoreMod.log.info("Patched the Golem inventory to be compatible with the coin conter (This might lead to issues with other mods).");
    }
    
    private void patchGolemBannerProtection(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "protects", "(Llotr/common/entity/item/LOTREntityBanner;)Llotr/common/LOTRBannerProtection$ProtectType;");
        if(method == null) return;

        //Add our own hook for thaumcraft fake players
        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.ALOAD) {
                InsnList insList = new InsnList();
                
                LabelNode endLabel = new LabelNode();
                LabelNode popLabel = new LabelNode();
                
                insList.add(new VarInsnNode(Opcodes.ALOAD, 0));//this
                insList.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/LOTRBannerProtection$2", "val$entityplayer", "Lnet/minecraft/entity/player/EntityPlayer;")); //get player
                insList.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraftforge/common/util/FakePlayer")); //instanceof fake player if true put 1 on stack else put 0 on stack
                insList.add(new JumpInsnNode(Opcodes.IFEQ, endLabel)); //if 0 jump to label else continue
                
                insList.add(new VarInsnNode(Opcodes.ALOAD, 0));//this
                insList.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/LOTRBannerProtection$2", "val$entityplayer", "Lnet/minecraft/entity/player/EntityPlayer;")); //get player
                insList.add(new VarInsnNode(Opcodes.ALOAD, 1));//banner
                insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/frendlypenetration/core/hooks/ThaumcraftHooks", "thaumcraftGolemBannerProtection", "(Lnet/minecraft/entity/player/EntityPlayer;Llotr/common/entity/item/LOTREntityBanner;)Llotr/common/LOTRBannerProtection$ProtectType;", false)); //call our hook
                
                
                insList.add(new InsnNode(Opcodes.DUP)); //Duplicate hook result
                insList.add(new JumpInsnNode(Opcodes.IFNULL, popLabel)); //if hook returns null that means that this fake player isn't supported by the hook and we continue with the default lotr code.
                
                insList.add(new InsnNode(Opcodes.ARETURN)); //Return the result from the hook

                insList.add(popLabel); //Label to pop last value from stack and continue with lotr code
                insList.add(new InsnNode(Opcodes.POP)); //Duplicate hook result
                insList.add(endLabel); //Label to continue with normal lotr code
                
                method.instructions.insertBefore(node, insList);
                
                break;
            }
        }

        UCPCoreMod.log.info("Patched the banner protection to user the golem owner instead of the golem itself and prevented sending warning messages to fake players.");
    }
}

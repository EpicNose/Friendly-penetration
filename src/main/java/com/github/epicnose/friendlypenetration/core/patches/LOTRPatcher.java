package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import com.github.epicnose.friendlypenetration.core.patches.base.ModPatcher;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;
import net.minecraftforge.classloading.FMLForgePlugin;

public class LOTRPatcher extends ModPatcher {

    public LOTRPatcher() {
        super("LOTR", "lotr");
        this.classes.put("lotr.common.item.LOTRValuableItems", (classNode) -> patchValuableToolMaterials(classNode));
        this.classes.put("lotr.common.enchant.LOTREnchantmentHelper", (classNode) -> patchLOTRNegateDamage(classNode));
        this.classes.put("lotr.client.render.tileentity.LOTRRenderArmorStand", (classNode) -> patchArmorStandRender(classNode));
        this.classes.put("lotr.common.block.LOTRBlockReplacement", (classNode) -> patchBlockReplacements(classNode));
        this.classes.put("lotr.common.LOTRBannerProtection$2", (classNode) -> patchFakePlayerWarningMessage(classNode));
    }

    private void patchValuableToolMaterials(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "registerToolMaterials", "()V");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.IFNULL) {
                JumpInsnNode jumpNode = (JumpInsnNode) node;
                
                InsnList insList = new InsnList();
                insList.add(new VarInsnNode(Opcodes.ALOAD, 5));
                insList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", FMLForgePlugin.RUNTIME_DEOBF ? "func_77973_b" : "getItem", "()Lnet/minecraft/item/Item;", false));
                insList.add(new JumpInsnNode(Opcodes.IFNULL, jumpNode.label));
                
                method.instructions.insert(jumpNode, insList);
            }
        }

        UCPCoreMod.log.info("Patched LOTRValuableItems to ignore tool materials with repair itemstack which have a null item.");
    }

    private void patchLOTRNegateDamage(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "negateDamage", "(Lnet/minecraft/item/ItemStack;Ljava/util/Random;)Z");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.IFNULL) {
                JumpInsnNode jumpNode = (JumpInsnNode) node;
                
                InsnList insList = new InsnList();
                insList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insList.add(new JumpInsnNode(Opcodes.IFNULL, jumpNode.label));
                
                method.instructions.insert(jumpNode, insList);
            }
        }
          
        UCPCoreMod.log.info("Patched LOTREnchantmentHelper to ignore damage negation attempts with a null random.");
    }

    private void patchArmorStandRender(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "renderTileEntityAt", "func_147500_a", "(Lnet/minecraft/tileentity/TileEntity;DDDF)V");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.INVOKESTATIC) {
                MethodInsnNode methodNode = (MethodInsnNode) node;
                
                if(methodNode.name.equals("getArmorModel") && methodNode.owner.equals("net/minecraftforge/client/ForgeHooksClient") && methodNode.desc.equals("(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;ILnet/minecraft/client/model/ModelBiped;)Lnet/minecraft/client/model/ModelBiped;")) {
                    AbstractInsnNode nullNode = methodNode.getPrevious().getPrevious().getPrevious().getPrevious();
                    
                    if(nullNode.getOpcode() == Opcodes.ACONST_NULL) {
                        FieldInsnNode fakeEntityNode = new FieldInsnNode(Opcodes.GETSTATIC, "io/gitlab/dwarfyassassin/lotrucp/client/util/FakeArmorStandEntity", "INSTANCE", "Lio/gitlab/dwarfyassassin/lotrucp/client/util/FakeArmorStandEntity;");
                        method.instructions.set(nullNode, fakeEntityNode);
                    }
                }
            }
        }
          
        UCPCoreMod.log.info("Patched LOTRRenderArmorStand to use a fake entity instead of null.");
    }

    private void patchBlockReplacements(ClassNode classNode) {
        MethodNode blockMethod = ASMUtils.findMethod(classNode, "replaceVanillaBlock", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/Block;Ljava/lang/Class;)V");
        if(blockMethod == null) return;

        boolean first = true;
        for(AbstractInsnNode node : blockMethod.instructions.toArray()) {
            //Patch block and itemBlock delegate
            if(node.getOpcode() == Opcodes.INVOKEINTERFACE) {
                MethodInsnNode methodNode = (MethodInsnNode) node;
                
                if(methodNode.name.equals("put") && methodNode.owner.equals("java/util/Map") && methodNode.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")) {
                    AbstractInsnNode popNode = methodNode.getNext();
                    
                    if(popNode.getOpcode() == Opcodes.POP) {
                        InsnList insList = new InsnList();
                        
                        VarInsnNode newVarNode = first ? new VarInsnNode(Opcodes.ALOAD, 1) : new VarInsnNode(Opcodes.ALOAD, 10);
                        insList.add(newVarNode); //load new block or itemBlock
                        
                        insList.add(new VarInsnNode(Opcodes.ALOAD, 6)); //load registry name
                        
                        String methodName = first ? "setBlockDelagateName" : "setItemDelagateName";
                        String methodDesc = first ? "(Lnet/minecraft/block/Block;Ljava/lang/String;)V" : "(Lnet/minecraft/item/Item;Ljava/lang/String;)V";
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/GenericModHooks", methodName, methodDesc, false));
                        
                        blockMethod.instructions.insert(popNode, insList);
                        
                        first = false;
                    }
                }
            }
            
            //Patch ore registry removal
            if(node.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodNode = (MethodInsnNode) node;
                
                if(methodNode.name.equals(FMLForgePlugin.RUNTIME_DEOBF ? "func_149663_c" : "setBlockName") && methodNode.owner.equals("net/minecraft/block/Block") && methodNode.desc.equals("(Ljava/lang/String;)Lnet/minecraft/block/Block;")) {
                    AbstractInsnNode aLoadNode = methodNode.getPrevious().getPrevious();
                    
                    if(aLoadNode.getOpcode() == Opcodes.ALOAD) {
                        InsnList insList = new InsnList();
                        insList.add(new VarInsnNode(Opcodes.ALOAD, 0)); //load old block
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/GenericModHooks", "removeBlockFromOreDictionary", "(Lnet/minecraft/block/Block;)V", false));
                        
                        blockMethod.instructions.insertBefore(aLoadNode, insList);
                    }
                }
            }
        }
        
        
        MethodNode itemMethod = ASMUtils.findMethod(classNode, "replaceVanillaItem", "(Lnet/minecraft/item/Item;Lnet/minecraft/item/Item;)V");
        if(itemMethod == null) return;

        for(AbstractInsnNode node : itemMethod.instructions.toArray()) {
            //Patch item delegate
            if(node.getOpcode() == Opcodes.INVOKEINTERFACE) {
                MethodInsnNode methodNode = (MethodInsnNode) node;
                
                if(methodNode.name.equals("put") && methodNode.owner.equals("java/util/Map") && methodNode.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")) {
                    AbstractInsnNode popNode = methodNode.getNext();
                    
                    if(popNode.getOpcode() == Opcodes.POP) {
                        InsnList insList = new InsnList();
                        insList.add(new VarInsnNode(Opcodes.ALOAD, 1)); //load new item
                        insList.add(new VarInsnNode(Opcodes.ALOAD, 4)); //load registry name
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/GenericModHooks", "setItemDelagateName", "(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false));
                        
                        itemMethod.instructions.insert(popNode, insList);
                        break;
                    }
                }
            }

            //Patch ore registry removal
            if(node.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodNode = (MethodInsnNode) node;
                
                if(methodNode.name.equals(FMLForgePlugin.RUNTIME_DEOBF ? "func_77655_b" : "setUnlocalizedName") && methodNode.owner.equals("net/minecraft/item/Item") && methodNode.desc.equals("(Ljava/lang/String;)Lnet/minecraft/item/Item;")) {
                    AbstractInsnNode aLoadNode = methodNode.getPrevious().getPrevious();
                    
                    if(aLoadNode.getOpcode() == Opcodes.ALOAD) {
                        InsnList insList = new InsnList();
                        insList.add(new VarInsnNode(Opcodes.ALOAD, 0)); //load old item
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/GenericModHooks", "removeItemFromOreDictionary", "(Lnet/minecraft/item/Item;)V", false));
                        
                        blockMethod.instructions.insertBefore(aLoadNode, insList);
                    }
                }
            }
        }
        
        
        UCPCoreMod.log.info("Patched LOTRBlockReplacement method to also set the registry delagate names and to remove the old blocks/items from the ore registry.");
    }
    
    private void patchFakePlayerWarningMessage(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "warnProtection", "(Lnet/minecraft/util/IChatComponent;)V");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.INSTANCEOF) {
                TypeInsnNode typeNode = (TypeInsnNode) node;
                
                if(typeNode.desc.equals("net/minecraft/entity/player/EntityPlayerMP")) {
                    AbstractInsnNode nextNode = typeNode.getNext();
                    
                    if(nextNode.getOpcode() == Opcodes.IFEQ) {
                        JumpInsnNode jumpNode = (JumpInsnNode) nextNode;
                        LabelNode endLabel = jumpNode.label;
                        
                        InsnList insList = new InsnList();
                        insList.add(new VarInsnNode(Opcodes.ALOAD, 0));//this
                        insList.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/LOTRBannerProtection$2", "val$entityplayer", "Lnet/minecraft/entity/player/EntityPlayer;")); //get player
                        insList.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraftforge/common/util/FakePlayer")); //instanceof fake player if true put 1 on stack else put 0 on stack
                        insList.add(new JumpInsnNode(Opcodes.IFNE, endLabel)); //if 1 jump to label else continue
                        
                        method.instructions.insert(jumpNode, insList);
                        
                        break;
                    }
                }
            }
        }
        
        UCPCoreMod.log.info("Patched the banner protection to prevent sending warning messages to fake players.");
    }
}

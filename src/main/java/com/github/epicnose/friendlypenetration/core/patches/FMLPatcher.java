package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.core.patches.base.Patcher;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FMLPatcher extends Patcher {

    public FMLPatcher() {
        super("FML");

        this.classes.put("cpw.mods.fml.common.FMLModContainer", (classNode) -> patchModContainer(classNode));
        this.classes.put("cpw.mods.fml.common.Loader", (classNode) -> patchLoader(classNode));
    }

    private void patchModContainer(ClassNode classNode) {
        // Not a vanilla classes so no obfuscation.
        MethodNode method = ASMUtils.findMethod(classNode, "bindMetadata", "(Lcpw/mods/fml/common/MetadataCollection;)V");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node instanceof FieldInsnNode) {
                FieldInsnNode fieldNode = (FieldInsnNode) node;

                if(fieldNode.name.equals("dependants")) {
                    InsnList insList = new InsnList();
                    insList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insList.add(new FieldInsnNode(Opcodes.GETFIELD, "cpw/mods/fml/common/FMLModContainer", "modMetadata", "Lcpw/mods/fml/common/ModMetadata;"));
                    insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/friendlypenetration/core/hooks/PreMCHooks", "forgeLoadOrderHook", "(Lcpw/mods/fml/common/ModMetadata;)V", false));
                    method.instructions.insert(fieldNode, insList);
                    break;
                }
            }
        }

        UCPCoreMod.log.info("[FR]Patched the FML dependency loader.");
    }

    private void patchLoader(ClassNode classNode) {
        // Not a vanilla classes so no obfuscation.
        MethodNode method = ASMUtils.findMethod(classNode, "loadMods", "()V");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node instanceof MethodInsnNode && node.getOpcode() == Opcodes.INVOKESTATIC) {
                MethodInsnNode methodNode = (MethodInsnNode) node;

                if(methodNode.name.equals("copyOf") && methodNode.owner.equals("com/google/common/collect/ImmutableList")) {
                    MethodInsnNode insertNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/friendlypenetration/core/hooks/PreMCHooks", "postFMLLoad", "()V", false);
                    method.instructions.insert(methodNode.getNext(), insertNode);
                    break;
                }
            }
        }

        UCPCoreMod.log.info("[FR]Patched the FML loader.");
    }
}

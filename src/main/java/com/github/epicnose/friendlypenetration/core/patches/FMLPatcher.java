package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.core.patches.base.Patcher;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class FMLPatcher extends Patcher {

    public FMLPatcher() {
        super("FML");

        this.classes.put("cpw.mods.fml.common.FMLModContainer", (classNode) -> patchModContainer(classNode));
        this.classes.put("cpw.mods.fml.common.Loader", (classNode) -> patchLoader(classNode));

        this.classes.put("lotr.common.entity.npc.LOTREntityNPC",(classNode -> patchNPCArrowAttack(classNode)));
    }


    private void patchNPCArrowAttack(ClassNode classNode){
//        UCPCoreMod.log.info("[FP]:"+"arrowin");
        MethodNode method = ASMUtils.findMethod(classNode, "npcArrowAttack", "(Lnet/minecraft/entity/EntityLivingBase;F)V");
        int count=0;
//        UCPCoreMod.log.info("counttag0:"+count);
        if(method == null) return;
        AbstractInsnNode ain = ASMUtils.getLastOpcode(method.instructions, Opcodes.RETURN);

        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); // Load 'this' onto the stack
        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getHeldItem", "()Lnet/minecraft/item/ItemStack;", false)); // Call 'getHeldItem' method
        instructions.add(new VarInsnNode(Opcodes.ASTORE, 3)); // Store the result in local variable 3


        method.instructions.insertBefore(ain,instructions);









//        insList.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/util/LOTRLog", "logger", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
//        insList.add(new LdcInsnNode("射了宝贝")); // 加载字符串 "1222"
//        insList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法
//        method.instructions.clear();
//        method.instructions.add(new InsnNode(Opcodes.RETURN));
        //移除第四行之后的Ins

//       LineNumberNode
//        InsnList reserveList = new InsnList();
//        for(int i=0;i<method.instructions.size();i++){
//            AbstractInsnNode node = method.instructions.get(i);
//            UCPCoreMod.log.info("[arrow]:"+node.getOpcode());
//
//            if(node.getOpcode() == Opcodes.FSTORE){
//                VarInsnNode vnode = (VarInsnNode) node;
//                method.instructions.clear();
//                method.instructions=reserveList;
////                method.instructions.add(vnode, reserveList);
////                UCPCoreMod.log.info("匹配到fstore:"+node.getOpcode());
//                break;
//            }
//            reserveList.add(node);
//        }

//        method.instructions=reserveList; //直接覆盖

//        for(AbstractInsnNode node : method.instructions.toArray()) {
////            method.instructions.indexOf()
//        }
//        UCPCoreMod.log.info("方法总行数:"+count);
//        UCPCoreMod.log.info("[FP]patch npcarrowattack");
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

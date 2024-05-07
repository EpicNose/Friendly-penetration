package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
//import jdk.internal.org.objectweb.asm.tree.InsnNode;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;

import lotr.common.util.LOTRLog;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import com.github.epicnose.friendlypenetration.core.patches.base.ModPatcher;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;
import net.minecraftforge.classloading.FMLForgePlugin;
import scala.tools.cmd.gen.AnyValReps;

public class LOTRPatcher extends ModPatcher {
    public static FieldInsnNode renderins;
    public LOTRPatcher() {
        super("LOTR", "lotr");

//        this.classes.put("lotr.common.entity.npc.LOTREntityNPC",(classNode -> patchNPCArrowAttack(classNode)));


//        this.classes.put("cpw/mods/fml/client/registry/RenderingRegistry",(classNode -> getRenderIns(classNode)));
//        this.classes.put("lotr.common.item.LOTRValuableItems", (classNode) -> patchValuableToolMaterials(classNode));
//        this.classes.put("lotr.common.enchant.LOTREnchantmentHelper", (classNode) -> patchLOTRNegateDamage(classNode));
//        this.classes.put("lotr.client.render.tileentity.LOTRRenderArmorStand", (classNode) -> patchArmorStandRender(classNode));
//        this.classes.put("lotr.common.block.LOTRBlockReplacement", (classNode) -> patchBlockReplacements(classNode));
//        this.classes.put("lotr.common.LOTRBannerProtection$2", (classNode) -> patchFakePlayerWarningMessage(classNode));

        this.classes.put("lotr.client.LOTRClientProxy",(classNode -> patchClientRegistry(classNode)));
        this.classes.put("lotr.common.entity.LOTREntities",(classNode -> patchEntityRegistry(classNode)));


        this.classes.put("lotr.common.entity.projectile.LOTREntityProjectileBase",(classNode -> patchFriendlyPenetration(classNode)));



    }




    private void getRenderIns(ClassNode classNode){
        this.renderins = new FieldInsnNode(Opcodes.GETSTATIC, "cpw/mods/fml/client/registry/RenderingRegistry", "INSTANCE", "Lcpw/mods/fml/client/registry/RenderingRegistry;");
    }



    private void patchEntityRegistry(ClassNode classNode){
//        LOTREntities.registerEntity(LOTREntityArrow.class, "LOTRArrow", 2043, 64, 20, false);
        MethodNode method = ASMUtils.findMethod(classNode, "registerEntities", "()V");
        if(method == null) return;
        AbstractInsnNode ain = ASMUtils.getLastOpcode(method.instructions, Opcodes.RETURN);

        InsnList insList = new InsnList();

        insList.add(new TypeInsnNode(Opcodes.NEW, "lotr/common/entity/LOTREntities"));
        insList.add(new InsnNode(Opcodes.DUP));
        insList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "lotr/common/entity/LOTREntities", "<init>", "()V", false));
        insList.add(new LdcInsnNode(Type.getType("Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;")));
        insList.add(new LdcInsnNode("LOTRArrow"));
        insList.add(new IntInsnNode(Opcodes.BIPUSH, 2077)); // id
        insList.add(new IntInsnNode(Opcodes.BIPUSH, 64)); // updateRange
        insList.add(new IntInsnNode(Opcodes.BIPUSH, 20)); // updateFreq
        insList.add(new InsnNode(Opcodes.ICONST_0)); // false (sendVelocityUpdates)
        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lotr/common/entity/LOTREntities", "registerEntity", "(Ljava/lang/Class;Ljava/lang/String;IIIZ)V", false));
//        insList.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/entity/LOTREntities", "INSTANCE", "Llotr/common/entity/LOTREntities;"));
//        insList.add(new LdcInsnNode(Type.getType("Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;")));
//        insList.add(new LdcInsnNode("LOTRArrow"));
//        insList.add(new IntInsnNode(Opcodes.BIPUSH, 2077));
//        insList.add(new IntInsnNode(Opcodes.BIPUSH, 64));
//        insList.add(new IntInsnNode(Opcodes.BIPUSH, 20));
//        insList.add(new InsnNode(Opcodes.ICONST_0)); // false
//        insList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/LOTREntities", "registerEntity", "(Ljava/lang/Class;Ljava/lang/String;IIIZ)V", false));

        method.instructions.insertBefore(ain,insList);
        UCPCoreMod.log.info("[FP]patch LOTREntities lotrentityarrow registry");
    }

    private void patchClientRegistry(ClassNode classNode){
        MethodNode method = ASMUtils.findMethod(classNode, "onLoad", "()V");
        if(method == null) return;
//        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
        AbstractInsnNode ain = ASMUtils.getLastOpcode(method.instructions, Opcodes.RETURN);

        InsnList insList = new InsnList();


//        insList.add(new TypeInsnNode(Opcodes.NEW, "cpw/mods/fml/client/registry/RenderingRegistry"));
//        insList.add(new InsnNode(Opcodes.DUP));
//        insList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "cpw/mods/fml/client/registry/RenderingRegistry", "<init>", "()V", false));
//        insList.add(new LdcInsnNode(Type.getType("Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;")));
//        insList.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/client/render/entity/LOTRRenderArrow"));
//        insList.add(new InsnNode(Opcodes.DUP));
//        insList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/client/render/entity/LOTRRenderArrow", "<init>", "()V", false));
//        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cpw/mods/fml/client/registry/RenderingRegistry", "registerEntityRenderingHandler", "(Ljava/lang/Class;Lnet/minecraft/client/renderer/entity/Render;)V", false));
//

        insList.add(new LdcInsnNode(Type.getType("Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;")));
        insList.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/client/render/entity/LOTRRenderArrow"));
        insList.add(new InsnNode(Opcodes.DUP));
        insList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/client/render/entity/LOTRRenderArrow", "<init>", "()V", false));
        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cpw/mods/fml/client/registry/RenderingRegistry", "registerEntityRenderingHandler", "(Ljava/lang/Class;Lnet/minecraft/client/renderer/entity/Render;)V", false));



        method.instructions.insertBefore(ain,insList);
        UCPCoreMod.log.info("[FP]patch LOTRclientproxy lotrentityarrow registry");
    }



    private void patchFriendlyPenetration(ClassNode classNode){

//        MethodNode method = ASMUtils.findMethod(classNode,"onUpdate","()V");
//        classNode.
        MethodNode method = ASMUtils.findMethod(classNode, "onUpdate", "func_70071_h_", "()V");;
        if(method == null) return;
        UCPCoreMod.log.info("[FR]wuhuqifei1");


//        AbstractInsnNode ain = ASMUtils.getLastOpcode(method.instructions, Opcodes.RETURN);
//        InsnList instructions = new InsnList();
//        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/util/LOTRLog", "logger", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
//        instructions.add(new LdcInsnNode("targetbroke")); // 加载字符串 "1222"
//        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法
//        method.instructions.insertBefore(ain,instructions);

        for(AbstractInsnNode node : method.instructions.toArray()) {
            UCPCoreMod.log.info(node.getOpcode());
//            UCPCoreMod.log.info("[FR]wuhuqifei2");
            if(node.getOpcode() == Opcodes.GETFIELD){
//                ASMUtils.getLast
                FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
                UCPCoreMod.log.info("name"+fieldInsnNode.name);
                UCPCoreMod.log.info("owner"+fieldInsnNode.owner);
//                if(fieldInsnNode.name.equals("entityHit")){    //混淆的
//                    UCPCoreMod.log.info("[FR]wuhuqifei3");
//                }

                if(node.getPrevious().getOpcode() == Opcodes.ALOAD && node.getNext().getOpcode() == Opcodes.ASTORE){
                    UCPCoreMod.log.info("[FR]wuhuqifei4");
                    if(node.getPrevious().getPrevious().getPrevious().getPrevious().getOpcode() == Opcodes.IFNULL){   //targetpos
                        UCPCoreMod.log.info("[FR]wuhuqifei5");

                        JumpInsnNode jumpNode = (JumpInsnNode) node.getPrevious().getPrevious().getPrevious().getPrevious();  //在这个jumpNode前增加内容

                        InsnList instructions = new InsnList();
                        LabelNode label58 = new LabelNode();
                        LabelNode label59 = new LabelNode();
                        LabelNode label60 = new LabelNode();
                        LabelNode label61 = new LabelNode();
                        LabelNode label62 = new LabelNode();
                        LabelNode label63 = new LabelNode();
                        LabelNode label64 = new LabelNode();
                        LabelNode label65 = new LabelNode();
//
//                        LabelNode label66 = new LabelNode();


                        LabelNode label52 = new LabelNode();
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        instructions.add(new JumpInsnNode(Opcodes.IFNULL, label58));

                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
//                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/util/MovingObjectPosition", "entityHit", "Lnet/minecraft/entity/Entity;"));
                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/util/MovingObjectPosition", "field_72308_g", "Lnet/minecraft/entity/Entity;"));

                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 444));
////
//                        // label60
                        instructions.add(label60);
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 444));
                        instructions.add(new JumpInsnNode(Opcodes.IFNULL, label58));

                        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/util/LOTRLog", "logger", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
                        instructions.add(new LdcInsnNode("entityHit不为空")); // 加载字符串 "1222"
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法


////
//                        // label61
                        instructions.add(label61);
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 444));
                        instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "lotr/common/entity/npc/LOTREntityNPC"));
                        instructions.add(new JumpInsnNode(Opcodes.IFEQ, label58));

                        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/util/LOTRLog", "logger", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
                        instructions.add(new LdcInsnNode("444 是lotrentity")); // 加载字符串 "1222"
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法


////
//                        // label62
                        instructions.add(label62);
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/projectile/LOTREntityProjectileBase", "shootingEntity", "Lnet/minecraft/entity/Entity;"));
                        instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "lotr/common/entity/npc/LOTREntityNPC"));
                        instructions.add(new JumpInsnNode(Opcodes.IFEQ, label58));

                        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/util/LOTRLog", "logger", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
                        instructions.add(new LdcInsnNode("shootingEntity 是lotrentity")); // 加载字符串 "1222"
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法

//                        // label63
                        instructions.add(label63);
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 444));
                        instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "lotr/common/entity/npc/LOTREntityNPC"));
                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 666));
//
////                         label64
                        instructions.add(label64);
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/projectile/LOTREntityProjectileBase", "shootingEntity", "Lnet/minecraft/entity/Entity;"));
                        instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "lotr/common/entity/npc/LOTREntityNPC"));
                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 669));

                        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/util/LOTRLog", "logger", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
                        instructions.add(new LdcInsnNode("hit shoot都为生物")); // 加载字符串 "1222"
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法
////                         label65
                        instructions.add(label65);
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 666));
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getFaction", "()Llotr/common/fac/LOTRFaction;", false));
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 669));
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getFaction", "()Llotr/common/fac/LOTRFaction;", false));
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/fac/LOTRFaction", "isBadRelation", "(Llotr/common/fac/LOTRFaction;)Z", false));
                        instructions.add(new JumpInsnNode(Opcodes.IFNE, label58));
//                        LabelNode label66 = new LabelNode(new Label());
//                        instructions.add(label66);
//                        instructions.add(new LineNumberNode(339, label66));
                        instructions.add(new InsnNode(Opcodes.ACONST_NULL));
                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 4));

                        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "com/github/epicnose/friendlypenetration/core/UCPCoreMod", "log", "Lorg/apache/logging/log4j/Logger;")); // 获取 LOTRLog 类的 logger 字段
                        instructions.add(new LdcInsnNode("[ucp]将hitentity置为Null")); // 加载字符串 "1222"
                        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true)); // 调用 Logger 的 info 方法


                        instructions.add(label58);
//                        instructions.add(new LineNumberNode(343, label61));
//                        method.instructions.insertBefore(jumpNode.getNext().getNext().getNext().getNext(),instructions);



//                        method.instructions.insertBefore(jumpNode.getPrevious(),instructions);//2个 +4
                        method.instructions.insertBefore(node.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious(),instructions);//4个
//                        method.instructions.insertBefore(jumpNode.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious(),instructions);//4个
//                        jumpNode.getPrevious()
                        UCPCoreMod.log.info("[FR]wuhuqifei6");
                    }

                }

            }
        }
        UCPCoreMod.log.info("[FR]友军穿透patch");
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
                
                method.instructions.insertBefore(jumpNode, insList);
//                method.instructions.
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
//                FieldInsnNode fnode;
//                fnode.name
                if(methodNode.name.equals("getArmorModel") && methodNode.owner.equals("net/minecraft/client/renderer/ForgeHooksClient") && methodNode.desc.equals("(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;ILnet/minecraft/client/model/ModelBiped;)Lnet/minecraft/client/model/ModelBiped;")) {
                    AbstractInsnNode nullNode = methodNode.getPrevious().getPrevious().getPrevious().getPrevious();
                    
                    if(nullNode.getOpcode() == Opcodes.ACONST_NULL) {
                        FieldInsnNode fakeEntityNode = new FieldInsnNode(Opcodes.GETSTATIC, "com/github/epicnose/friendlypenetration/client/util/FakeArmorStandEntity", "INSTANCE", "Lio/gitlab/dwarfyassassin/lotrucp/client/util/FakeArmorStandEntity;");
                        method.instructions.set(nullNode, fakeEntityNode);
                    }
                }
            }
        }
          
        UCPCoreMod.log.info("[FR]Patched LOTRRenderArmorStand to use a fake entity instead of null.");
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
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/friendlypenetration/core/hooks/GenericModHooks", methodName, methodDesc, false));
                        
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
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/friendlypenetration/core/hooks/GenericModHooks", "removeBlockFromOreDictionary", "(Lnet/minecraft/block/Block;)V", false));
                        
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
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/friendlypenetration/core/hooks/GenericModHooks", "setItemDelagateName", "(Lnet/minecraft/item/Item;Ljava/lang/String;)V", false));
                        
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
                        insList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/epicnose/friendlypenetration/core/hooks/GenericModHooks", "removeItemFromOreDictionary", "(Lnet/minecraft/item/Item;)V", false));
                        
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
        
        UCPCoreMod.log.info("[FRFRFR]Patched the banner protection to prevent sending warning messages to fake players.");
    }
}

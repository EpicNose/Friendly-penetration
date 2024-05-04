package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
//import jdk.internal.org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import com.github.epicnose.friendlypenetration.core.patches.base.ModPatcher;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;
import net.minecraftforge.classloading.FMLForgePlugin;
import scala.tools.cmd.gen.AnyValReps;

public class LOTRPatcher extends ModPatcher {

    public LOTRPatcher() {
        super("LOTR", "lotr");
//        this.classes.put("lotr.common.item.LOTRValuableItems", (classNode) -> patchValuableToolMaterials(classNode));
//        this.classes.put("lotr.common.enchant.LOTREnchantmentHelper", (classNode) -> patchLOTRNegateDamage(classNode));
//        this.classes.put("lotr.client.render.tileentity.LOTRRenderArmorStand", (classNode) -> patchArmorStandRender(classNode));
//        this.classes.put("lotr.common.block.LOTRBlockReplacement", (classNode) -> patchBlockReplacements(classNode));
        this.classes.put("lotr.common.LOTRBannerProtection$2", (classNode) -> patchFakePlayerWarningMessage(classNode));
        this.classes.put("lotr.common.entity.projectile.LOTREntityProjectileBase",(classNode -> patchFriendlyPenetration(classNode)));
    }


    private void patchFriendlyPenetration(ClassNode classNode){

//        MethodNode method = ASMUtils.findMethod(classNode,"onUpdate","()V");
//        classNode.
        MethodNode method = ASMUtils.findMethod(classNode, "onUpdate", "func_70071_h_", "()V");;
        if(method == null) return;
        UCPCoreMod.log.info("[FR]wuhuqifei1");
        VarInsnNode vnode = new VarInsnNode(Opcodes.ALOAD, 5);
        UCPCoreMod.log.info("5号节点"+vnode.toString());
        for(AbstractInsnNode node : method.instructions.toArray()) {
            UCPCoreMod.log.info(node.getOpcode());
//            UCPCoreMod.log.info("[FR]wuhuqifei2");
            if(node.getOpcode() == Opcodes.GETFIELD){
                FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
                UCPCoreMod.log.info("name"+fieldInsnNode.name);
                UCPCoreMod.log.info("owner"+fieldInsnNode.owner);
//                if(fieldInsnNode.name.equals("entityHit")){
//                    UCPCoreMod.log.info("[FR]wuhuqifei3");
//                }

                if(node.getPrevious().getOpcode() == Opcodes.ALOAD && node.getNext().getOpcode() == Opcodes.ASTORE){
                    UCPCoreMod.log.info("[FR]wuhuqifei4");
                    if(node.getPrevious().getPrevious().getPrevious().getPrevious().getOpcode() == Opcodes.IFNULL){   //targetpos
                        UCPCoreMod.log.info("[FR]wuhuqifei5");
//                        AbstractInsnNode
                        JumpInsnNode jumpNode = (JumpInsnNode) node.getPrevious().getPrevious().getPrevious().getPrevious();  //在这个jumpNode前增加内容

                        InsnList instructions = new InsnList();

                        LabelNode label58 = new LabelNode();
                        LabelNode label59 = new LabelNode();
//                        LabelNode label60 = new LabelNode();
//                        LabelNode label61 = new LabelNode();
//                        LabelNode label62 = new LabelNode();
//                        LabelNode label63 = new LabelNode();
//                        LabelNode label64 = new LabelNode();
//                        LabelNode label65 = new LabelNode();
//
//                        LabelNode label66 = new LabelNode();


                        LabelNode label52 = new LabelNode();
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        instructions.add(new JumpInsnNode(Opcodes.IFNULL, label52));
                        LabelNode label53 = new LabelNode();
                        instructions.add(label53);
                        instructions.add(new LineNumberNode(293, label53));
                        instructions.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/util/MovingObjectPosition"));
                        instructions.add(new InsnNode(Opcodes.DUP));
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/util/MovingObjectPosition", "<init>", "(Lnet/minecraft/entity/Entity;)V", false));
                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 5));




                        // 第一行指令
                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        instructions.add(new JumpInsnNode(Opcodes.IFNULL, label58));

                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/util/MovingObjectPosition", "entityHit", "Lnet/minecraft/entity/Entity;"));
//                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/util/MovingObjectPosition", "field_72308_g", "Lnet/minecraft/entity/Entity;"));

                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 444));
////
//                        // label60
//                        instructions.add(label60);
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 444));
//                        instructions.add(new JumpInsnNode(Opcodes.IFNULL, label58));
////
//                        // label61
//                        instructions.add(label61);
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 444));
//                        instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "lotr/common/entity/npc/LOTREntityNPC"));
//                        instructions.add(new JumpInsnNode(Opcodes.IFNE, label58));
////
//                        // label62
//                        instructions.add(label62);
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
//                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/projectile/LOTREntityProjectileBase", "shootingEntity", "Lnet/minecraft/entity/Entity;"));
//                        instructions.add(new TypeInsnNode(Opcodes.INSTANCEOF, "lotr/common/entity/npc/LOTREntityNPC"));
//                        instructions.add(new JumpInsnNode(Opcodes.IFEQ, label58));
//
//                        // label63
//                        instructions.add(label63);
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 444));
//                        instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "lotr/common/entity/npc/LOTREntityNPC"));
//                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 666));
//
////                         label64
//                        instructions.add(label64);
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
//                        instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/projectile/LOTREntityProjectileBase", "shootingEntity", "Lnet/minecraft/entity/Entity;"));
//                        instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, "lotr/common/entity/npc/LOTREntityNPC"));
//                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 669));
//
////                         label65
//                        instructions.add(label65);
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 666));
//                        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getFaction", "()Llotr/common/fac/LOTRFaction;", false));
//                        instructions.add(new VarInsnNode(Opcodes.ALOAD, 669));
//                        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getFaction", "()Llotr/common/fac/LOTRFaction;", false));
//                        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/fac/LOTRFaction", "isBadRelation", "(Llotr/common/fac/LOTRFaction;)Z", false));
//                        instructions.add(new JumpInsnNode(Opcodes.IFNE, label58));
//
//                        // label66
//                        instructions.add(label66);
//                        instructions.add(new InsnNode(Opcodes.ACONST_NULL));
//                        instructions.add(new VarInsnNode(Opcodes.ASTORE, 5));

                        // label58

                        instructions.add(label58);

                        instructions.add(label52);

                        method.instructions.insertBefore(jumpNode.getPrevious().getPrevious().getPrevious().getPrevious(),instructions);//4个
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
                if(methodNode.name.equals("getArmorModel") && methodNode.owner.equals("net/minecraftforge/client/ForgeHooksClient") && methodNode.desc.equals("(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;ILnet/minecraft/client/model/ModelBiped;)Lnet/minecraft/client/model/ModelBiped;")) {
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

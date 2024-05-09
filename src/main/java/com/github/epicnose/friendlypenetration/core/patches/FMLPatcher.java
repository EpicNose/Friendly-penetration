package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.core.patches.base.Patcher;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

//import static jdk.internal.org.objectweb.asm.Opcodes.ALOAD;
//
//import static jdk.internal.org.objectweb.asm.Opcodes.RETURN;


public class FMLPatcher extends Patcher {

    public FMLPatcher() {
        super("FML");

        this.classes.put("cpw.mods.fml.common.FMLModContainer", (classNode) -> patchModContainer(classNode));
        this.classes.put("cpw.mods.fml.common.Loader", (classNode) -> patchLoader(classNode));

        this.classes.put("lotr.common.entity.npc.LOTREntityNPC",(classNode -> patchNPCArrowAttack(classNode)));
    }


    private void patchNPCArrowAttack(ClassNode classNode){

        MethodNode methodNode = ASMUtils.findMethod(classNode, "npcArrowAttack", "(Lnet/minecraft/entity/EntityLivingBase;F)V");
//        methodNode.localVariables.clear();
        for(int i =0;i<methodNode.localVariables.size();i++){
//            UCPCoreMod.log.info(i+"号localNode"+"desc:"+methodNode.localVariables.get(i).desc);
//            UCPCoreMod.log.info(i+"号localNode"+"name:"+methodNode.localVariables.get(i).name);
//            UCPCoreMod.log.info(i+"号localNode"+"sig:"+methodNode.localVariables.get(i).signature);
//            methodNode.localVariables.remove(i);
        }
        methodNode.instructions.clear();
        if(methodNode.localVariables.size()==9){
            methodNode.localVariables.remove(8);
            { //传承版
                InsnList instructions = new InsnList();

                LabelNode label0 = new LabelNode();
                instructions.add(label0);


                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); //patch调整存档
                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_70694_bm", "()Lnet/minecraft/item/ItemStack;", false));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getHeldItem", "()Lnet/minecraft/item/ItemStack;", false));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getHeldItem", "()Lnet/minecraft/item/ItemStack;", false));

                instructions.add(new VarInsnNode(Opcodes.ASTORE, 3));//patch调整存档

                LabelNode label1 = new LabelNode();
                instructions.add(label1);
//            instructions.add(new LineNumberNode(900, label1));
                instructions.add(new LdcInsnNode(new Float("1.1"))); //原来默认是1.3
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));//patch调整存档

//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getDistanceToEntity", "(Lnet/minecraft/entity/Entity;)F", false));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_70032_d", "(Lnet/minecraft/entity/Entity;)F", false));

                instructions.add(new LdcInsnNode(new Float("80.0")));
                instructions.add(new InsnNode(Opcodes.FDIV));
                instructions.add(new InsnNode(Opcodes.FADD));
                instructions.add(new VarInsnNode(Opcodes.FSTORE, 4));//patch调整存档

                instructions.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/item/ItemStack"));
                instructions.add(new InsnNode(Opcodes.DUP));
                instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", "field_151032_g", "Lnet/minecraft/item/Item;"));
//            instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", "arrow", "Lnet/minecraft/item/Item;"));

                instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;)V", false));
                instructions.add(new VarInsnNode(Opcodes.ASTORE, 8));



                LabelNode label2 = new LabelNode();
                instructions.add(label2);
//            instructions.add(new LineNumberNode(901, label2));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

                instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/entity/npc/LOTREntityNPC", "npcRangedAccuracy", "Lnet/minecraft/entity/ai/attributes/IAttribute;"));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getEntityAttribute", "(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;", false));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_110148_a", "(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;", false));

//            instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/entity/ai/attributes/IAttributeInstance", "getAttributeValue", "()D", true));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/entity/ai/attributes/IAttributeInstance", "func_111126_e", "()D", true));

                instructions.add(new InsnNode(Opcodes.D2F));
                instructions.add(new VarInsnNode(Opcodes.FSTORE, 5));//patch调整存档

                LabelNode label3 = new LabelNode();
                instructions.add(label3);
//            instructions.add(new LineNumberNode(903, label3));
                instructions.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"));
                instructions.add(new InsnNode(Opcodes.DUP));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

//            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "worldObj", "Lnet/minecraft/world/World;"));
                instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;"));

                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));//patch调整存档
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 8));//patch调整存档
                instructions.add(new VarInsnNode(Opcodes.FLOAD, 4));//patch调整存档
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));//patch调整存档

                instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lotr/common/item/LOTRItemBow", "getLaunchSpeedFactor", "(Lnet/minecraft/item/ItemStack;)F", false));
                instructions.add(new InsnNode(Opcodes.FMUL));
                instructions.add(new VarInsnNode(Opcodes.FLOAD, 5));//patch调整存档

                instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;FF)V", false));
                instructions.add(new VarInsnNode(Opcodes.ASTORE, 10));

                LabelNode label4 = new LabelNode();
                instructions.add(label4);
//            instructions.add(new LineNumberNode(904, label4));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));//patch调整存档

                LabelNode label5 = new LabelNode();
                instructions.add(new JumpInsnNode(Opcodes.IFNULL, label5));
                instructions.add(label5);
//            instructions.add(new LineNumberNode(907, label5));
                instructions.add(new FrameNode(Opcodes.F_FULL, 7, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT, Opcodes.FLOAT, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}, 0, new Object[] {}));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

                instructions.add(new LdcInsnNode("random.bow"));
                instructions.add(new InsnNode(Opcodes.FCONST_1));
                instructions.add(new InsnNode(Opcodes.FCONST_1));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

                instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70146_Z", "Ljava/util/Random;"));
//            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "rand", "Ljava/util/Random;"));

                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextFloat", "()F", false));
                instructions.add(new LdcInsnNode(new Float("0.4")));
                instructions.add(new InsnNode(Opcodes.FMUL));
                instructions.add(new LdcInsnNode(new Float("0.8")));
                instructions.add(new InsnNode(Opcodes.FADD));
                instructions.add(new InsnNode(Opcodes.FDIV));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "playSound", "(Ljava/lang/String;FF)V", false));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_85030_a", "(Ljava/lang/String;FF)V", false));

                LabelNode label6 = new LabelNode();
                instructions.add(label6);
//            instructions.add(new LineNumberNode(908, label6));
                instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

//            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "worldObj", "Lnet/minecraft/world/World;"));
                instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;"));

                instructions.add(new VarInsnNode(Opcodes.ALOAD, 10));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "spawnEntityInWorld", "(Lnet/minecraft/entity/Entity;)Z", false));
                instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "func_72838_d", "(Lnet/minecraft/entity/Entity;)Z", false));

                instructions.add(new InsnNode(Opcodes.POP));

                LabelNode label7 = new LabelNode();
                instructions.add(label7);
//            instructions.add(new LineNumberNode(909, label7));
                instructions.add(new InsnNode(Opcodes.RETURN));
//
//            LabelNode label8 = new LabelNode();
//            instructions.add(label8);
//            V

//            instructions.add(new LocalVariableNode("this", "Llotr/common/entity/npc/LOTREntityNPC;", null, label0, label8, 0));
//            instructions.add(new LocalVariableNode("target", "Lnet/minecraft/entity/EntityLivingBase;", null, label0, label8, 1));
//            instructions.add(new LocalVariableNode("f", "F", null, label0, label8, 2));
//            instructions.add(new LocalVariableNode("heldItem", "Lnet/minecraft/item/ItemStack;", null, label1, label8, 3));
//            instructions.add(new LocalVariableNode("str", "F", null, label2, label8, 4));
//            instructions.add(new LocalVariableNode("accuracy", "F", null, label3, label8, 5));
//            instructions.add(new LocalVariableNode("arrow", "Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;", null, label4, label8, 6));

//            methodNode.maxLocals = 7;
//            methodNode.maxStack = 8;

                methodNode.instructions.add(instructions);

            }
        }else {
            //重制版
            InsnList instructions = new InsnList();

//            LabelNode label0 = new LabelNode();
//            instructions.add(label0);


            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0)); //patch调整存档
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_70694_bm", "()Lnet/minecraft/item/ItemStack;", false));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getHeldItem", "()Lnet/minecraft/item/ItemStack;", false));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getHeldItem", "()Lnet/minecraft/item/ItemStack;", false));

            instructions.add(new VarInsnNode(Opcodes.ASTORE, 3));//patch调整存档

//            LabelNode label1 = new LabelNode();
//            instructions.add(label1);
//            instructions.add(new LineNumberNode(900, label1));
            instructions.add(new LdcInsnNode(new Float("1.1"))); //原来默认是1.3
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));//patch调整存档

//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getDistanceToEntity", "(Lnet/minecraft/entity/Entity;)F", false));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_70032_d", "(Lnet/minecraft/entity/Entity;)F", false));

            instructions.add(new LdcInsnNode(new Float("80.0")));
            instructions.add(new InsnNode(Opcodes.FDIV));
            instructions.add(new InsnNode(Opcodes.FADD));
            instructions.add(new VarInsnNode(Opcodes.FSTORE, 4));//patch调整存档

            instructions.add(new TypeInsnNode(Opcodes.NEW, "net/minecraft/item/ItemStack"));
            instructions.add(new InsnNode(Opcodes.DUP));
            instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", "field_151032_g", "Lnet/minecraft/item/Item;"));
//            instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", "arrow", "Lnet/minecraft/item/Item;"));

            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;)V", false));
            instructions.add(new VarInsnNode(Opcodes.ASTORE, 8));



//            LabelNode label2 = new LabelNode();
//            instructions.add(label2);
//            instructions.add(new LineNumberNode(901, label2));
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

            instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "lotr/common/entity/npc/LOTREntityNPC", "npcRangedAccuracy", "Lnet/minecraft/entity/ai/attributes/IAttribute;"));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getEntityAttribute", "(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;", false));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_110148_a", "(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;", false));

//            instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/entity/ai/attributes/IAttributeInstance", "getAttributeValue", "()D", true));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "net/minecraft/entity/ai/attributes/IAttributeInstance", "func_111126_e", "()D", true));

            instructions.add(new InsnNode(Opcodes.D2F));
            instructions.add(new VarInsnNode(Opcodes.FSTORE, 5));//patch调整存档

//            LabelNode label3 = new LabelNode();
//            instructions.add(label3);
////            instructions.add(new LineNumberNode(903, label3));

            instructions.add(new VarInsnNode(Opcodes.FLOAD, 4));//patch调整存档  str
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));//patch调整存档 heldItem
            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lotr/common/item/LOTRItemBow", "getLaunchSpeedFactor", "(Lnet/minecraft/item/ItemStack;)F", false));
            instructions.add(new InsnNode(Opcodes.FMUL));
            instructions.add(new VarInsnNode(Opcodes.FSTORE, 7));//patch调整存档


////            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//这里是worldobject的获取与存储  本来是想减少栈深度  直接换个Init方法 减一个参数了
//////            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "worldObj", "Lnet/minecraft/world/World;"));
////            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;"));
////            instructions.add(new VarInsnNode(Opcodes.ASTORE, 9));//patch调整存档
//
////            instructions.add(new InsnNode(Opcodes.POP));   //空一个栈出来 不然下面挤不下
////            instructions.add(new VarInsnNode(Opcodes.POP, 4));//patch调整存档  str
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

//            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "worldObj", "Lnet/minecraft/world/World;"));
            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;"));
            instructions.add(new VarInsnNode(Opcodes.ASTORE, 9));//patch调整存档

           instructions.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"));
           instructions.add(new InsnNode(Opcodes.DUP));
           instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
           instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
           instructions.add(new VarInsnNode(Opcodes.FLOAD, 4));
           instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
           instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lotr/common/item/LOTRItemBow", "getLaunchSpeedFactor", "(Lnet/minecraft/item/ItemStack;)F", false));
           instructions.add(new InsnNode(Opcodes.FMUL));
           instructions.add(new VarInsnNode(Opcodes.FLOAD, 5));
           instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;FF)V", false));
           instructions.add(new VarInsnNode(Opcodes.ASTORE, 6));

//            instructions.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"));
//            instructions.add(new InsnNode(Opcodes.DUP));
////            instructions.add(new VarInsnNode(Opcodes.ALOAD, 9));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.FLOAD, 7));//charge
//            instructions.add(new VarInsnNode(Opcodes.FLOAD, 5));//accruacy
//
////////            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;FF)V", false));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;FF)V", false));

            //换了个类init就没问题
//            instructions.add(new TypeInsnNode(Opcodes.NEW, "lotr/common/entity/LOTREntities"));
//            instructions.add(new InsnNode(Opcodes.DUP));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "lotr/common/entity/LOTREntities", "<init>", "()V", false));
//            instructions.add(new LdcInsnNode(Type.getType("Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;")));
//            instructions.add(new LdcInsnNode("LOTRArrow"));
//            instructions.add(new IntInsnNode(Opcodes.BIPUSH, 2077)); // id
//            instructions.add(new IntInsnNode(Opcodes.BIPUSH, 64)); // updateRange
//            instructions.add(new IntInsnNode(Opcodes.BIPUSH, 20)); // updateFreq
//            instructions.add(new InsnNode(Opcodes.ICONST_0)); // false (sendVelocityUpdates)
//            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lotr/common/entity/LOTREntities", "registerEntity", "(Ljava/lang/Class;Ljava/lang/String;IIIZ)V", false));


//            instructions.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"));
//            instructions.add(new InsnNode(Opcodes.DUP));
//            instructions.add(new FrameNode(Opcodes.F_FULL, 9, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT,"net/minecraft/item/ItemStack",Opcodes.FLOAT,Opcodes.FLOAT}, 1, new Object[] {"com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}));
//            instructions.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"));
//            instructions.add(new InsnNode(Opcodes.DUP));
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 9));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.FLOAD, 4));//patch调整存档
////            instructions.add(new InsnNode(Opcodes.FCONST_1)); // false (sendVelocityUpdates)
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));//patch调整存档
////
//            instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lotr/common/item/LOTRItemBow", "getLaunchSpeedFactor", "(Lnet/minecraft/item/ItemStack;)F", false));
//            instructions.add(new InsnNode(Opcodes.FMUL));
//            instructions.add(new VarInsnNode(Opcodes.FLOAD, 5));//patch调整存档
////
//            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;FF)V", false));
//            instructions.add(new VarInsnNode(Opcodes.ASTORE, 10));

//            instructions.add(new FrameNode(Opcodes.F_FULL, 5, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT, Opcodes.FLOAT, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}, 0, new Object[] {}));
//            LabelNode label4 = new LabelNode();
//            instructions.add(label4);
//            instructions.add(new LineNumberNode(904, label4));



//            instructions.add(new LineNumberNode(907, label5));




//            instructions.add(new TypeInsnNode(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"));
//            instructions.add(new InsnNode(Opcodes.DUP));
//////            instructions.add(new InsnNode(Opcodes.POP));
//////            instructions.add(new VarInsnNode(Opcodes.ALOAD, 9));//patch调整存档 worldobj
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档 this lotrentitynpc
//            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;"));
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档 this lotrentitynpc
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));//patch调整存档  targetnpc
//////            instructions.add(new VarInsnNode(Opcodes.ALOAD, 8));//patch调整存档  ItemStack(arrow)
//            instructions.add(new VarInsnNode(Opcodes.FLOAD, 7));//patch调整存档
//            instructions.add(new VarInsnNode(Opcodes.FLOAD, 5));//patch调整存档
//////            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;FF)V", false));
//            instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;FF)V", false));
////            instructions.add(new FrameNode(Opcodes.F_FULL, 9, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT,"net/minecraft/item/ItemStack",Opcodes.FLOAT,Opcodes.FLOAT}, 1, new Object[] {"com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}));
//            instructions.add(new VarInsnNode(Opcodes.ASTORE, 10));


//            LabelNode label4 = new LabelNode();
//            instructions.add(label4);
////////            instructions.add(new LineNumberNode(904, label4));
//            instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));//patch调整存档
//
//            LabelNode label5 = new LabelNode();
//            instructions.add(new JumpInsnNode(Opcodes.IFNULL, label5));
//            instructions.add(label5);
//////            instructions.add(new LineNumberNode(907, label5));
//            instructions.add(new FrameNode(Opcodes.F_FULL, 8, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT,Opcodes.FLOAT,"com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}, 0, new Object[] {}));


            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

            instructions.add(new LdcInsnNode("random.bow"));
            instructions.add(new InsnNode(Opcodes.FCONST_1));
            instructions.add(new InsnNode(Opcodes.FCONST_1));
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档

            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70146_Z", "Ljava/util/Random;"));
////            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "rand", "Ljava/util/Random;"));
//
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextFloat", "()F", false));
            instructions.add(new LdcInsnNode(new Float("0.4")));
            instructions.add(new InsnNode(Opcodes.FMUL));
            instructions.add(new LdcInsnNode(new Float("0.8")));
            instructions.add(new InsnNode(Opcodes.FADD));
            instructions.add(new InsnNode(Opcodes.FDIV));
////            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "playSound", "(Ljava/lang/String;FF)V", false));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_85030_a", "(Ljava/lang/String;FF)V", false));
//
//            LabelNode label6 = new LabelNode();
//            instructions.add(label6);
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));//patch调整存档
//
////            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "worldObj", "Lnet/minecraft/world/World;"));
            instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;"));
//
            instructions.add(new VarInsnNode(Opcodes.ALOAD, 6));
////            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "spawnEntityInWorld", "(Lnet/minecraft/entity/Entity;)Z", false));
            instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "func_72838_d", "(Lnet/minecraft/entity/Entity;)Z", false));

//            instructions.add(new InsnNode(Opcodes.POP));

//            LabelNode label7 = new LabelNode();
//            instructions.add(label7);

            instructions.add(new InsnNode(Opcodes.RETURN));


            methodNode.instructions.add(instructions);

//            {
////                methodNode = classWriter.visitMethod(ACC_PUBLIC, "npcArrowAttack", "(Lnet/minecraft/entity/EntityLivingBase;F)V", null, null);
//                methodNode.visitCode();
//                Label label0 = new Label();
//                methodNode.visitLabel(label0);
//                methodNode.visitLineNumber(926, label0);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
////                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "getHeldItem", "()Lnet/minecraft/item/ItemStack;", false);
//
//                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_70694_bm", "()Lnet/minecraft/item/ItemStack;", false);
//                methodNode.visitVarInsn(Opcodes.ASTORE, 3);
//                Label label1 = new Label();
//                methodNode.visitLabel(label1);
//                methodNode.visitLineNumber(927, label1);
//                methodNode.visitLdcInsn(new Float("1.3"));
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 1);
//                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_70032_d", "(Lnet/minecraft/entity/Entity;)F", false);
////                getDistanceToEntity
//                methodNode.visitLdcInsn(new Float("80.0"));
//                methodNode.visitInsn(Opcodes.FDIV);
//                methodNode.visitInsn(Opcodes.FADD);
//                methodNode.visitVarInsn(Opcodes.FSTORE, 4);
//                Label label2 = new Label();
//                methodNode.visitLabel(label2);
//                methodNode.visitLineNumber(928, label2);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
//                methodNode.visitFieldInsn(Opcodes.GETSTATIC, "lotr/common/entity/npc/LOTREntityNPC", "npcRangedAccuracy", "Lnet/minecraft/entity/ai/attributes/IAttribute;");
//                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_110148_a", "(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;", false);
////                getEntityAttribute
//                methodNode.visitMethodInsn(Opcodes.INVOKEINTERFACE, "net/minecraft/entity/ai/attributes/IAttributeInstance", "func_111126_e", "()D", true);
////                getAttributeValue
//                methodNode.visitInsn(Opcodes.D2F);
//                methodNode.visitVarInsn(Opcodes.FSTORE, 5);
//                Label label3 = new Label();
//                methodNode.visitLabel(label3);
//                methodNode.visitLineNumber(931, label3);
//                methodNode.visitVarInsn(Opcodes.FLOAD, 4);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 3);
//                methodNode.visitMethodInsn(Opcodes.INVOKESTATIC, "lotr/common/item/LOTRItemBow", "getLaunchSpeedFactor", "(Lnet/minecraft/item/ItemStack;)F", false);
//                methodNode.visitInsn(Opcodes.FMUL);
//                methodNode.visitVarInsn(Opcodes.FSTORE, 6);
////
//                Label label4 = new Label();
//                methodNode.visitLabel(label4);
//                methodNode.visitLineNumber(932, label4);
//                methodNode.visitTypeInsn(Opcodes.NEW, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow");
//                methodNode.visitInsn(Opcodes.DUP);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
//                methodNode.visitFieldInsn(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;");
////                worldObj
//
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 1);
//                methodNode.visitVarInsn(Opcodes.FLOAD, 6);
//                methodNode.visitVarInsn(Opcodes.FLOAD, 5);
//                methodNode.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow", "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;FF)V", false);
//                methodNode.visitVarInsn(Opcodes.ASTORE, 7);
//
//                Label label5 = new Label();
//                methodNode.visitLabel(label5);
//                methodNode.visitLineNumber(937, label5);
//
//                methodNode.visitFrame(Opcodes.F_FULL, 8, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT, Opcodes.FLOAT,Opcodes.FLOAT, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}, 0, new Object[] {});
////                instructions.add(new FrameNode(Opcodes.F_FULL, 7, new Object[] {"lotr/common/entity/npc/LOTREntityNPC", "net/minecraft/entity/EntityLivingBase", Opcodes.FLOAT, "net/minecraft/item/ItemStack", Opcodes.FLOAT, Opcodes.FLOAT, "com/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow"}, 0, new Object[] {}));
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
//                methodNode.visitLdcInsn("random.bow");
//                methodNode.visitInsn(Opcodes.FCONST_1);
//                methodNode.visitInsn(Opcodes.FCONST_1);
//                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
//                methodNode.visitFieldInsn(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70146_Z", "Ljava/util/Random;");
//                //rand
//                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextFloat", "()F", false);
//                methodNode.visitLdcInsn(new Float("0.4"));
//                methodNode.visitInsn(Opcodes.FMUL);
//                methodNode.visitLdcInsn(new Float("0.8"));
//                methodNode.visitInsn(Opcodes.FADD);
//                methodNode.visitInsn(Opcodes.FDIV);
//                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "lotr/common/entity/npc/LOTREntityNPC", "func_85030_a", "(Ljava/lang/String;FF)V", false);
//                //sound
//
//
////                Label label6 = new Label();
////                methodNode.visitLabel(label6);
////                methodNode.visitLineNumber(938, label6);
////                methodNode.visitVarInsn(Opcodes.ALOAD, 0);
////                methodNode.visitFieldInsn(Opcodes.GETFIELD, "lotr/common/entity/npc/LOTREntityNPC", "field_70170_p", "Lnet/minecraft/world/World;");
////                //worldobj
////                methodNode.visitVarInsn(Opcodes.ALOAD, 7);
////                methodNode.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "func_72838_d", "(Lnet/minecraft/entity/Entity;)Z", false);
////                //spawnentityinworld
////                methodNode.visitInsn(Opcodes.POP);
//                Label label7 = new Label();
//                methodNode.visitLabel(label7);
//                methodNode.visitLineNumber(939, label7);
//                methodNode.visitInsn(Opcodes.RETURN);
////                Label label8 = new Label();
////                methodNode.visitLabel(label8);
////                methodNode.visitLocalVariable("this", "Llotr/common/entity/npc/LOTREntityNPC;", null, label0, label8, 0);
////                methodNode.visitLocalVariable("target", "Lnet/minecraft/entity/EntityLivingBase;", null, label0, label8, 1);
////                methodNode.visitLocalVariable("f", "F", null, label0, label8, 2);
////                methodNode.visitLocalVariable("heldItem", "Lnet/minecraft/item/ItemStack;", null, label1, label8, 3);
////                methodNode.visitLocalVariable("str", "F", null, label2, label8, 4);
////                methodNode.visitLocalVariable("accuracy", "F", null, label3, label8, 5);
////                methodNode.visitLocalVariable("charge", "F", null, label4, label8, 6);
////                methodNode.visitLocalVariable("arrow", "Lcom/github/epicnose/friendlypenetration/common/entity/item/LOTREntityArrow;", null, label5, label8, 7);
////                methodNode.visitMaxs(7, 8);
//                methodNode.visitEnd();
//            }



        }

//        if(methodNode == null) return;

//        methodNode







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

package com.github.epicnose.friendlypenetration.core.utils;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import org.objectweb.asm.tree.*;
import net.minecraftforge.classloading.FMLForgePlugin;

import java.util.ArrayList;

public class ASMUtils {

    public static MethodNode findMethod(ClassNode classNode, String targetMethodName, String obfTargetMethodName, String targetMethodDesc) {
        return findMethod(classNode, FMLForgePlugin.RUNTIME_DEOBF ? obfTargetMethodName : targetMethodName, targetMethodDesc); // RUNTIME_DEOBF seems to be inversed
    }

    public static MethodNode findMethod(ClassNode classNode, String targetMethodName, String targetMethodDesc) {
        for(MethodNode method : classNode.methods) {
            if(method.name.equals(targetMethodName) && method.desc.equals(targetMethodDesc)) return method;
        }

        UCPCoreMod.log.error("Couldn't find method " + targetMethodName + " with desc " + targetMethodDesc + " in " + classNode.name);
        return null;
    }

    public static void removeCodeLine(MethodNode m, int line) {
        ArrayList<AbstractInsnNode> toRemove = new ArrayList();
        for (int i = 0; i < m.instructions.size(); i++) {
            AbstractInsnNode ain = m.instructions.get(i);
            if (ain instanceof LineNumberNode) {
                if (((LineNumberNode)ain).line == line) {
                    toRemove.add(ain.getPrevious()); //"L#"
                    while (!(ain.getNext() instanceof LineNumberNode)) {
                        toRemove.add(ain);
                        ain = ain.getNext();
                    }
                }
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            AbstractInsnNode insn = toRemove.get(i);
            m.instructions.remove(insn);
        }
    }
    
    public static void removePreviousNodes(InsnList list, AbstractInsnNode start, int amount) {
        for(int i = 0; i < amount; i++) {
            AbstractInsnNode prevNode = start.getPrevious();
            list.remove(prevNode);
        }
    }
    public static AbstractInsnNode getLastOpcode(InsnList li, int opcode) {
        AbstractInsnNode ret = null;
        for (int i = 0; i < li.size(); i++) {
            AbstractInsnNode ain = li.get(i);
            if (ain.getOpcode() == opcode)
                ret = ain;
        }
        return ret;
    }

}

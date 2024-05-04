package com.github.epicnose.friendlypenetration.core.patches.base;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.objectweb.asm.tree.ClassNode;

public abstract class Patcher {

    protected Map<String, Consumer<ClassNode>> classes;
    private String patcherName;

    public Patcher(String name) {
        classes = new HashMap<String, Consumer<ClassNode>>();

        patcherName = name;
    }

    public LoadingPhase getLoadPhase() {
        return LoadingPhase.CORE_MOD_LOADING;
    }

    public boolean shouldInit() {
        return true;
    }

    public boolean isDone() {
        return classes.isEmpty();
    }

    public boolean canRun(String className) {
        return classes.containsKey(className);
    }

    public void run(String className, ClassNode classNode) {
        classes.get(className).accept(classNode);
        classes.remove(className);
    }

    public String getName() {
        return patcherName;
    }

    public static enum LoadingPhase {
        CORE_MOD_LOADING, 
        FORGE_MOD_LOADING;
    }

}

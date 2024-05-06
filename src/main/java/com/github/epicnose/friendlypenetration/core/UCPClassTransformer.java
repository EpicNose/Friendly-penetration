package com.github.epicnose.friendlypenetration.core;

import com.github.epicnose.friendlypenetration.core.hooks.PreMCHooks;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import com.github.epicnose.friendlypenetration.core.patches.base.Patcher;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class UCPClassTransformer implements IClassTransformer {

    static {
        FMLLaunchHandler launchHandler = ReflectionHelper.getPrivateValue(FMLLaunchHandler.class, null, "INSTANCE");
        LaunchClassLoader classLoader = ReflectionHelper.getPrivateValue(FMLLaunchHandler.class, launchHandler, "classLoader");
        
        PreMCHooks.transformerExclusionsTweaks(classLoader);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        boolean ran = false;

        for(Patcher patcher : UCPCoreMod.activePatches) {
            UCPCoreMod.log.info("[FR]尝试 " + patcher.getName() + " for " + name);
            if(patcher.canRun(name)) {
                ran = true;

                ClassNode classNode = new ClassNode();
                ClassReader classReader = new ClassReader(classBytes);
                classReader.accept(classNode, 0);

                UCPCoreMod.log.info("[FR]Running patcher " + patcher.getName() + " for " + name);
                patcher.run(name, classNode);

                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);
                classBytes = writer.toByteArray();
            }
        }

        if(ran) {
            UCPCoreMod.activePatches.removeIf(patcher -> patcher.isDone());
            if(UCPCoreMod.activePatches.isEmpty()) UCPCoreMod.log.info("[FP]GAGAGARan all active patches.");
        }

        return classBytes;
    }
}

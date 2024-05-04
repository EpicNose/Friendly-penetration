package com.github.epicnose.friendlypenetration.core.hooks;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class PreMCHooks {

    private static List<String> loadOrderMods = Arrays.asList("Botania", "DragonAPI", "TwilightForest");

    public static void forgeLoadOrderHook(ModMetadata mmd) {
        if(loadOrderMods.contains(mmd.modId)) {
            mmd.dependencies.add(VersionParser.parseVersionReference("lotr"));
            UCPCoreMod.log.info("Succesfully patched the load order for " + mmd.modId);
        }
    }

    public static void postFMLLoad() {
        UCPCoreMod.loadModPatches();
    }
    
    public static void transformerExclusionsTweaks(LaunchClassLoader classLoader) {
        Set<String> exclusions = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, classLoader, "transformerExceptions");

        exclusions.remove("lotr");
        exclusions.add("lotr.common.coremod");
    }
}

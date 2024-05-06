package com.github.epicnose.friendlypenetration.core;

import java.util.Map;

import com.github.epicnose.friendlypenetration.core.patches.*;
import org.apache.logging.log4j.LogManager;
import cpw.mods.fml.relauncher.IFMLCallHook;

public class UCPCoreSetup implements IFMLCallHook {

    @Override
    public Void call() throws Exception {
        UCPCoreMod.log = LogManager.getLogger("LOTR-FP");

        UCPCoreMod.registerPatcher(new FMLPatcher());
//        UCPCoreMod.registerPatcher(new BotaniaPatcher());
//        UCPCoreMod.registerPatcher(new ScreenshotEnhancedPatcher());
//        UCPCoreMod.registerPatcher(new ThaumcraftPatcher());
        UCPCoreMod.registerPatcher(new LOTRPatcher());
//        UCPCoreMod.registerPatcher(new LOTR2Patcher());
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }
}

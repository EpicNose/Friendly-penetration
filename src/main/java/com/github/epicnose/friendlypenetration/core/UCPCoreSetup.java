package com.github.epicnose.friendlypenetration.core;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import cpw.mods.fml.relauncher.IFMLCallHook;
import com.github.epicnose.friendlypenetration.core.patches.BotaniaPatcher;
import com.github.epicnose.friendlypenetration.core.patches.FMLPatcher;
import com.github.epicnose.friendlypenetration.core.patches.LOTRPatcher;
import com.github.epicnose.friendlypenetration.core.patches.ScreenshotEnhancedPatcher;
import com.github.epicnose.friendlypenetration.core.patches.ThaumcraftPatcher;

public class UCPCoreSetup implements IFMLCallHook {

    @Override
    public Void call() throws Exception {
        UCPCoreMod.log = LogManager.getLogger("LOTR-UCP");

        UCPCoreMod.registerPatcher(new FMLPatcher());
//        UCPCoreMod.registerPatcher(new BotaniaPatcher());
//        UCPCoreMod.registerPatcher(new ScreenshotEnhancedPatcher());
//        UCPCoreMod.registerPatcher(new ThaumcraftPatcher());
        UCPCoreMod.registerPatcher(new LOTRPatcher());

        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }
}

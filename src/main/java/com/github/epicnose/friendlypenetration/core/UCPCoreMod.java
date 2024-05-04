package com.github.epicnose.friendlypenetration.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import com.github.epicnose.friendlypenetration.core.patches.base.Patcher;
import com.github.epicnose.friendlypenetration.core.patches.base.Patcher.LoadingPhase;

@IFMLLoadingPlugin.TransformerExclusions(value = {"io.gitlab.dwarfyassassin.lotrucp.core"})
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class UCPCoreMod implements IFMLLoadingPlugin {

    public static Logger log;
    public static List<Patcher> activePatches = new ArrayList<Patcher>();
    private static List<Patcher> modPatches = new ArrayList<Patcher>();

    static {
        System.out.println("LOTR-UCP: Found core mod.");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {UCPClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return UCPCoreSetup.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static void registerPatcher(Patcher patcher) {
        if(patcher.getLoadPhase() == LoadingPhase.CORE_MOD_LOADING && patcher.shouldInit()) activePatches.add(patcher);
        else if(patcher.getLoadPhase() == LoadingPhase.FORGE_MOD_LOADING) modPatches.add(patcher);
    }

    public static void loadModPatches() {
        int i = 0;

        for(Patcher patcher : modPatches) {
            if(patcher.shouldInit()) {
                activePatches.add(patcher);
                i++;
            }
        }

        UCPCoreMod.log.info("Loaded " + i + " mod patches.");

        modPatches.clear();
    }
}

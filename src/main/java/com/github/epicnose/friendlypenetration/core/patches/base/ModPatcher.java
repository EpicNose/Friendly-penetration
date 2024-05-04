package com.github.epicnose.friendlypenetration.core.patches.base;

import cpw.mods.fml.common.Loader;

public abstract class ModPatcher extends Patcher {
    private String modID;

    public ModPatcher(String name, String modID) {
        super(name);
        this.modID = modID;
    }

    @Override
    public LoadingPhase getLoadPhase() {
        return LoadingPhase.FORGE_MOD_LOADING;
    }

    @Override
    public boolean shouldInit() {
        return Loader.isModLoaded(modID);
    }

}

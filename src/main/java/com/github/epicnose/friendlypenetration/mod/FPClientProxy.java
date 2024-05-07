package com.github.epicnose.friendlypenetration.mod;

import com.github.epicnose.friendlypenetration.client.render.entity.LOTRRenderArrow;
import com.github.epicnose.friendlypenetration.common.entity.item.LOTREntityArrow;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lotr.client.render.entity.LOTRRenderThrowingAxe;

public class FPClientProxy extends FPServerProxy{
    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);

//        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderThrowingAxe());
    }
//    @Mod.EventHandler
//    public void preload(FMLPreInitializationEvent event) {
//        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
//    }
}

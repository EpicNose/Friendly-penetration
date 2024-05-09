package com.github.epicnose.friendlypenetration.mod;

import com.github.epicnose.friendlypenetration.client.render.entity.LOTRRenderArrow;
import com.github.epicnose.friendlypenetration.common.entity.item.LOTREntityArrow;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class FPClientProxy extends FPServerProxy{
//    @Override
//    public void onInit(FMLInitializationEvent event) {
//        super.onInit(event);
////        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
////        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderThrowingAxe());
//    }
//    @Override
//    public void onLoad(){
//        System.out.println("clientclientclient");
////        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
//    }
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
    }
//    @Override
//    public void preload(FMLPreInitializationEvent event) {
//
//    }
//    @Mod.EventHandler
//    public void preload(FMLPreInitializationEvent event) {
//        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
//    }
}

package com.github.epicnose.friendlypenetration.mod;


import com.github.epicnose.friendlypenetration.client.render.entity.LOTRRenderArrow;
import com.github.epicnose.friendlypenetration.common.entity.item.LOTREntityArrow;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lotr.common.entity.LOTREntities;

@Mod(modid = "friendlypenetration", name = "LOTR Friendly penetration",version = FriendlyPenetration.VERSION, dependencies = "after:lotr")
public class FriendlyPenetration {
    public static final String VERSION = "alpha-1.0.3";


    @SidedProxy(serverSide = "com.github.epicnose.friendlypenetration.mod.FPServerProxy", clientSide = "com.github.epicnose.friendlypenetration.mod.FPClientProxy")
    public static FPServerProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

        System.out.println("[FriendlyPenetration]友军穿透加载 MadeBy Epic_Nose");
        LOTREntities.registerEntity(LOTREntityArrow.class, "LOTRArrow", 1988, 64, 20, false);
//        proxy.onLoad();
    }
    @Mod.EventHandler
    public void preload(FMLPreInitializationEvent event) {
        proxy.preInit();
//        RenderingRegistry.registerEntityRenderingHandler(LOTREntityArrow.class, new LOTRRenderArrow());
    }


}

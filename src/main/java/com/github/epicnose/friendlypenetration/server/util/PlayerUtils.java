package com.github.epicnose.friendlypenetration.server.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;

public class PlayerUtils {
    
    public static UUID getLastKownUUIDFromUsername(String username) {
        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(username);

        if(player != null) return player.getGameProfile().getId();
        
        Map<UUID, String> userNameCache = UsernameCache.getMap();
        
        for(Entry<UUID, String> entry : userNameCache.entrySet()) {
            if(entry.getValue().equalsIgnoreCase(username)) return entry.getKey();
        }
        
        return null;
    }

}

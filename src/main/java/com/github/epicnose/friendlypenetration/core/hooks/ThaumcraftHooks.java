package com.github.epicnose.friendlypenetration.core.hooks;

import java.util.List;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.ReflectionHelper;
import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.server.util.PlayerUtils;
import lotr.common.LOTRBannerProtection.ProtectType;
import lotr.common.LOTRReflection;
import lotr.common.entity.item.LOTREntityBanner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import thaumcraft.common.entities.golems.EntityGolemBase;

public class ThaumcraftHooks {    
    public static ProtectType thaumcraftGolemBannerProtection(EntityPlayer player, LOTREntityBanner banner) {
        World world = player.worldObj;
        
        if(player instanceof FakePlayer) {
            FakePlayer fakePlayer = (FakePlayer) player;
            GameProfile profile = fakePlayer.getGameProfile();
            
            if(profile.getName().equals("FakeThaumcraftGolem")) {
                List<EntityGolemBase> foundGolems = world.getEntitiesWithinAABB(EntityGolemBase.class, player.boundingBox.expand(1, 1, 1));
                
                EntityGolemBase closestGolem = null;
                double foundDistance = Double.MAX_VALUE;
                for(EntityGolemBase golem : foundGolems) {
                    double distance = player.getDistanceSqToEntity(golem);
                    if(distance < foundDistance) closestGolem = golem;
                }
                
                if(closestGolem == null) return null;
                
                UUID uuid = PlayerUtils.getLastKownUUIDFromUsername(closestGolem.getOwnerName());
                try {
                    LOTRReflection.setFinalField(GameProfile.class, profile, uuid, "id");
                    ReflectionHelper.setPrivateValue(Entity.class, fakePlayer, uuid, "entityUniqueID", "field_96093_i");
                }
                catch(Exception e) {
                    UCPCoreMod.log.error("Was unable to set a FakeThaumcraftGolem player uuid to " + uuid.toString());
                    e.printStackTrace();
                }
                //We let it return null in the end so it continues the normal lotr code. This should work as we replace the fake player UUID with our owner UUID (if we found one).
            }
        }
        
        return null;
    }
}

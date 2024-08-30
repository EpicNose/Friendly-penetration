package com.github.epicnose.friendlypenetration.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lotr.common.entity.LOTREntityRegistry;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.fac.LOTRAlignmentValues;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class FREventHandler {
//    @SubscribeEvent
//    public void onLivingDeath(LivingDeathEvent event) {
//        DamageSource source = event.source;
//        EntityLivingBase entity = event.entityLiving;
//        World world = entity.worldObj;
//        EntityPlayer entityplayer;
//
//
//
//
//        if (!world.isRemote) {
//            entityplayer = null;
//            boolean creditHiredUnit = false;
//            boolean byNearbyUnit = false;
//            LOTRAlignmentValues.AlignmentBonus alignmentBonus = null;
//
//
//         if (source.getEntity() instanceof LOTREntityNPC) {
//             LOTREntityNPC npc = (LOTREntityNPC) source.getEntity();
//             if (npc.hiredNPCInfo.isActive && npc.hiredNPCInfo.getHiringPlayer() != null) {
//                 entityplayer = npc.hiredNPCInfo.getHiringPlayer();
//                 creditHiredUnit = true;
//                 double nearbyDist = 64.0;
//                 byNearbyUnit = npc.getDistanceSqToEntity(entityplayer) <= nearbyDist * nearbyDist;
//             }
//         }
//
//            if (entityplayer != null) {
//
//            }
//
//            if (!wasSelfDefenceAgainstAlliedUnit) {
//                if (entity instanceof LOTREntityNPC) {
//                    LOTREntityNPC npc = (LOTREntityNPC) entity;
//                    alignmentBonus = new LOTRAlignmentValues.AlignmentBonus(npc.getAlignmentBonus(), npc.getEntityClassName());
//                    alignmentBonus.needsTranslation = true;
//                    alignmentBonus.isCivilianKill = npc.isCivilianNPC();
//                } else {
//                    String s = EntityList.getEntityString(entity);
//                    Object obj = LOTREntityRegistry.registeredNPCs.get(s);
//                    if (obj != null) {
//                        LOTREntityRegistry.RegistryInfo info = (LOTREntityRegistry.RegistryInfo) obj;
//                        alignmentBonus = info.alignmentBonus;
//                        alignmentBonus.isCivilianKill = false;
//                    }
//                }
//            }
//
//            if (alignmentBonus != null && alignmentBonus.bonus != 0.0f && (!creditHiredUnit || creditHiredUnit && byNearbyUnit)) {
//                alignmentBonus.isKill = true;
//                if (creditHiredUnit) {
//                    alignmentBonus.killByHiredUnit = true;
//                }
//                playerData.addAlignment(entityplayer, alignmentBonus, entityFaction, forcedBonusFactions, entity);
//            }
//
//
//
//        }
//
//
//    }
}

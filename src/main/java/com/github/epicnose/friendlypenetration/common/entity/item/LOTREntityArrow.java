package com.github.epicnose.friendlypenetration.common.entity.item;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import cpw.mods.fml.common.FMLLog;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.LOTRPlayerData;
import lotr.common.enchant.LOTREnchantment;
import lotr.common.enchant.LOTREnchantmentHelper;
import lotr.common.entity.LOTREntityInvasionSpawner;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.projectile.LOTREntityProjectileBase;
import lotr.common.item.LOTRItemBow;
import lotr.common.item.LOTRWeaponStats;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.lang.reflect.Field;
import java.util.List;

public class LOTREntityArrow extends LOTREntityProjectileBase {
    public int ticksInGround;
    public int ticksInAir = 0;
    public int xTile = -1;
    public int yTile = -1;
    public int zTile = -1;
    public Block inTile;
    public int inData = 0;
    public boolean inGround = false;
    public int shake = 0;
    public Entity shootingEntity;
    //    public LOTREntityArrow(World p_i1753_1_) {
//        super(p_i1753_1_);
//    }
    private int knockbackStrength;
    private double damage = 2.0D;

    public void setDamage(double p_70239_1_) {
        this.damage = p_70239_1_;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int p_70240_1_) {
        this.knockbackStrength = p_70240_1_;
    }

    @Override
    public float getSpeedReduction() {
        return 0.9999F;
    }

    @Override
    public float getBaseImpactDamage(Entity var1, ItemStack var2) {
//        if (!isThrowingAxe()) {
//            return 0.0f;
//        }
        float speed = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        if (var2 != null) {
            applyLOTRBowModifiers(this, var2);
        }
//        float damage = ((LOTRItemBow) var2.getItem()).getRangedDamageMultiplier(var2, shootingEntity, entity);
        return (float) (speed * damage);
//        return 0;
    }

    @Override
    public void onUpdate() {
        Block block;
//        super.onUpdate();
        if (prevRotationPitch == 0.0f && prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0 / 3.141592653589793);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, f) * 180.0 / 3.141592653589793);
        }
        block = worldObj.getBlock(xTile, yTile, zTile);
        if (block != Blocks.air) {
            block.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ))) {
                inGround = true;
            }
        }
        if (shake > 0) {
            --shake;
        }
        if (inGround) {
            Block j = worldObj.getBlock(xTile, yTile, zTile);
            int k = worldObj.getBlockMetadata(xTile, yTile, zTile);
            if (j == inTile && k == inData) {
                ++ticksInGround;
                if (ticksInGround >= maxTicksInGround()) {
                    setDead();
                }
            } else {
                inGround = false;
                motionX *= rand.nextFloat() * 0.2f;
                motionY *= rand.nextFloat() * 0.2f;
                motionZ *= rand.nextFloat() * 0.2f;
                ticksInGround = 0;
                ticksInAir = 0;
            }
        } else {
            int l;
            ++ticksInAir;
            Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition movingobjectposition = worldObj.func_147447_a(vec3d, vec3d1, false, true, false);
            vec3d = Vec3.createVectorHelper(posX, posY, posZ);
            vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }
            Entity entity = null;
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0));
            double d = 0.0;
            for (l = 0; l < list.size(); ++l) {
                float f5;
                double d1;
                MovingObjectPosition movingobjectposition1;
                Entity entity1 = (Entity) list.get(l);
                if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 5 || (movingobjectposition1 = entity1.boundingBox.expand(f5 = 0.3f, f5, f5).calculateIntercept(vec3d, vec3d1)) == null || (d1 = vec3d.distanceTo(movingobjectposition1.hitVec)) >= d && d != 0.0) {
                    continue;
                }
                entity = entity1;
                d = d1;
            }
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
            if (movingobjectposition != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;
                if (entityplayer.capabilities.disableDamage || shootingEntity instanceof EntityPlayer && !((EntityPlayer) shootingEntity).canAttackPlayer(entityplayer)) {
                    movingobjectposition = null;
                }
            }
            //NoseVersion 2023/1/5 21:16
//            if (movingobjectposition != null) {
//                Entity hitEntity = movingobjectposition.entityHit;
//                if (hitEntity != null) {
//                    if (hitEntity instanceof LOTREntityNPC) {
//                        if (shootingEntity instanceof LOTREntityNPC) {
//                            LOTREntityNPC hitlotrnpc = (LOTREntityNPC) hitEntity;
//                            LOTREntityNPC source = (LOTREntityNPC) shootingEntity;
//                            if (!hitlotrnpc.getFaction().isBadRelation(source.getFaction()) ) {
////								System.out.println("test");
//                                movingobjectposition = null;
////                                System.out.println("movingobjecttest");
//                            }
//                        }
//                    }
//                }
//            }

            if (movingobjectposition != null) {
                Entity hitEntity = movingobjectposition.entityHit;
//                System.out.println("似乎没有执行");
                if (hitEntity != null) {
//                    System.out.println("tag1");
                    if (hitEntity instanceof LOTREntityNPC) {
//                        System.out.println("tag2");
                        if (shootingEntity != null) {
                            if (shootingEntity instanceof LOTREntityNPC) {

//                                System.out.println("tag3");
                                LOTREntityNPC hitlotrnpc = (LOTREntityNPC) hitEntity;
                                LOTREntityNPC source = (LOTREntityNPC) shootingEntity;
//                                & !(hitlotrnpc.getFaction().isNeutral(source.getFaction()))
                                if (hitlotrnpc.getFaction().isGoodRelation(source.getFaction())) {
//                                if (!hitlotrnpc.getFaction().isBadRelation(source.getFaction())  ) {
//								System.out.println("test");
                                    movingobjectposition.entityHit = null;
                                    movingobjectposition = null;
                                    hitEntity = null;
                                }
                            }
                        } else {
                            movingobjectposition.entityHit = null;
                            movingobjectposition = null;

                            hitEntity = null;
                        }
//                        System.out.println("在地上吗"+shootingEntity.onGround);

                    } else { //被击中的不是lotrentitynpc  那就再判断是不是主人玩家 或击中坐骑的主人是不是友善faction
//                        FMLLog.info("1");
                        if (hitEntity instanceof EntityPlayer) {
//                            FMLLog.info("2");
                            EntityPlayer p = (EntityPlayer) hitEntity;
                            if (shootingEntity != null) {
//                                FMLLog.info("3");
                                if (shootingEntity instanceof LOTREntityNPC) {
//                                    FMLLog.info("4");
//                                    EntityPlayer hitlotrnpc = (LOTREntityNPC) hitEntity;
                                    LOTREntityNPC source = (LOTREntityNPC) shootingEntity;
                                    LOTRPlayerData lpd = LOTRLevelData.getData(p);
                                    if (lpd.getPledgeFaction() != null)
//                                    if(!source.getFaction().isBadRelation(lpd.getPledgeFaction()) & !source.getFaction().isNeutral(lpd.getPledgeFaction())){
                                        if (source.getFaction().isGoodRelation(lpd.getPledgeFaction())) {
                                            movingobjectposition.entityHit = null;
                                            movingobjectposition = null;
                                            hitEntity = null;
                                        }
//                                    if(source.hiredNPCInfo.getHiringPlayerUUID()!=null){
//                                        if(source.hiredNPCInfo.getHiringPlayerUUID().equals(hitEntity.getUniqueID())){
////                                        FMLLog.info("5");
//                                            movingobjectposition.entityHit=null;
//                                            movingobjectposition = null;
//                                            hitEntity=null;
////                                        FMLLog.info("击中的是生存玩家设为Null");
////                                        System.out.println();
//                                        }
//                                    }

                                }
                            }
                        } else if (hitEntity.riddenByEntity != null) {
//                            FMLLog.info("6");
                            Entity rider = hitEntity.riddenByEntity;
                            if (rider instanceof LOTREntityNPC) { //骑手是npc
//                                FMLLog.info("7");
                                LOTREntityNPC riderhit = (LOTREntityNPC) rider;
                                if (shootingEntity != null) {
//                                    FMLLog.info("8");
                                    if (shootingEntity instanceof LOTREntityNPC) {
//                                        FMLLog.info("9");
//
                                        LOTREntityNPC source = (LOTREntityNPC) shootingEntity;
                                        if (!riderhit.getFaction().isBadRelation(source.getFaction())) {
//                                            FMLLog.info("10");
//								System.out.println("test");
                                            movingobjectposition.entityHit = null;
                                            movingobjectposition = null;
                                            hitEntity = null;
//                                            FMLLog.info("击中的是友军Npc坐骑");
//                                            System.out.println();
                                        }
                                    }
                                }
                            } else if (rider instanceof EntityPlayer) { //骑手是玩家a
//                                FMLLog.info("11");
//                                if(hitEntity instanceof EntityPlayer ){
                                if (shootingEntity != null) {
//                                        FMLLog.info("12");
                                    if (shootingEntity instanceof LOTREntityNPC) {
//                                            FMLLog.info("13");
//                                    EntityPlayer hitlotrnpc = (LOTREntityNPC) hitEntity;
                                        EntityPlayer p = (EntityPlayer) rider;
                                        LOTRPlayerData lpd = LOTRLevelData.getData(p);
                                        LOTREntityNPC source = (LOTREntityNPC) shootingEntity;
                                        if (lpd.getPledgeFaction() != null)
                                            if (!source.getFaction().isBadRelation(lpd.getPledgeFaction())) {
//                                                    FMLLog.info("14");
                                                movingobjectposition.entityHit = null;
                                                movingobjectposition = null;
                                                hitEntity = null;
//                                                    FMLLog.info("击中的是玩家坐骑");
//                                                    System.out.println();
                                            }
                                    }
                                }
                            }

                        }
//                        else{
//
//                        }
                    }
                }

                if (hitEntity != null) {
                    ItemStack itemstack = getProjectileItem();
                    int damageInt = MathHelper.ceiling_double_int(getBaseImpactDamage(hitEntity, itemstack));
                    int fireAspect = 0;
                    if (itemstack != null) {
                        knockbackStrength = shootingEntity instanceof EntityLivingBase && hitEntity instanceof EntityLivingBase ? (knockbackStrength += EnchantmentHelper.getKnockbackModifier((EntityLivingBase) shootingEntity, (EntityLivingBase) hitEntity)) : (knockbackStrength += LOTRWeaponStats.getTotalKnockback(itemstack));
                    }
                    if (getIsCritical()) {
                        damageInt += rand.nextInt(damageInt / 2 + 2);
                    }
                    double[] prevMotion = {hitEntity.motionX, hitEntity.motionY, hitEntity.motionZ};
                    DamageSource damagesource = getDamageSource();
                    if (hitEntity.attackEntityFrom(damagesource, damageInt)) {
                        double[] newMotion = {hitEntity.motionX, hitEntity.motionY, hitEntity.motionZ};
                        float kbf = getKnockbackFactor();
                        hitEntity.motionX = prevMotion[0] + (newMotion[0] - prevMotion[0]) * kbf;
                        hitEntity.motionY = prevMotion[1] + (newMotion[1] - prevMotion[1]) * kbf;
                        hitEntity.motionZ = prevMotion[2] + (newMotion[2] - prevMotion[2]) * kbf;
                        if (isBurning()) {
                            hitEntity.setFire(5);
                        }
                        if (hitEntity instanceof EntityLivingBase) {
                            float knockback;
                            EntityLivingBase hitEntityLiving = (EntityLivingBase) hitEntity;
                            if (knockbackStrength > 0 && (knockback = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ)) > 0.0f) {
                                hitEntityLiving.addVelocity(motionX * knockbackStrength * 0.6 / knockback, 0.1, motionZ * knockbackStrength * 0.6 / knockback);
                            }
                            if (fireAspect > 0) {
                                hitEntityLiving.setFire(fireAspect * 4);
                            }
                            if (shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(hitEntityLiving, shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) shootingEntity, hitEntityLiving);
                            }
                            if (shootingEntity instanceof EntityPlayerMP && hitEntityLiving instanceof EntityPlayer) {
                                ((EntityPlayerMP) shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0f));
                            }
                        }
                        worldObj.playSoundAtEntity(this, getImpactSound(), 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f));
                        onCollideWithTarget(hitEntity);
                    } else {
                        motionX *= -0.1;
                        motionY *= -0.1;
                        motionZ *= -0.1;
                        rotationYaw += 180.0f;
                        prevRotationYaw += 180.0f;
                        ticksInAir = 0;
                    }
                } else {
                    if (movingobjectposition != null) {
                        xTile = movingobjectposition.blockX;
                        yTile = movingobjectposition.blockY;
                        zTile = movingobjectposition.blockZ;
                        inTile = worldObj.getBlock(xTile, yTile, zTile);
                        inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
                        motionX = (float) (movingobjectposition.hitVec.xCoord - posX);
                        motionY = (float) (movingobjectposition.hitVec.yCoord - posY);
                        motionZ = (float) (movingobjectposition.hitVec.zCoord - posZ);
                        float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
                        posX -= motionX / f2 * 0.05;
                        posY -= motionY / f2 * 0.05;
                        posZ -= motionZ / f2 * 0.05;
                        worldObj.playSoundAtEntity(this, getImpactSound(), 1.0f, 1.2f / (rand.nextFloat() * 0.2f + 0.9f));
                        inGround = true;
                        shake = 7;
                        setIsCritical(false);
                        if (inTile.getMaterial() != Material.air) {
                            inTile.onEntityCollidedWithBlock(worldObj, xTile, yTile, zTile, this);
                        }
                    }

                }
            }
            if (getIsCritical()) {
                for (l = 0; l < 4; ++l) {
                    worldObj.spawnParticle("crit", posX + motionX * l / 4.0, posY + motionY * l / 4.0, posZ + motionZ * l / 4.0, -motionX, -motionY + 0.2, -motionZ);
                }
            }
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            float f3 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0 / 3.141592653589793);
            rotationPitch = (float) (Math.atan2(motionY, f3) * 180.0 / 3.141592653589793);
            while (rotationPitch - prevRotationPitch < -180.0f) {
                prevRotationPitch -= 360.0f;
            }
            while (rotationPitch - prevRotationPitch >= 180.0f) {
                prevRotationPitch += 360.0f;
            }
            while (rotationYaw - prevRotationYaw < -180.0f) {
                prevRotationYaw -= 360.0f;
            }
            while (rotationYaw - prevRotationYaw >= 180.0f) {
                prevRotationYaw += 360.0f;
            }
            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f;
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f;
            float f4 = getSpeedReduction();
            if (isInWater()) {
                for (int k1 = 0; k1 < 4; ++k1) {
                    float f7 = 0.25f;
                    worldObj.spawnParticle("bubble", posX - motionX * f7, posY - motionY * f7, posZ - motionZ * f7, motionX, motionY, motionZ);
                }
                f4 = 0.8f;
            }
            motionX *= f4;
            motionY *= f4;
            motionZ *= f4;
            motionY -= 0.050000000074505806; //原来0.05
            setPosition(posX, posY, posZ);
            func_145775_I();
        }
    }

    public LOTREntityArrow(World world) {
        super(world);
    }

    @Override
    public void entityInit() {
        dataWatcher.addObject(17, (byte) 0);
        dataWatcher.addObjectByDataType(18, 5);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        xTile = nbt.getInteger("xTile");
        yTile = nbt.getInteger("yTile");
        zTile = nbt.getInteger("zTile");
        inTile = Block.getBlockById(nbt.getInteger("inTile"));
        inData = nbt.getByte("inData");
        shake = nbt.getByte("shake");
        inGround = nbt.getByte("inGround") == 1;
        canBePickedUp = nbt.getByte("pickup");
        knockbackStrength = nbt.getByte("Knockback");
        if (nbt.hasKey("itemID")) {
            ItemStack item = new ItemStack(Item.getItemById(nbt.getInteger("itemID")), 1, nbt.getInteger("itemDamage"));
            if (nbt.hasKey("ItemTagCompound")) {
                item.setTagCompound(nbt.getCompoundTag("ItemTagCompound"));
            }
            setProjectileItem(item);
        } else {
            setProjectileItem(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("ProjectileItem")));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setInteger("xTile", xTile);
        nbt.setInteger("yTile", yTile);
        nbt.setInteger("zTile", zTile);
        nbt.setInteger("inTile", Block.getIdFromBlock(inTile));
        nbt.setByte("inData", (byte) inData);
        nbt.setByte("shake", (byte) shake);
        nbt.setByte("inGround", (byte) (inGround ? 1 : 0));
        nbt.setByte("pickup", (byte) canBePickedUp);
        nbt.setByte("Knockback", (byte) knockbackStrength);
        if (getProjectileItem() != null) {
            nbt.setTag("ProjectileItem", getProjectileItem().writeToNBT(new NBTTagCompound()));
        }
    }

    public LOTREntityArrow(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy) {
//        super(world, entityliving, target, item, charge, inaccuracy);
        super(world);
        setProjectileItem(item);
        shootingEntity = entityliving;
        if (entityliving instanceof EntityPlayer) {
            canBePickedUp = 1;
        }
        setSize(0.5f, 0.5f);
        posY = entityliving.posY + entityliving.getEyeHeight() - 0.1;
        double d = target.posX - entityliving.posX;
        double d1 = target.posY + target.getEyeHeight() - 0.7 - posY;
        double d2 = target.posZ - entityliving.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        if (d3 >= 1.0E-7) {
            float f = (float) (Math.atan2(d2, d) * 180.0 / 3.141592653589793) - 90.0f;
            float f1 = (float) -(Math.atan2(d1, d3) * 180.0 / 3.141592653589793);
            double d4 = d / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(entityliving.posX + d4, posY, entityliving.posZ + d5, f, f1);
            yOffset = 0.0f;
            float d6 = (float) d3 * 0.2f;
            setThrowableHeading(d, d1 + d6, d2, charge * 1.5f, inaccuracy); //这里charge *1.5 去掉了这个a倍数a
        }
    }

    public LOTREntityArrow(World world, EntityLivingBase entityliving, EntityLivingBase target, float charge, float inaccuracy) {
        super(world);
        setProjectileItem(new ItemStack(Items.arrow));
        shootingEntity = entityliving;
        if (entityliving instanceof EntityPlayer) {
            canBePickedUp = 1;
        }
        setSize(0.5f, 0.5f);
        posY = entityliving.posY + entityliving.getEyeHeight() - 0.1;
        double d = target.posX - entityliving.posX;
        double d1 = target.posY + target.getEyeHeight() - 0.7 - posY;
        double d2 = target.posZ - entityliving.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        if (d3 >= 1.0E-7) {
            float f = (float) (Math.atan2(d2, d) * 180.0 / 3.141592653589793) - 90.0f;
            float f1 = (float) -(Math.atan2(d1, d3) * 180.0 / 3.141592653589793);
            double d4 = d / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(entityliving.posX + d4, posY, entityliving.posZ + d5, f, f1);
            yOffset = 0.0f;
            float d6 = (float) d3 * 0.2f;
            setThrowableHeading(d, d1 + d6, d2, charge * 1.5f, inaccuracy);//这里charge *1.5 去掉了这个a倍数a
        }
    }

    public LOTREntityArrow(EntityLivingBase entityliving, EntityLivingBase target, float charge, float inaccuracy) {
        super(entityliving.worldObj);
        setProjectileItem(new ItemStack(Items.arrow));
        shootingEntity = entityliving;
        if (entityliving instanceof EntityPlayer) {
            canBePickedUp = 1;
        }
        setSize(0.5f, 0.5f);
        posY = entityliving.posY + entityliving.getEyeHeight() - 0.1;
        double d = target.posX - entityliving.posX;
        double d1 = target.posY + target.getEyeHeight() - 0.7 - posY;
        double d2 = target.posZ - entityliving.posZ;
        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        if (d3 >= 1.0E-7) {
            float f = (float) (Math.atan2(d2, d) * 180.0 / 3.141592653589793) - 90.0f;
            float f1 = (float) -(Math.atan2(d1, d3) * 180.0 / 3.141592653589793);
            double d4 = d / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(entityliving.posX + d4, posY, entityliving.posZ + d5, f, f1);
            yOffset = 0.0f;
            float d6 = (float) d3 * 0.2f;
            setThrowableHeading(d, d1 + d6, d2, charge * 1.5f, inaccuracy);
        }
    }

    public LOTREntityArrow(World world, EntityLivingBase entityliving, ItemStack item, float charge) {
        super(world, entityliving, item, charge);
    }

    public LOTREntityArrow(World world, ItemStack item, double d, double d1, double d2) {
        super(world, item, d, d1, d2);
    }

    public static void applyLOTRBowModifiers(LOTREntityArrow arrow, ItemStack itemstack) {
        int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemstack);
        if (power > 0) {
            arrow.setDamage(arrow.getDamage() + power * 0.5 + 0.5);
        }
        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemstack);
        punch += LOTREnchantmentHelper.calcRangedKnockback(itemstack);
        if (punch > 0) {
            arrow.setKnockbackStrength(punch);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemstack) + LOTREnchantmentHelper.calcFireAspect(itemstack) > 0) {
            arrow.setFire(100);
        }
        // 获取 LOTREnchantment 类对象
        Class<?> enchantmentClass = null;
        try {
            enchantmentClass = Class.forName("lotr.common.enchant.LOTREnchantment");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 获取 allEnchantments 字段
        Field allEnchantmentsField = null;
        try {
            allEnchantmentsField = enchantmentClass.getDeclaredField("allEnchantments");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 获取字段的值（假设是一个 Collection 或 Iterable）
        List<LOTREnchantment> allEnchantments;
        try {
            allEnchantments = (List<LOTREnchantment>) allEnchantmentsField.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (LOTREnchantment ench : allEnchantments) {
            if (!ench.applyToProjectile() || !LOTREnchantmentHelper.hasEnchant(itemstack, ench)) {
                continue;
            }
            LOTREnchantmentHelper.setProjectileEnchantment(arrow, ench);
        }
    }

    @Override
    public void setThrowableHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += rand.nextGaussian() * 0.0075 * f1;
        d1 += rand.nextGaussian() * 0.0075 * f1;
        d2 += rand.nextGaussian() * 0.0075 * f1;
        motionX = d *= f;
        motionY = d1 *= f * 0.6; //这里对y乘了个float0.6
        motionZ = d2 *= f;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        prevRotationYaw = rotationYaw = (float) (Math.atan2(d, d2) * 180.0 / 3.141592653589793);
        prevRotationPitch = rotationPitch = (float) (Math.atan2(d1, f3) * 180.0 / 3.141592653589793);
        ticksInGround = 0;
    }

    @Override
    public int maxTicksInGround() {
//		return canBePickedUp == 1 ? 6000 : 1200;
        return canBePickedUp == 1 ? 6000 : 120;
    }


    @Override
    public void onKillEntity(EntityLivingBase entity) {
        super.onKillEntity(entity);
        if (this.shootingEntity != null) {
            if (this.shootingEntity instanceof LOTREntityNPC) {
                LOTREntityNPC shooter = (LOTREntityNPC) this.shootingEntity;
                if (shooter.hiredNPCInfo != null)
                    shooter.hiredNPCInfo.onKillEntity(entity);
//            if (shooter.lootsExtraCoins() && !worldObj.isRemote && entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).canDropRares() && rand.nextInt(2) == 0) {
//                int coins = shooter.getRandomCoinDropAmount();
//                coins = (int) (coins * MathHelper.randomFloatClamp(rand, 1.0f, 3.0f));
//                if (coins > 0) {
//                    entity.dropItem(LOTRMod.silverCoin, coins);
//                }
//            }
                LOTREntityInvasionSpawner invasion;
                if (entity instanceof LOTREntityNPC) {
                    LOTREntityNPC target = (LOTREntityNPC) entity;
                    if (shooter.hiredNPCInfo.getHiringPlayer() != null) {
                        EntityPlayer entityplayer = shooter.hiredNPCInfo.getHiringPlayer();
                        if (!worldObj.isRemote && target.isInvasionSpawned() && (invasion = LOTREntityInvasionSpawner.locateInvasionNearby(this, target.getInvasionID())) != null) {
                            invasion.addPlayerKill(entityplayer);
//                            if (damagesource.getEntity() == entityplayer) {
//                                invasion.setWatchingInvasion((EntityPlayerMP) entityplayer, true);
//                            }
                        }
                    }


                }

            }


        }
//        hiredNPCInfo.onKillEntity(entity);
//        if (lootsExtraCoins() && !worldObj.isRemote && entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).canDropRares() && rand.nextInt(2) == 0) {
//            int coins = getRandomCoinDropAmount();
//            coins = (int) (coins * MathHelper.randomFloatClamp(rand, 1.0f, 3.0f));
//            if (coins > 0) {
//                entity.dropItem(LOTRMod.silverCoin, coins);
//            }
//        }
    }


}

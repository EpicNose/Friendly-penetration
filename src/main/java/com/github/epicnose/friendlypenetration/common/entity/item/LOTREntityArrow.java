package com.github.epicnose.friendlypenetration.common.entity.item;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import lotr.common.enchant.LOTREnchantment;
import lotr.common.enchant.LOTREnchantmentHelper;
import lotr.common.entity.projectile.LOTREntityProjectileBase;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityArrow extends LOTREntityProjectileBase  {

//    public LOTREntityArrow(World p_i1753_1_) {
//        super(p_i1753_1_);
//    }
    public int xTile = -1;
    public int yTile = -1;
    public int zTile = -1;
    public Block inTile;

    public int inData = 0;
    private int knockbackStrength;
    private double damage = 2.0D;
    public void setDamage(double p_70239_1_)
    {
        this.damage = p_70239_1_;
    }
    public double getDamage()
    {
        return this.damage;
    }
    public void setKnockbackStrength(int p_70240_1_)
    {
        this.knockbackStrength = p_70240_1_;
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
        for (LOTREnchantment ench : LOTREnchantment.allEnchantments) {
            if (!ench.applyToProjectile() || !LOTREnchantmentHelper.hasEnchant(itemstack, ench)) {
                continue;
            }
            LOTREnchantmentHelper.setProjectileEnchantment(arrow, ench);
        }
    }
    public LOTREntityArrow(World world) {
        super(world);
    }

    public ItemStack getProjectileItem() {
//        return dataWatcher.getWatchableObjectItemStack(18);
        return new ItemStack(Items.arrow);
    }
    public void setProjectileItem(ItemStack item) {
        dataWatcher.updateObject(18, new ItemStack(Items.arrow));
    }
    public void setIsCritical(boolean flag) {
        dataWatcher.updateObject(17, (byte) (flag ? 1 : 0));
    }
    @Override
    public void entityInit() {
        dataWatcher.addObject(17, (byte) 0);
        dataWatcher.addObjectByDataType(18, 5);
    }

//   @Override
//    public void entityInit() {
//        dataWatcher.addObject(17, (byte) 0);
//        dataWatcher.addObjectByDataType(18, 5);
//    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
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


    public LOTREntityArrow(World world, EntityLivingBase entityliving, EntityLivingBase target,ItemStack itemStack, float charge, float inaccuracy) {
//        super(world, entityliving, target, itemStack, charge, inaccuracy);
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
            setThrowableHeading(d, d1 + d6, d2, charge * 1.5f, inaccuracy);
        }
    }
//    public LOTREntityArrow(World world, EntityLivingBase entityliving, EntityLivingBase target,ItemStack itemStack, float charge, float inaccuracy) {
//        super(world, entityliving, target, itemStack, charge, inaccuracy);
//    }

    public LOTREntityArrow(World world, EntityLivingBase entityliving, ItemStack item, float charge) {
        super(world, entityliving, item, charge);
    }

    public LOTREntityArrow(World world, ItemStack item, double d, double d1, double d2) {
        super(world, item, d, d1, d2);
    }


    @Override
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {

    }
}

package com.github.epicnose.friendlypenetration.common.entity.item;

import lotr.common.enchant.LOTREnchantment;
import lotr.common.enchant.LOTREnchantmentHelper;
import lotr.common.entity.projectile.LOTREntityProjectileBase;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityArrow extends LOTREntityProjectileBase  {

//    public LOTREntityArrow(World p_i1753_1_) {
//        super(p_i1753_1_);
//    }
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

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

    }

    public LOTREntityArrow(World world, EntityLivingBase entityliving, EntityLivingBase target, ItemStack item, float charge, float inaccuracy) {
        super(world, entityliving, target, item, charge, inaccuracy);
    }

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

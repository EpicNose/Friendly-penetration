package com.github.epicnose.friendlypenetration.core.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.RegistryDelegate;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GenericModHooks {
    public static void setItemDelagateName(Item item, String name) {
        RegistryDelegate.Delegate<Item> delegate = (RegistryDelegate.Delegate<Item>) item.delegate;
        ReflectionHelper.setPrivateValue(RegistryDelegate.Delegate.class, delegate, name, "name");
    }

    public static void setBlockDelagateName(Block block, String name) {
        RegistryDelegate.Delegate<Block> delegate = (RegistryDelegate.Delegate<Block>) block.delegate;
        ReflectionHelper.setPrivateValue(RegistryDelegate.Delegate.class, delegate, name, "name");
    }

    public static void removeBlockFromOreDictionary(Block block) {
        removeItemFromOreDictionary(Item.getItemFromBlock(block));
    }

    public static void removeItemFromOreDictionary(Item item) {
        if(item == null) return;
        
        ItemStack stack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE); //Only doing worldcard which should grab all the possible values
        int[] oreIDs = OreDictionary.getOreIDs(stack);
        
        List<ArrayList<ItemStack>> oreIdToStacks = ReflectionHelper.getPrivateValue(OreDictionary.class, null, "idToStack");
        for(int oreID : oreIDs) {
            ArrayList<ItemStack> oreStacks = oreIdToStacks.get(oreID);
            if(oreStacks == null) continue;
            
            oreStacks.removeIf(oreStack -> oreStack.getItem() == stack.getItem());
        }
        
        String registryName = stack.getItem().delegate.name();
        if(registryName == null) return;
        
        int stackId = GameData.getItemRegistry().getId(registryName);
        Map<Integer, List<Integer>> stackIdToOreId = ReflectionHelper.getPrivateValue(OreDictionary.class, null, "stackToId");
        stackIdToOreId.remove(stackId);
    }
}

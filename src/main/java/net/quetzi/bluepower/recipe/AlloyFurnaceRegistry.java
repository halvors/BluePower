/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRecipe;
import net.quetzi.bluepower.api.recipe.IAlloyFurnaceRegistry;
import net.quetzi.bluepower.util.ItemStackUtils;

public class AlloyFurnaceRegistry implements IAlloyFurnaceRegistry {
    
    private static AlloyFurnaceRegistry     INSTANCE            = new AlloyFurnaceRegistry();
    
    private final List<IAlloyFurnaceRecipe> alloyFurnaceRecipes = new ArrayList<IAlloyFurnaceRecipe>();
    
    private AlloyFurnaceRegistry() {
    
    }
    
    public static AlloyFurnaceRegistry getInstance() {
    
        return INSTANCE;
    }
    
    @Override
    public void addRecipe(IAlloyFurnaceRecipe recipe) {
    
        alloyFurnaceRecipes.add(recipe);
    }
    
    /**
     * getter for NEI plugin
     * @return
     */
    public List<IAlloyFurnaceRecipe> getAllRecipes() {
    
        return alloyFurnaceRecipes;
    }
    
    @Override
    public void addRecipe(ItemStack craftingResult, Object... requiredItems) {
    
        ItemStack[] requiredStacks = new ItemStack[requiredItems.length];
        for (int i = 0; i < requiredStacks.length; i++) {
            if (requiredItems[i] instanceof ItemStack) {
                requiredStacks[i] = (ItemStack) requiredItems[i];
            } else if (requiredItems[i] instanceof Item) {
                requiredStacks[i] = new ItemStack((Item) requiredItems[i], 1, OreDictionary.WILDCARD_VALUE);
            } else if (requiredItems[i] instanceof Block) {
                requiredStacks[i] = new ItemStack((Item) requiredItems[i], 1, OreDictionary.WILDCARD_VALUE);
            } else {
                throw new IllegalArgumentException("Alloy Furnace crafting ingredients can only be ItemStack, Item or Block!");
            }
        }
        addRecipe(new StandardAlloyFurnaceRecipe(craftingResult, requiredStacks));
    }
    
    public IAlloyFurnaceRecipe getMatchingRecipe(ItemStack[] input, ItemStack outputSlot) {
    
        for (IAlloyFurnaceRecipe recipe : alloyFurnaceRecipes) {
            if (recipe.matches(input)) {
                if (outputSlot != null) {
                    if (outputSlot != null) {// check if we can add the crafting result to the output slot
                        ItemStack craftingResult = recipe.getCraftingResult(input);
                        if (!ItemStack.areItemStackTagsEqual(outputSlot, craftingResult) || !outputSlot.isItemEqual(craftingResult)) {
                            continue;
                        } else if (craftingResult.stackSize + outputSlot.stackSize > outputSlot.getMaxStackSize()) {
                            continue;
                        }
                    }
                }
                return recipe;
            }
        }
        return null;
    }
    
    public class StandardAlloyFurnaceRecipe implements IAlloyFurnaceRecipe {
        
        private final ItemStack   craftingResult;
        private final ItemStack[] requiredItems;
        
        private StandardAlloyFurnaceRecipe(ItemStack craftingResult, ItemStack... requiredItems) {
        
            if (craftingResult == null) throw new IllegalArgumentException("Alloy Furnace crafting result can't be null!");
            if (requiredItems.length > 9) throw new IllegalArgumentException("There can't be more than 9 crafting ingredients for the Alloy Furnace!");
            for (ItemStack requiredItem : requiredItems) {
                if (requiredItem == null) throw new NullPointerException("An Alloy Furnace crafting ingredient can't be null!");
            }
            for (ItemStack stack : requiredItems) {
                for (ItemStack stack2 : requiredItems) {
                    if (stack != stack2 && ItemStackUtils.isItemFuzzyEqual(stack, stack2)) throw new IllegalArgumentException("No equivalent Alloy Furnace crafting ingredient can be given twice! This does take OreDict + wildcard values in account.");
                }
            }
            
            this.craftingResult = craftingResult;
            this.requiredItems = requiredItems;
        }
        
        @Override
        public boolean matches(ItemStack[] input) {
        
            for (ItemStack requiredItem : requiredItems) {
                int itemsNeeded = requiredItem.stackSize;
                for (ItemStack inputStack : input) {
                    if (inputStack != null && ItemStackUtils.isItemFuzzyEqual(inputStack, requiredItem)) {
                        itemsNeeded -= inputStack.stackSize;
                        if (itemsNeeded <= 0) break;
                    }
                }
                if (itemsNeeded > 0) return false;
            }
            return true;
        }
        
        @Override
        public void useItems(ItemStack[] input) {
        
            for (ItemStack requiredItem : requiredItems) {
                int itemsNeeded = requiredItem.stackSize;
                for (int i = 0; i < input.length; i++) {
                    ItemStack inputStack = input[i];
                    if (inputStack != null && ItemStackUtils.isItemFuzzyEqual(inputStack, requiredItem)) {
                        int itemsSubstracted = Math.min(inputStack.stackSize, itemsNeeded);
                        inputStack.stackSize -= itemsSubstracted;
                        if (inputStack.stackSize <= 0) input[i] = null;
                        itemsNeeded -= itemsSubstracted;
                        if (itemsNeeded <= 0) break;
                    }
                }
                if (itemsNeeded > 0) throw new IllegalArgumentException("Alloy Furnace recipe using items, after using still items required?? This is a bug!");
            }
        }
        
        @Override
        public ItemStack getCraftingResult(ItemStack[] input) {
        
            return craftingResult;
        }
        
        /**
         * getter for NEI plugin
         * @return
         */
        public ItemStack[] getRequiredItems() {
        
            return requiredItems;
        }
        
    }
}
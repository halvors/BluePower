package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class BPBlockColor implements IBlockColor, IItemColor {

    @Override
    @OnlyIn(Dist.CLIENT)
    public int colorMultiplier(BlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
        //Color for Block
        return ((IBPColoredBlock)state.getBlock()).getColor(world, pos, tintIndex);
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return ((IBPColoredBlock)Block.getBlockFromItem(stack.getItem())).getColor(tintIndex);
    }
}

/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.tile.BPTileEntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


/**
 * @author Koen Beckers (K4Unl) and Amadornes.
 */
public class TileLampRGB extends TileLamp {

    private byte[] bundledPower = new byte[16];

    @Override
    public CompoundNBT write(CompoundNBT tCompound) {
        if (getBlockState().getBlock() instanceof BlockLampRGB) {
            tCompound.putByte("red", bundledPower[MinecraftColor.RED.ordinal()]);
            tCompound.putByte("green", bundledPower[MinecraftColor.GREEN.ordinal()]);
            tCompound.putByte("blue", bundledPower[MinecraftColor.BLUE.ordinal()]);
        }
        return tCompound;
    }

    @Override
    public void read(CompoundNBT tCompound) {
        if (tCompound.contains("red")) {
            byte[] pow = bundledPower;
            pow[MinecraftColor.RED.ordinal()] = tCompound.getByte("red");
            pow[MinecraftColor.GREEN.ordinal()] = tCompound.getByte("green");
            pow[MinecraftColor.BLUE.ordinal()] = tCompound.getByte("blue");
            bundledPower = pow;
        }
    }

    public int getColor() {

        int r = MathHelper.map(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, 0, 255, 20, 235);
        int g = MathHelper.map(bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF, 0, 255, 20, 235);
        int b = MathHelper.map(bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF, 0, 255, 20, 235);

        return (r << 16) + (g << 8) + b;
    }

}

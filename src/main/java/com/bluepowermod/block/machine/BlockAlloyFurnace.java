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

package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockAlloyFurnace extends BlockContainerHorizontalFacingBase {

    public BlockAlloyFurnace() {
        super(Material.ROCK, TileAlloyFurnace.class);
        setRegistryName(Refs.MODID, Refs.ALLOYFURNACE_NAME);
    }

    @Override
    public void tick(BlockState stateIn, ServerWorld world, BlockPos pos, Random rnd) {
        if (stateIn.get(ACTIVE)) {
            int l = stateIn.get(FACING).ordinal();
            float f = pos.getX() + 0.5F;
            float f1 = pos.getY() + 0.0F + rnd.nextFloat() * 6.0F / 16.0F;
            float f2 = pos.getZ() + 0.5F;
            float f3 = 0.52F;
            float f4 = rnd.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.addParticle(ParticleTypes.SMOKE, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.addParticle(ParticleTypes.SMOKE, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.addParticle(ParticleTypes.SMOKE, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.addParticle(ParticleTypes.SMOKE, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(ACTIVE) ? 13 : 0;
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }
}

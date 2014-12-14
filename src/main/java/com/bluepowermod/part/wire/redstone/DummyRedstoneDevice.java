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

package com.bluepowermod.part.wire.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class DummyRedstoneDevice implements IRedstoneDevice {

    private static final List<DummyRedstoneDevice> dummyDevices = new ArrayList<DummyRedstoneDevice>();

    public static DummyRedstoneDevice getDeviceAt(Vec3i loc) {

        if (dummyDevices.contains(loc))
            return dummyDevices.get(dummyDevices.indexOf(loc));

        DummyRedstoneDevice dev = new DummyRedstoneDevice(loc);
        dummyDevices.add(dev);
        return dev;
    }

    private IRedstoneDevice[] devices = new IRedstoneDevice[6];
    private Vec3i loc;

    private DummyRedstoneDevice(Vec3i loc) {

        this.loc = loc;
    }

    @Override
    public World getWorld() {

        return loc.getWorld();
    }

    @Override
    public int getX() {

        return loc.getX();
    }

    @Override
    public int getY() {

        return loc.getY();
    }

    @Override
    public int getZ() {

        return loc.getZ();
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        return RedstoneHelper.canConnect(getWorld(), getX(), getY(), getZ(), side, device instanceof IFace ? ((IFace) device).getFace()
                : ForgeDirection.UNKNOWN);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return false;// RedstoneHelper.canConnect(getWorld(), getX(), getY(), getZ(), side);
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        devices[side.ordinal()] = device;
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN)
            return (byte) 0;

        return (byte) MathHelper.map(RedstoneHelper.getOutput(getWorld(), getX(), getY(), getZ(), side), 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return true;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == loc || obj == this)
            return true;

        return false;
    }

}

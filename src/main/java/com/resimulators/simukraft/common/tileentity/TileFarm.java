package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.network.GetSimIdPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileFarm extends TileEntity implements ITickable {
    public boolean building;
    protected int width = 5;
    protected int length = 5;
    public String profession = "Farmer";
    Boolean hired = false;
    public void CreateFarm() {}

    public void update() {
    if (!building) {
        CreateFarm();
        building = true;
        }

    }

    public void setHired(boolean hired){
        this.hired = hired;
    }

    public boolean getHired()
    {
        return this.hired;
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        setHired(nbt.getBoolean("hired"));
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("hired",getHired());
        return super.writeToNBT(nbt);
    }

    public void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn){
        if (getHired())
        {
            playerIn.openGui(SimUKraft.instance, GuiHandler.GUI_FARM, worldIn, pos.getX(), pos.getY(), pos.getZ());
        } else
            {
                PacketHandler.INSTANCE.sendToServer(new GetSimIdPacket());
                playerIn.openGui(SimUKraft.instance, GuiHandler.GUI_HIRED, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
    }
}

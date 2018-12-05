package com.resimulators.simukraft.common;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.ISim;
import com.resimulators.simukraft.common.tileentity.TileSheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FactionData implements INBTSerializable<NBTTagCompound> {



    long factionId;
    List<EntityPlayer> players = new ArrayList<>();
    List<EntitySim> unemployedsims = new ArrayList<>();
    List<EntitySim> totalsims = new ArrayList<>();
    List<TileEntity> jobblocks = new ArrayList<>();



    public FactionData(long id){
        this.factionId = id;
    }
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList playersList = new NBTTagList();
        for (EntityPlayer player:players){
            NBTTagCompound playercompound = new NBTTagCompound();
            playercompound.setUniqueId("playerid",player.getUniqueID());
            playersList.appendTag(playercompound);
        }
        compound.setTag("players",playersList);
        NBTTagList unemployedSimsList = new NBTTagList();
        for (EntitySim sim:unemployedsims){
            NBTTagCompound simcompound = new NBTTagCompound();
            simcompound.setUniqueId("sim",sim.getUniqueID());
            unemployedSimsList.appendTag(simcompound);
        }
        compound.setTag("unemployedsims",unemployedSimsList);

        NBTTagList totalsimslist = new NBTTagList();
        for (EntitySim sim:totalsims){
            NBTTagCompound simcompound = new NBTTagCompound();
            simcompound.setUniqueId("sim",sim.getUniqueID());
            totalsimslist.appendTag(simcompound);
        }
        compound.setTag("totalsims",totalsimslist);

        NBTTagList blocks = new NBTTagList();
        for (TileEntity block: jobblocks){
            NBTTagCompound blockcompound = new NBTTagCompound();
            blockcompound.setLong("blockpos",block.getPos().toLong());
            blocks.appendTag(blockcompound);
        }
        compound.setTag("jobblocks",blocks);
        return compound;
    }


    @Override
    public void deserializeNBT(NBTTagCompound compound) {

    }
}

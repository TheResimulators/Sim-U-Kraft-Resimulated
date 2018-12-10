package com.resimulators.simukraft.common;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FactionData implements INBTSerializable<NBTTagCompound> {


    private long factionId;
    private String factionname;
    private List<UUID> players = new ArrayList<>();
    private List<UUID> unemployedsims = new ArrayList<>();
    private List<UUID> totalSims = new ArrayList<>();
    private List<TileEntity> jobblocks = new ArrayList<>();
    private int mode;


    public FactionData(long id, String factionname,int mode) {
        this.mode = mode;
        this.factionname = factionname;
        this.factionId = id;
    }

    public FactionData(NBTTagCompound compound){
        deserializeNBT(compound);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList playersList = new NBTTagList();
        for (UUID player : players) {
            NBTTagCompound playercompound = new NBTTagCompound();
            playercompound.setUniqueId("playerid", player);
            playersList.appendTag(playercompound);
        }
        compound.setTag("players", playersList);
        NBTTagList unemployedSimsList = new NBTTagList();
        for (UUID sim : unemployedsims) {
            NBTTagCompound simcompound = new NBTTagCompound();
            simcompound.setUniqueId("sim", sim);
            unemployedSimsList.appendTag(simcompound);
        }
        compound.setTag("unemployedsims", unemployedSimsList);

        NBTTagList totalsimslist = new NBTTagList();
        for (UUID sim : totalSims) {
            NBTTagCompound simcompound = new NBTTagCompound();
            simcompound.setUniqueId("sim", sim);
            totalsimslist.appendTag(simcompound);
        }
        compound.setTag("totalsims", totalsimslist);

        NBTTagList blocks = new NBTTagList();
        for (TileEntity block : jobblocks) {
            NBTTagCompound blockcompound = new NBTTagCompound();
            blockcompound.setLong("blockpos", block.getPos().toLong());
            blocks.appendTag(blockcompound);
        }
        compound.setTag("jobblocks", blocks);
        return compound;
    }


    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        NBTTagList playersList = compound.getTagList("players", Constants.NBT.TAG_COMPOUND);
        NBTTagList unemployedSims = compound.getTagList("unemployedsims", Constants.NBT.TAG_COMPOUND);
        NBTTagList totalsims = compound.getTagList("totalsims", Constants.NBT.TAG_COMPOUND);
        NBTTagList jobblock = compound.getTagList("jobblocks", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < playersList.tagCount(); i++) {
            NBTTagCompound player = playersList.getCompoundTagAt(i);
            players.add(player.getUniqueId("playerid"));
        }
        for (int i = 0; i < unemployedSims.tagCount(); i++) {
            NBTTagCompound unemployed = unemployedSims.getCompoundTagAt(i);
            unemployedsims.add(unemployed.getUniqueId("sim"));
        }
        for (int i = 0; i < totalsims.tagCount(); i++) {
            NBTTagCompound total = totalsims.getCompoundTagAt(i);
            totalSims.add(total.getUniqueId("sim"));
        }

        for (int i = 0; i < jobblock.tagCount(); i++) {
            NBTTagCompound block = jobblock.getCompoundTagAt(i);
            jobblocks.add(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getTileEntity(BlockPos.fromLong(block.getLong("blockpos"))));
        }
    }


    public long getFactionId() {
        return factionId;
    }


    public void addPlayer(UUID player) {
        if (player != null) {
            players.add(player);
        }
    }

    public void removePlayer(EntityPlayer player) {
        if (players.contains(player.getUniqueID())) {
            players.remove(player.getUniqueID());
        }
    }

    public List<UUID> getPlayers() {
        return players;
    }


    public void addUnemployedSim(UUID sim) {
        if (sim != null) {
            unemployedsims.add(sim);
        }
    }

    public void removeUnemplyedSim(EntitySim sim) {
        if (unemployedsims.contains(sim)) {
            unemployedsims.remove(sim);
        } else {
            SimUKraft.getLogger().debug("Removing sim that does not exist in faction: " + factionname + ", report to mod Author");
        }
    }

    public List<UUID> getUnemployedSims() {
        return unemployedsims;
    }


    public void addTotalSim(UUID sim) {
        if (sim != null) {
            totalSims.add(sim);
        }

    }

    public void removeTotalSim(EntitySim sim) {
        if (totalSims.contains(sim)) {
            totalSims.remove(sim);
        } else {
            SimUKraft.getLogger().debug("Removing sim that does not exist in faction: " + factionname + ", report to mod Author");
        }
    }

    public List<UUID> getTotalSims() {
        return totalSims;
    }


    public List<TileEntity> getJobblocks(){
        return jobblocks;
    }


    public void addJobBlock(TileEntity entity){
        if (entity != null){
        this.jobblocks.add(entity);
        }
    }
    public void removeJobblock(TileEntity entity){
        if (jobblocks.contains(entity)){
            jobblocks.remove(entity);
        }
    }


    public int getMode(){
        return mode;
    }
    public void setMode(int mode){
        this.mode = mode;
    }


    public void sendFactionPacket(IMessage message){
        List<UUID> uuids = getPlayers();
        for (UUID id:uuids){
            EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getPlayerEntityByUUID(id);
            if (player != null){
                player.getCapability(ModCapabilities.getPlayerCap(),null).updateClientWithPacket(message,player);
            }
        }
    }

}

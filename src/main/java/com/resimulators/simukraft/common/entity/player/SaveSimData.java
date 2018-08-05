package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import scala.collection.immutable.Stream;

import java.util.*;

public class SaveSimData extends WorldSavedData {
    private static final String DATA_NAME = Reference.MOD_ID + "_SimData";
    private Map<Long,Set<UUID>> Total_sims = new HashMap<>();
    private Map<Long,Set<UUID>> Unemployed_sims = new HashMap<>();
    private Map<UUID, Long> faction = new HashMap<>();
    private Map<Long,Set<UUID>> revfaction = new HashMap<>();
    private Set<TileEntity> job_tiles = new HashSet<>();
    private Map<UUID,Integer> mode = new HashMap<>();
    private Map<UUID,Boolean> enables = new HashMap<>();

    public SaveSimData() {
        super(DATA_NAME);
    }

    public SaveSimData(String s) {
        super(s);
    }


    public void  addPlayertoFaction(UUID id,long longs){
        faction.put(id,longs);
        Set<UUID> Set = revfaction.get(longs);
        if (Set == null)
        {
            Set = new HashSet<>();
        }
        Set.add(id);
        revfaction.put(longs,Set);
    }

    public void playerChangeFaction(long newlong,UUID id){
        long oldlong = faction.get(id);
        faction.remove(id,oldlong);
        Set<UUID> Set = revfaction.get(oldlong);
        Set.remove(id);
        Set = revfaction.get(newlong);
        if (Set == null)
        {
            Set = new HashSet<>();
        }
        Set.add(id);
        revfaction.put(newlong,Set);

    }
    public long getPlayerFaction(UUID uuid){
            if (faction.get(uuid) == null){
                Random rnd = new Random();
                addPlayertoFaction(uuid,rnd.nextLong());
            }
            return faction.get(uuid);
    }

    public Set<UUID> getTotalSims(long longid)
    {
        if (Total_sims.get(longid) != null){
        return Total_sims.get(longid);
        }else{return new HashSet<>();}
    }

    public Set<UUID> getUnemployedSims(long longid)
    {
        if (Unemployed_sims.get(longid) != null){
            return Unemployed_sims.get(longid);
        }else{return new HashSet<>();}
    }

    public void addtotalSim(UUID uuid,long longid){
        Set<UUID> totalSims = getTotalSims(longid);
        if (totalSims == null){
            totalSims = new HashSet<>();
        }
        totalSims.add(uuid);
        Total_sims.put(longid,totalSims);
        markDirty();
    }

    public void addUnemployedsim(UUID uuid,long longid){
        Set<UUID> unemployedsims = Unemployed_sims.get(longid);
        System.out.println("unemployed sims " + unemployedsims);
        System.out.println("uuid " + uuid);
        if (unemployedsims == null){
            unemployedsims = new HashSet<>();
        }
        unemployedsims.add(uuid);
        Unemployed_sims.put(longid,unemployedsims);
        markDirty();
    }

    public void removeTotalSim(UUID uuid,long longid){
        System.out.println("this has been called");
        Set<UUID> totalSims = getTotalSims(longid);
        totalSims.remove(uuid);
        Total_sims.put(longid,totalSims);
        markDirty();
    }

    public void removeUnemployedSim(UUID uuid,long longid){
        System.out.println("long id that was sent as parameter to remove unemployed sim " + longid);
        Set<UUID> unemployedsims = Unemployed_sims.get(longid);
        unemployedsims.remove(uuid);
        Unemployed_sims.put(longid,unemployedsims);
        markDirty();
    }



    public Set<UUID> getFactionPlayers(long longid){
        if (revfaction.get(longid) != null){
        return revfaction.get(longid);}

        for (UUID uuid: faction.keySet()){
            Set<UUID> set = revfaction.get(faction.get(uuid));
            if (set == null){
                set = new HashSet<>();
            }
            set.add(uuid);
            revfaction.put(faction.get(uuid),set);
        }
        return revfaction.get(longid);
    }

    public void SendFactionPacket(IMessage message, long longid)
    {
        if (FMLCommonHandler.instance().getSide() == Side.SERVER){
        Set<UUID> players = getFactionPlayers(longid);
        if (players != null){
            System.out.println(players);
        for (UUID id:players){
            if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(id) != null){
            EntityPlayerMP playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(id);
            System.out.println("sending packet to player " + playerMP);
            System.out.println("message being sent");
            if (playerMP != null){
            PacketHandler.INSTANCE.sendTo(message,playerMP);
                        }
                    }
                }
            }
        }
    }

      public void addTile(TileEntity tile) {
        job_tiles.add(tile);
        markDirty();
    }

    public boolean isEnabled(UUID id) {
        return enables.getOrDefault(id, false);
    }

    public void setEnabled(UUID id,boolean enable) {
        enables.put(id,enable);
        markDirty();
    }


    public Set<TileEntity> getJob_tiles() {
        return job_tiles;
    }

    public void removeTile(TileEntity tile) {
        if (job_tiles.contains(tile)) {
            job_tiles.remove(tile);
            markDirty();
        }
    }

    public Integer isMode(UUID id)
    {
        return mode.get(id);
    }

    public Map<UUID,Integer> getModeMap(){return mode;}

    public void setMode(UUID id,int mode)
    {
        this.mode.put(id,mode);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList JobTiles = nbt.getTagList("JobTiles", Constants.NBT.TAG_COMPOUND);
        NBTTagList EnabledList = nbt.getTagList("enables",Constants.NBT.TAG_COMPOUND);
        NBTTagList TotalSimList = nbt.getTagList("TotalSimsList",Constants.NBT.TAG_COMPOUND);
        NBTTagList UnemployedSimList = nbt.getTagList("UnemployedSimsList",Constants.NBT.TAG_COMPOUND);
        NBTTagList FactionList = nbt.getTagList("faction",Constants.NBT.TAG_COMPOUND);


        for (int i = 0; i < JobTiles.tagCount(); i++) {
            NBTTagCompound tag = JobTiles.getCompoundTagAt(i);
            int x = tag.getInteger("Tile x");
            int y = tag.getInteger("Tile y");
            int z = tag.getInteger("Tile Z");
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity entity;
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT){
            System.out.println("tile entity client " + Minecraft.getMinecraft().player.world.getTileEntity(pos));
             entity = Minecraft.getMinecraft().player.world.getTileEntity(pos);}else{
                System.out.println("tile entity server " + FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getTileEntity(pos));
                entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getTileEntity(pos);
            }
            if (entity instanceof ISimJob) {
                addTile(entity);
            }
        }
        for (int i = 0;i < EnabledList.tagCount();i++){
            NBTTagCompound enabled = EnabledList.getCompoundTagAt(i);
            setEnabled(UUID.fromString(enabled.getString("uuid")),enabled.getBoolean("enable"));
        }
        System.out.println("enabled " + enables);

        for (int i = 0;i < TotalSimList.tagCount();i++){
         NBTTagCompound compound = TotalSimList.getCompoundTagAt(i);
             long longid = compound.getLong("key");
             NBTTagList values = compound.getTagList("values", Constants.NBT.TAG_LIST);
             for (int x = 0;x<values.tagCount();x++){
             NBTTagCompound value = values.getCompoundTagAt(x);
             UUID uuid = value.getUniqueId("value");
             addtotalSim(uuid,longid);
        }}

        for (int i = 0;i< UnemployedSimList.tagCount();i++){
            NBTTagCompound compound = TotalSimList.getCompoundTagAt(i);
            System.out.println("reading unemployed sim compound " + compound + " " + i);
            long longid = compound.getLong("key");
            NBTTagList values = compound.getTagList("values", Constants.NBT.TAG_LIST);
            for (int x = 0;x<values.tagCount();x++){
                NBTTagCompound value = values.getCompoundTagAt(x);
                UUID uuid = value.getUniqueId("value");
                addUnemployedsim(uuid,longid);
        }}

        for (int i = 0;i<FactionList.tagCount();i++){
            NBTTagCompound factions = FactionList.getCompoundTagAt(i);
            System.out.println("faction nbt " + factions);
            UUID uuid = factions.getUniqueId("key");
            long longid = factions.getLong("value");
            faction.put(uuid,longid);
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList JobTiles = new NBTTagList();
        NBTTagList modeList = new NBTTagList();
        NBTTagList enabledList = new NBTTagList();
        NBTTagList TotalSimslist = new NBTTagList();
        NBTTagList UnemployedSimsList = new NBTTagList();
        NBTTagList FactionidList = new NBTTagList();
        {
            for (TileEntity tiles : job_tiles) {
                NBTTagCompound tile = new NBTTagCompound();
                tile.setInteger("Tile x", tiles.getPos().getX());
                tile.setInteger("Tile y", tiles.getPos().getY());
                tile.setInteger("Tile Z", tiles.getPos().getZ());
                JobTiles.appendTag(tile);

            }
            nbt.setTag("JobTiles", JobTiles);
            for (UUID uuid : mode.keySet()) {
                NBTTagCompound modes = new NBTTagCompound();
                modes.setInteger(uuid.toString(), mode.get(uuid));
                modeList.appendTag(modes);
            }
            nbt.setTag("Modes", modeList);
            for (UUID uuid : enables.keySet()) {
                NBTTagCompound enabled = new NBTTagCompound();
                enabled.setBoolean("enable", enables.get(uuid));
                enabled.setString("uuid", uuid.toString());
                System.out.println("enabled " + enables.get(uuid));
                enabledList.appendTag(enabled);

            }
            nbt.setTag("enables", enabledList);


            for (UUID uuid: faction.keySet()){
                NBTTagCompound factions = new NBTTagCompound();
                factions.setUniqueId("key",uuid);
                factions.setLong("value",faction.get(uuid));
                FactionidList.appendTag(factions);
            }
            nbt.setTag("faction",FactionidList);


            for (long longid : Total_sims.keySet()){
                NBTTagCompound compound = new NBTTagCompound();
                NBTTagList values = new NBTTagList();
                compound.setLong("key",longid);
                for (UUID uuid: Total_sims.get(longid)){
                    NBTTagCompound value = new NBTTagCompound();
                    value.setUniqueId("value",uuid);
                    values.appendTag(value);

                }
                compound.setTag("values",values);
                TotalSimslist.appendTag(compound);
            }
            nbt.setTag("TotalSimsList",TotalSimslist);

            for (long longid : Unemployed_sims.keySet()){
                NBTTagCompound compound = new NBTTagCompound();
                NBTTagList values = new NBTTagList();
                compound.setLong("key",longid);
                for (UUID uuid: Unemployed_sims.get(longid)){
                    NBTTagCompound value = new NBTTagCompound();
                    value.setUniqueId("value",uuid);
                    values.appendTag(value);
                }
                compound.setTag("values",values);
                UnemployedSimsList.appendTag(compound);
            }
            System.out.println("unemployted tag " + UnemployedSimsList);
            nbt.setTag("UnemployedSimsList",UnemployedSimsList);
            return nbt;
        }}

    public static SaveSimData get(World world) {
        MapStorage storage = world.getMapStorage();
        if (storage == null) return null;
        SaveSimData instance = (SaveSimData) storage.getOrLoadData(SaveSimData.class, DATA_NAME);
        if (instance == null) {
            instance = new SaveSimData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;

    }
}
package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SaveSimDataUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class SaveSimData extends WorldSavedData {
    private static final String DATA_NAME = Reference.MOD_ID + "_SimData";
    private List<FactionData> factions = new ArrayList<>();

    public SaveSimData() {
        super(DATA_NAME);
    }

    public SaveSimData(String s) {
        super(s);
    }



    @Override
    public void readFromNBT(NBTTagCompound nbt) {
          System.out.println("nbt in readfromnbt " + nbt);
        System.out.println(nbt.getTagList("factionlist",10).getTagType());
        NBTTagList factionlist = nbt.getTagList("factionlist", Constants.NBT.TAG_COMPOUND);
        System.out.println("factionlist " +factionlist);
        for (int i = 0;i<factionlist.tagCount();i++){
            factions.add(new FactionData(factionlist.getCompoundTagAt(i).getCompoundTag("faction")));
            System.out.println("faction id just added after reading " + factions.get(0).getFactionId());
            System.out.println("faction add from nbt");
            markDirty();

        }
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList factionlist = new NBTTagList();
        System.out.println("factions " + factions);
        for (FactionData data:factions){
            System.out.println("faction id before writing " + data.getFactionId());
            NBTTagCompound faction = new NBTTagCompound();
            faction.setTag("faction",data.serializeNBT());
            System.out.println("faction list writing " + faction.getTag("faction"));
            factionlist.appendTag(faction);
        }
        nbt.setTag("factionlist",factionlist);
        System.out.println("factionlist " + factionlist);
        return nbt;
    }

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
    public List<FactionData> getFactions(){
        return factions;
    }

    public void addfaction(long id,String name){
        addfaction(new FactionData(id,name,-1));
    }
    public void addfaction(FactionData data){
        factions.add(data);
        updateclients();
        markDirty();
    }


    public FactionData getfaction(long id){
        for (FactionData data: factions){
            if (data.getFactionId() == id){
                return data;
            }
        }
        return null;
    }

    public void deleteFaction(long id){
        for (FactionData data:factions){
            if (data.getFactionId() == id){
                factions.remove(data);
                markDirty();
                updateclients();
                break;
            }
        }
    }

    public void updateclients(){
        PacketHandler.INSTANCE.sendToAll(new SaveSimDataUpdatePacket(this.serializeNBT()));
    }
}
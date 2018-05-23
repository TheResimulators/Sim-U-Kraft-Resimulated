package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import java.util.*;

public class SaveSimData extends WorldSavedData {
    private static final String DATA_NAME = Reference.MOD_ID +"_SimData";
    private Set<UUID> Total_sims = new HashSet<>();
    private Set<UUID> Unemployed_sims = new HashSet<>();

    public SaveSimData() {
        super(DATA_NAME);
    }
    public SaveSimData(String s) {
        super(s);
    }

    public Set<UUID> getTotalSims()
    {
        return Total_sims;
    }
    public void setUnemployed_sims(UUID id)
    {
        Unemployed_sims.add(id);
    }

    public Set<UUID> getUnemployed_sims()
    {
        return Unemployed_sims;
    }

    public void addSim(UUID id)
    {
            Total_sims.add(id);
            markDirty();
    }

    public void spawnedSim(UUID id)
    {
        Total_sims.add(id);
        Unemployed_sims.add(id);
        markDirty();
    }

    public void simDied(UUID id)
    {
        Total_sims.remove(id);
        Unemployed_sims.remove(id);
        markDirty();
    }

    public void hiredsim(UUID id){
        Unemployed_sims.remove(id);
        markDirty();
    }

    public void firedSim(UUID id){
        Unemployed_sims.add(id);
        markDirty();
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList Ttaglist = nbt.getTagList("TSims", Constants.NBT.TAG_COMPOUND);
        NBTTagList Utaglist = nbt.getTagList("USims", Constants.NBT.TAG_COMPOUND);
        for (int i = 0;i < Ttaglist.tagCount(); i++)
        {
            NBTTagCompound tag = Ttaglist.getCompoundTagAt(i);
            UUID id = tag.getUniqueId("TSim");
            addSim(id);
        }
        for (int i = 0; i < Utaglist.tagCount(); i++)
        {
            NBTTagCompound tag = Utaglist.getCompoundTagAt(i);
            UUID id = tag.getUniqueId("USim");
            setUnemployed_sims(id);
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList Ttaglist = new NBTTagList();
        NBTTagList Utaglist = new NBTTagList();

        for (UUID id: Total_sims)
        {
            NBTTagCompound sims = new NBTTagCompound();
            sims.setUniqueId("TSim",id);
            Ttaglist.appendTag(sims);
        }
        nbt.setTag("TSims",Ttaglist);

        for (UUID id : Unemployed_sims)
        {
            NBTTagCompound sims = new NBTTagCompound();
            sims.setUniqueId("USim",id);
            Utaglist.appendTag(sims);
        }
        nbt.setTag("USims",Utaglist);
        return nbt;
    }

    public static SaveSimData get(World world)
    {
        MapStorage storage = world.getMapStorage();
        if(storage == null) return null;
        SaveSimData instance = (SaveSimData) storage.getOrLoadData(SaveSimData.class, DATA_NAME);
        if(instance == null)
        {
            instance = new SaveSimData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;

    }

}
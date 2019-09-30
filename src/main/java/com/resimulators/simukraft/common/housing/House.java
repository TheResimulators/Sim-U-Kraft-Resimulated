package com.resimulators.simukraft.common.housing;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.gen.structure.template.BlockRotationProcessor;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.ArrayList;
import java.util.UUID;

public class House implements INBTSerializable<NBTTagCompound> {
    private int houseId;
    private BlockPos housePos;
    private ArrayList<UUID> occupants = new ArrayList<>();
    private TemplatePlus structure;
    public House(int id, BlockPos pos,TemplatePlus structure){
        houseId = id;
        housePos = pos;
        this.structure = structure;
    }

    public House(){}

    public boolean isOccupied() {
        return occupants.size() > 0;
    }


    public ArrayList<UUID> getoccupants(){

        return null;
    }



    public void addOccupant(UUID sim){
        occupants.add(sim);
    }

    public boolean removeOccupant(UUID sim){
        if (occupants.contains(sim)){
            occupants.remove(sim);
            return true;
        }
        return false;

    }


    public boolean isOccupiedBy(EntitySim sim){
        return occupants.contains(sim);
    }


    public BlockPos getHousePos() {
        return housePos;
    }

    public int getHouseId() {
        return houseId;
    }

    public TemplatePlus getStructure(){
        return structure;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("id",houseId);
        compound.setLong("pos",housePos.toLong());
        compound.setTag("template",structure.writeToNBT(new NBTTagCompound()));
        NBTTagList list = new NBTTagList();
        for (UUID sim:occupants){
            NBTTagCompound simnbt = new NBTTagCompound();
            simnbt.setString("sim id",sim.toString());
            list.appendTag(simnbt);
        }
        compound.setTag("sims",list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        houseId = nbt.getInteger("id");
        housePos = BlockPos.fromLong(nbt.getLong("pos"));
        TemplatePlus temp = new TemplatePlus();
        temp.read(nbt.getCompoundTag("template"));
        structure = temp;

        NBTTagList sims = nbt.getTagList("sims", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < sims.tagCount();i++){
            NBTTagCompound compound = sims.getCompoundTagAt(i);
            occupants.add(UUID.fromString(compound.getString("sim id")));
        }



    }
}
package com.resimulators.simukraft.common.housing;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;

public class Housing implements INBTSerializable<NBTTagCompound> {
    private ArrayList<House> houses = new ArrayList<>();
    public Housing(){}


    public ArrayList<House> getHouses() {
        return houses;
    }

    public ArrayList<House> getAvaliableHouses(){
        ArrayList<House> houses = new ArrayList<>();
        for (House house:this.houses){
            if (!house.isOccupied()){
                houses.add(house);
            }
        }
        return houses;
    }

    public void addHouse(House house){
        houses.add(house);
    }

    public boolean removeHouse(House house){
        if (houses.contains(house)){
            houses.remove(house);
            return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList housing = new NBTTagList();
        for (House house: houses){
            housing.appendTag(house.serializeNBT());
        }
        compound.setTag("housing",housing);
    return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("housing", Constants.NBT.TAG_COMPOUND);
        for (int i = 0;i<list.tagCount();i++){
            NBTTagCompound sim = list.getCompoundTagAt(i);
            House temp = new House();
            temp.deserializeNBT(sim);
            houses.add(temp);
        }
    }
}

package com.resimulators.simukraft.common.interfaces;

import com.resimulators.simukraft.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PlayerCapability extends INBTSerializable<NBTTagCompound> {

    ResourceLocation RL = new ResourceLocation(Reference.MOD_ID,"_Player");

    void setfaction(long id);

    long getfaction();



    class Impl implements PlayerCapability{
        long factionid;
        @Override
        public void setfaction(long id) {
            this.factionid = id;
        }

        @Override
        public long getfaction() {
            return factionid;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setLong("faction id",factionid);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            factionid = nbt.getLong("faction id");
        }

        public void updateClientPlayer(){
            //TODO :send packet with NBTTagcompound and update client player
        }
    }

    class Storage implements Capability.IStorage<PlayerCapability>{

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<PlayerCapability> capability, PlayerCapability instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<PlayerCapability> capability, PlayerCapability instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }
    class Provider implements ICapabilityProvider,ICapabilitySerializable<NBTTagCompound> {
        PlayerCapability playerCapability;
        Capability<PlayerCapability> capability;

        public Provider(Capability<PlayerCapability> playerCapability){
            this.capability = playerCapability;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return this.capability == capability;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return hasCapability(capability, enumFacing) ? (T) capability : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return playerCapability.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            playerCapability.deserializeNBT(compound);

        }
    }
}

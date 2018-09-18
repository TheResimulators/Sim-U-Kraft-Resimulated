package com.resimulators.simukraft.common.interfaces;

import com.resimulators.simukraft.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CowCapability extends INBTSerializable<NBTTagCompound> {

    ResourceLocation RL = new ResourceLocation(Reference.MOD_ID,"_cap");

    void setcontroledspawn();

    boolean iscontroledspawn();

    void setmilked();

    boolean ismilkable();

    void decrementMilkcooldown();

    void resetmilkcooldown();

    int getCooldown();




    class Impl implements CowCapability{

        private byte ismilked = 0;
        private int milkcooldown = 200;
        private byte controledspawn = 0;
        @Override
        public void setcontroledspawn() {
        controledspawn = 1;
        }

        @Override
        public boolean iscontroledspawn() {
            return controledspawn > 0;
        }

        @Override
        public void setmilked() {
            ismilked = 1;
        }

        @Override
        public boolean ismilkable() {
            return ismilked > 0;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound bytes = new NBTTagCompound();
            bytes.setByte("spawn",controledspawn);
            bytes.setByte("milked",ismilked);
            bytes.setInteger("cooldown",milkcooldown);
            return bytes;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            controledspawn = nbt.getByte("spawn");
            ismilked = nbt.getByte("milked");
            milkcooldown = nbt.getInteger("cooldown");

        }
        @Override
        public void decrementMilkcooldown() {
            this.milkcooldown--;
        }

        @Override
        public void resetmilkcooldown() {
            milkcooldown = 200;
            ismilked = 0;
        }

        @Override
        public int getCooldown(){
            return milkcooldown;
        }
    }

    class Storage implements Capability.IStorage<CowCapability>{

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<CowCapability> capability, CowCapability cowCapability, EnumFacing enumFacing) {
            return cowCapability.serializeNBT();
        }

        @Override
        public void readNBT(Capability<CowCapability> capability, CowCapability cowCapability, EnumFacing enumFacing, NBTBase nbtBase) {
            cowCapability.deserializeNBT((NBTTagCompound) nbtBase);
        }

    }


    class Provider implements ICapabilityProvider,ICapabilitySerializable<NBTTagCompound>{

        private CowCapability cowcapability;
        private Capability<CowCapability> capability;

        public Provider(Capability<CowCapability> capability){
            this.cowcapability = capability.getDefaultInstance();
            this.capability = capability;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability,@Nullable EnumFacing facing){
            return this.capability == capability;
        }


        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return hasCapability(capability, facing) ? (T) cowcapability : null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            return cowcapability.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            cowcapability.deserializeNBT(nbt);
        }


    }

}

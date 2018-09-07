package com.resimulators.simukraft.common.interfaces;

import com.resimulators.simukraft.Reference;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.Provider;

public interface CowCapability extends INBTSerializable<NBTTagByte> {

    ResourceLocation RL = new ResourceLocation(Reference.MOD_ID,"_cap");

    void setcontroledspawn();

    boolean iscontroledspawn();



    class Impl implements CowCapability{


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
        public NBTTagByte serializeNBT() {
            return new NBTTagByte(controledspawn);
        }

        @Override
        public void deserializeNBT(NBTTagByte nbtTagByte) {
            controledspawn = nbtTagByte.getByte();

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
            cowCapability.deserializeNBT((NBTTagByte) nbtBase);
        }
    }


    class Provider implements ICapabilityProvider,ICapabilitySerializable<NBTTagByte>{

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
        public NBTTagByte serializeNBT()
        {
            return cowcapability.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagByte nbt)
        {
            cowcapability.deserializeNBT(nbt);
        }


    }

}

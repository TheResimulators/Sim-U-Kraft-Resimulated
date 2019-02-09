package com.resimulators.simukraft.common.interfaces;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SyncPlayerCapPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PlayerCapability extends INBTSerializable<NBTTagCompound> {

    ResourceLocation RL = new ResourceLocation(Reference.MOD_ID,"_Player");

    static ResourceLocation getkey() {
        return RL;
    }

    static Provider getProvidor() {
        return new Provider(ModCapabilities.PlayerCap);
    }


    void setfaction(long id);

    long getfactionid();

    boolean getenabled();

    void setenabled(boolean enabled);

    void setmode(int mode);

    int getmode();


    void updateClient(EntityPlayer player);
    FactionData getfaction(long id);

    void updateClientWithPacket(IMessage message,EntityPlayer entitiy);



    class Impl implements PlayerCapability{
        private long factionid;
        private boolean isenabled = false;
        private int mode = -1;

        @Override
        public long getfactionid() {
            return factionid;
        }

        @Override
        public void setfaction(long id) {
            factionid = id;
        }

        @Override
        public FactionData getfaction(long id) {
            return SaveSimData.get(FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld()).getFaction(id);
        }

        @Override
        public void updateClientWithPacket(IMessage message, EntityPlayer entity) {
            PacketHandler.INSTANCE.sendTo(message,(EntityPlayerMP) entity);
        }

        @Override
        public boolean getenabled() {
            return isenabled;
        }

        @Override
        public void setenabled(boolean enabled) {
            this.isenabled = enabled;
        }

        @Override
        public int getmode() {
            return mode;
        }

        @Override
        public void updateClient(EntityPlayer player) {
            PacketHandler.INSTANCE.sendTo(new SyncPlayerCapPacket(serializeNBT()),(EntityPlayerMP) player);
        }


        @Override
        public void setmode(int mode) {
           this.mode = mode;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setLong("faction id",factionid);
            compound.setInteger("mode",mode);
            compound.setBoolean("enabled",isenabled);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            factionid = nbt.getLong("faction id");
            mode = nbt.getInteger("mode");
            isenabled = nbt.getBoolean("enabled");
        }
    }

    class Storage implements Capability.IStorage<PlayerCapability>{

        public Storage(){}
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
            this.playerCapability = playerCapability.getDefaultInstance();
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return this.capability == capability;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            return hasCapability(capability, enumFacing) ? (T) playerCapability : null;
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

package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import scala.Byte;

public class SyncPlayerCapPacket implements IMessage {
    NBTTagCompound compound;
    public SyncPlayerCapPacket(){}
    public SyncPlayerCapPacket(NBTTagCompound compound){
        this.compound = compound;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,compound);

    }
}

package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TriggerRefreshPacket implements IMessage {
    public NBTTagCompound nbt;
    float credit;


    public TriggerRefreshPacket(){}

    @Override
    public void fromBytes(ByteBuf byteBuf) {

    }

    @Override
    public void toBytes(ByteBuf byteBuf) {

    }
}

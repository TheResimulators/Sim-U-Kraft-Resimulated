package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class FactionCreatedPacket implements IMessage {
    long factionid;
    String name;
    public FactionCreatedPacket(long factionid,String name){
        this.factionid = factionid;
        this.name = name;
    }
    public FactionCreatedPacket(){}
    @Override
    public void fromBytes(ByteBuf buf) {
        factionid = buf.readLong();
        name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(factionid);
        ByteBufUtils.writeUTF8String(buf,name);
    }
}

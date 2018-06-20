package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class FireSimPacket implements IMessage{
    int ids;
    public FireSimPacket(UUID id,int ids){
        this.ids = ids;
        this.sims = id;

    }
    public FireSimPacket(){

    }
    UUID sims;
    @Override
    public void toBytes(ByteBuf bytebuf){
        ByteBufUtils.writeUTF8String(bytebuf, sims.toString());
        bytebuf.writeInt(ids);
    }

    @Override
    public void fromBytes(ByteBuf bytebuf){
        this.sims = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));
        this.ids = bytebuf.readInt();


    }
}

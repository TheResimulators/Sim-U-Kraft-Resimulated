package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class UpdateSimPacket implements IMessage {
    public UpdateSimPacket(UUID id, String list){
        this.sims = id;
        this.List = list;

    }
    public UpdateSimPacket(){

    }
    UUID sims;
    String List;
    @Override
    public void toBytes(ByteBuf bytebuf){
        ByteBufUtils.writeUTF8String(bytebuf, sims.toString());
        ByteBufUtils.writeUTF8String(bytebuf,List);

    }

    @Override
    public void fromBytes(ByteBuf bytebuf){
        this.sims = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));
        this.List = ByteBufUtils.readUTF8String(bytebuf);

    }
}

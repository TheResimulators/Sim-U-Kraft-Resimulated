package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ModeChangePacket implements IMessage {
    UUID id;
    int mode;
    public ModeChangePacket(UUID id, int mode){
        this.id = id;
        this.mode = mode;
    }

    public ModeChangePacket(){}
    @Override
    public void fromBytes(ByteBuf bytebuf)
    {
        this.mode = bytebuf.readInt();
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(mode);
        ByteBufUtils.writeUTF8String(bytebuf,id.toString());



    }
}

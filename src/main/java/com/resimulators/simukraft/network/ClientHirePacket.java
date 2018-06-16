package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ClientHirePacket implements IMessage {
    UUID uuid;
    public ClientHirePacket(){}

    public ClientHirePacket(UUID uuid)
    {
        this.uuid = uuid;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf,uuid.toString());
    }
}

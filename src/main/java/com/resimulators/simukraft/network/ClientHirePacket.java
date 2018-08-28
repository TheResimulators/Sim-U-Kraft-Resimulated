package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ClientHirePacket implements IMessage {
    UUID uuid;
    int x;
    int y;
    int z;
    public ClientHirePacket() {
    }

    public ClientHirePacket(UUID uuid,int x, int y, int z)
    {
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.x = byteBuf.readInt();
        this.y = byteBuf.readInt();
        this.z = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, uuid.toString());
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        byteBuf.writeInt(z);
    }
}

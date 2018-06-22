package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class FireSimTriggerPacket implements IMessage {
    UUID id;
    int x;
    int y;
    int z;

    public FireSimTriggerPacket(){}

    public FireSimTriggerPacket(UUID id, int x, int y, int z)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,id.toString());
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

    }
}

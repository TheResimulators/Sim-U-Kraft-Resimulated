package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ReturnUpdateSimIdPacket implements IMessage {
    UUID uuid;
    int x;
    int y;
    int z;

    public void ReturnSimIdPacket(){}

    public void ReturnSimIdPacket(UUID uuid,int x, int y,int z){
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,uuid.toString());
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);


    }
}

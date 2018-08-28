package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import scala.Byte;

public class LoadStructurePacket implements IMessage {
    String name;
    String type;
    int x;
    int y;
    int z;
    public LoadStructurePacket(){}
    public LoadStructurePacket(String name,String type,int x, int y,int z){
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.name = ByteBufUtils.readUTF8String(byteBuf);
        this.type = ByteBufUtils.readUTF8String(byteBuf);
        this.x = byteBuf.readInt();
        this.y = byteBuf.readInt();
        this.z = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf,name);
        ByteBufUtils.writeUTF8String(byteBuf,type);
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        byteBuf.writeInt(z);

    }
}

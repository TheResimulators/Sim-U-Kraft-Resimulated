package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ServerStructurePacket implements IMessage {
    int x;
    int y;
    int z;

    public ServerStructurePacket(){}
    public ServerStructurePacket(int x,int y,int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.x = byteBuf.readInt();
        this.y = byteBuf.readInt();
        this.z = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.x);
        byteBuf.writeInt(this.y);
        byteBuf.writeInt(this.z);
    }
}

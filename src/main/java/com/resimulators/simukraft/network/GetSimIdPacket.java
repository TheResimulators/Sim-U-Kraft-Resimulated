package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GetSimIdPacket implements IMessage {
    int x;
    int y;
    int z;
    int guiid;
    public GetSimIdPacket(){}
    public GetSimIdPacket(int x, int y, int z,int guiid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.guiid = guiid;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        x = bytebuf.readInt();
        y = bytebuf.readInt();
        z = bytebuf.readInt();
        guiid = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(x);
        bytebuf.writeInt(y);
        bytebuf.writeInt(z);
        bytebuf.writeInt(guiid);
    }
}

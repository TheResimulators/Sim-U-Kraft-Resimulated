package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GetSimIdPacket implements IMessage {
    int x;
    int y;
    int z;
    public GetSimIdPacket(){}
    public GetSimIdPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        x = bytebuf.readInt();
        y = bytebuf.readInt();
        z = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(x);
        bytebuf.writeInt(y);
        bytebuf.writeInt(z);
    }
}

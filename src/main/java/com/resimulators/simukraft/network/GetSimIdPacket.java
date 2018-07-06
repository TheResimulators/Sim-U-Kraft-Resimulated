package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GetSimIdPacket implements IMessage {
    int x;
    int y;
    int z;

    public GetSimIdPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        System.out.println("Reading things");
        x = bytebuf.readInt();
        y = bytebuf.readInt();
        z = bytebuf.readInt();
        System.out.println("Reading " + x + " " + y + " " + z);
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        System.out.println("Writing things");
        bytebuf.writeInt(x);
        bytebuf.writeInt(y);
        bytebuf.writeInt(z);
        System.out.println("Writing " + x + " " + y + " " + z);
    }
}

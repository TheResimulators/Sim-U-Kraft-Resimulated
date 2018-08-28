package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HiringPacket implements IMessage {
    int job;
    int sims;
    int x;
    int y;
    int z;

    public HiringPacket(){}
    public HiringPacket(int id, int job_int,int x, int y, int z) {
        this.job = job_int;
        this.sims = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.sims = bytebuf.readInt();
        this.job = bytebuf.readInt();
        this.x = bytebuf.readInt();
        this.y = bytebuf.readInt();
        this.z = bytebuf.readInt();

    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(sims);
        byteBuf.writeInt(job);
        byteBuf.writeInt(x);
        byteBuf.writeInt(y);
        byteBuf.writeInt(z);
    }
}

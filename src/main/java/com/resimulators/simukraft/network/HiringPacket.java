package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HiringPacket implements IMessage {
    int job;
    int sims;
    public HiringPacket(){}
    public HiringPacket(int id, int job_int) {
        System.out.println("Sending hire packet");
        this.job = job_int;
        this.sims = id;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.sims = bytebuf.readInt();
        this.job = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(sims);
        byteBuf.writeInt(job);
    }
}

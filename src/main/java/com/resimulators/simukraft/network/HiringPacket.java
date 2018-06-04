package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class HiringPacket implements IMessage {

    public HiringPacket(int id, int job_int){
        System.out.println("Sending hire packet");
        this.job = job_int;
        this.sims = id;
    }

    public HiringPacket(){}
    int job;
    int sims;
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

package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class HiringPacket implements IMessage {

    public HiringPacket(UUID id, int job_int){
        System.out.println("Sending hire packet");
        this.job = job_int;
        this.sims = id;
    }

    public HiringPacket(){}
    int job;
    UUID sims;
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.sims = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));
        this.job = byteBuf.getInt(job);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, sims.toString());
        byteBuf.writeInt(job);
    }

    }

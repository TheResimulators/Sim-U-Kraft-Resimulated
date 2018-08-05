package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SimDeathPacket implements IMessage {
    int sims;
    long factionid;
    public SimDeathPacket(){}
    public SimDeathPacket(int id,long factionid) {
        this.sims = id;
        this.factionid = factionid;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.factionid = byteBuf.readLong();
        this.sims = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeLong(factionid);
        byteBuf.writeInt(sims);
    }
}

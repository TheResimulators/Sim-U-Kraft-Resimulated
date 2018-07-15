package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SimDeathPacket implements IMessage {
    int sims;
    public SimDeathPacket(){}
    public SimDeathPacket(int id) {
        this.sims = id;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.sims = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(sims);
    }
}

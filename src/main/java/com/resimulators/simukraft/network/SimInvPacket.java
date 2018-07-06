package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SimInvPacket implements IMessage {
    int id;

    public SimInvPacket(int id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.id = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(id);
    }
}

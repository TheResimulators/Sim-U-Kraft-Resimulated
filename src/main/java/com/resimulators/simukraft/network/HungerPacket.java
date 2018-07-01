package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class HungerPacket implements IMessage {
    int hunger;
    int id;
    public HungerPacket(){}
    public HungerPacket(int hunger,int id)
    {
        this.hunger = hunger;
        this.id = id;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.hunger = buf.readInt();
        this.id = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(hunger);
        buf.writeInt(id);
    }
}

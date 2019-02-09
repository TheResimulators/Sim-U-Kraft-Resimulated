package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateDayPacket implements IMessage {
    int daynum;
    public UpdateDayPacket(){}
    public UpdateDayPacket(int daynum){
        this.daynum = daynum;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        daynum = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(daynum);

    }
}

package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SimDeathPacket implements IMessage {


    public SimDeathPacket(UUID id){
        this.sims = id;

    }
    public SimDeathPacket(){}
    UUID sims;
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.sims = UUID.fromString(ByteBufUtils.readUTF8String(byteBuf));

    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, sims.toString());
    }
}

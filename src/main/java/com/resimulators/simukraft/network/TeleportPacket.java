package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class TeleportPacket implements IMessage {
    int id;
    boolean teleport;
    public TeleportPacket(){}
    public TeleportPacket(int id,boolean teleport){
        this.id = id;
        this.teleport = teleport;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        id=byteBuf.readInt();
        teleport = byteBuf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(id);
        byteBuf.writeBoolean(teleport);

    }
}

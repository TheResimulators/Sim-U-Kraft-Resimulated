package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TeleportClientPacket implements IMessage {
    BlockPos pos;
    int id;
    public TeleportClientPacket(){}
    public TeleportClientPacket(BlockPos pos,int id){
        this.pos = pos;
        this.id = id;

    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        pos = BlockPos.fromLong(byteBuf.readLong());
        id = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeLong(pos.toLong());
        byteBuf.writeInt(id);
    }
}

package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientUpdateMIningPacket implements IMessage {

    boolean mining;
    BlockPos pos;
    public ClientUpdateMIningPacket(){}

    public ClientUpdateMIningPacket(boolean mining, BlockPos blockpos){
        this.mining = mining;
        pos = blockpos;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        mining = buf.readBoolean();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        pos = new BlockPos(x, y, z);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(mining);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }
}

package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MinerUpdateDataPacket implements IMessage {

    NBTTagCompound compound;
    BlockPos pos;
    public MinerUpdateDataPacket(){}
    public MinerUpdateDataPacket(NBTTagCompound compound, BlockPos pos){
        this.compound = compound;
        this.pos = pos;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        compound = ByteBufUtils.readTag(buf);
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf,compound);
        buf.writeLong(pos.toLong());
    }
}

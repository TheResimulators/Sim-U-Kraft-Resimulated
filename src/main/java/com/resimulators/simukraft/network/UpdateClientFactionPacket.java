package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.FactionData;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateClientFactionPacket implements IMessage{
    NBTTagCompound compound;
    public UpdateClientFactionPacket(){}

    public UpdateClientFactionPacket(NBTTagCompound compound){
        this.compound = compound;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        compound = ByteBufUtils.readTag(byteBuf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeTag(byteBuf,compound);
    }
}

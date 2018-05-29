package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CreditsPacket implements IMessage {
    float credit;


    public CreditsPacket(){this.credit = SimEventHandler.getCredits();
    System.out.println("packet value equals " + this.credit);}

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeFloat(credit);

    }
    @Override
    public void fromBytes(ByteBuf bytebuf){
        credit = bytebuf.readFloat();
    }
}



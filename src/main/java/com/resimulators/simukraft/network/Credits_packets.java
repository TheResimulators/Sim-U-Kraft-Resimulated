package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Credits_packets implements IMessage {
    public NBTTagCompound nbt;

    public Credits_packets(){}


    float credit;
    public Credits_packets(float credit){
        this.credit = SimToHire.credits;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeFloat(credit);

    }
    @Override
    public void fromBytes(ByteBuf bytebuf){
        credit = bytebuf.readFloat();
    }
}


abstract class Credits_packet_handler implements IMessageHandler<Credits_packets, IMessage> {

    @Override public IMessage onMessage(Credits_packets message, MessageContext ctx){
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        serverPlayer.getServerWorld().addScheduledTask(() -> {
            System.out.println("credits =" + message.credit );
            SimToHire.credits = message.credit;
        });

    return null;

    }

}


package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.common.entity.player.PlayerCredits;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Arrays;

import static com.resimulators.simukraft.common.entity.player.PlayerCredits.*;

public class Credits_packets implements IMessage {
    public NBTTagCompound nbt;

    public Credits_packets(){}


    float credit = PlayerCredits.credits;
    public Credits_packets(int unemployedsim){
        this.credit = PlayerCredits.credits;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeFloat(credit);

    }
    public void fromBytes(ByteBuf bytebuf){
        credit = bytebuf.readFloat();
    }
}


abstract class Credits_packet_handler implements IMessageHandler<Credits_packets, IMessage> {

    @Override public IMessage onMessage(Credits_packets message, MessageContext ctx){
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        serverPlayer.getServerWorld().addScheduledTask(() -> {
            System.out.println("credits =" + message.credit );
            PlayerCredits.credits = message.credit;
        });

    return null;

    }

}


package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CreditsHandler implements IMessageHandler<CreditsPacket, IMessage> {

    @Override public IMessage onMessage(CreditsPacket message, MessageContext ctx){
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(new Runnable() {

            @Override
            public void run() {

                SimEventHandler.setCredits(message.credit);
            }

        });
        return null;

    }

}

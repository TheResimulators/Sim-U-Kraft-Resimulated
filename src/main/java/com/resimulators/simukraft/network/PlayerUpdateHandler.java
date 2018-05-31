package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PlayerUpdateHandler implements IMessageHandler<PlayerUpdatePacket, IMessage>

{

    @Override public IMessage onMessage(PlayerUpdatePacket message, MessageContext ctx){
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            if(SimEventHandler.getWorldSimData() == null)
            {
                SimEventHandler.setWorldSimData(SaveSimData.get(ctx.getServerHandler().player.world));
            }
            for (UUID id: message.totalsim) {
                SimEventHandler.getWorldSimData().addSim(id);
            }
            for (UUID id: message.totalsim) {
                SimEventHandler.getWorldSimData().setUnemployed_sims(id);
            }
            SimEventHandler.setCredits(message.credits);
        });return null;
    }
}
package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class UpdateSimHandler implements IMessageHandler<UpdateSimPacket, IMessage> {
    @Override
    public IMessage onMessage(UpdateSimPacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            UUID e = message.sims;
            String list = message.List;
            System.out.println("sim " + e);
            if (list.equals("unemployed")) {
                SimEventHandler.getWorldSimData().setUnemployed_sims(e);
                System.out.println("adding SIMS");
            }
            if (list.equals("total")) {
                SimEventHandler.getWorldSimData().addSim(e);
                System.out.println("adding SIMS");
            }
        });
        return null;
    }
}

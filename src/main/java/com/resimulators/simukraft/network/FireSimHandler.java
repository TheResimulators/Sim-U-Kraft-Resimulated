package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class FireSimHandler implements IMessageHandler<FireSimPacket, IMessage> {
    @Override
    public IMessage onMessage(FireSimPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            System.out.println("sim " + id);
            if (!(SimEventHandler.getWorldSimData().getTotalSims().contains(id))) {
                SimEventHandler.getWorldSimData().setUnemployed_sims(message.sims);
                EntitySim sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.ids);
                if (sim != null) {
                    sim.setProfession(0);
                }
            }
        });
        return null;
    }
}
package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class SimSpawnHandler implements IMessageHandler<SimSpawnPacket, IMessage> {
    @Override
    public IMessage onMessage(SimSpawnPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            System.out.println("client sim spawn  " + id);
            if (!(SimEventHandler.getWorldSimData().getTotalSims().contains(id))) {
                SimEventHandler.getWorldSimData().addSim(id);
                SimEventHandler.getWorldSimData().setUnemployed_sims(id);
            }
        });
        return null;
    }
}

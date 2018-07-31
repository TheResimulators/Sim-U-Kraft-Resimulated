package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class HiringHandler implements IMessageHandler<HiringPacket, IMessage> {
    @Override
    public IMessage onMessage(HiringPacket message, MessageContext ctx) {
        IThreadListener mainThread;
        mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            System.out.println("This is working");
            EntitySim sim = (EntitySim) ctx.getServerHandler().player.world.getEntityByID(message.sims);
            UUID id = sim.getUniqueID();
            SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).removeUnemployedSim(id, SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).getPlayerFaction(ctx.getServerHandler().player.getUniqueID()));
            sim.setProfession(message.job);
            SaveSimData.get(ctx.getServerHandler().player.world).SendFactionPacket(new ClientHirePacket(id),SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).getPlayerFaction(ctx.getServerHandler().player.getUniqueID()));
        });
        return null;
    }
}


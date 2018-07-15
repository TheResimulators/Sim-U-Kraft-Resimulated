package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SimDeathHandler implements IMessageHandler<SimDeathPacket, IMessage> {
    @Override
    public IMessage onMessage(SimDeathPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            int id = message.sims;
            EntitySim e = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(id);
            SimEventHandler.getWorldSimData().simDied(e.getUniqueID());
        });
        return null;
    }
}

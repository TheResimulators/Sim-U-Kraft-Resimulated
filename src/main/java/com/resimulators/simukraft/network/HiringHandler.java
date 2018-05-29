package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class HiringHandler implements IMessageHandler<HiringPacket, IMessage> {

    @Override public IMessage onMessage(HiringPacket message, MessageContext ctx){
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            System.out.println(" removing sim " + id);
            EntitySim e = (EntitySim) ctx.getServerHandler().player.getServerWorld().getEntityFromUuid(id);
            SimEventHandler.getWorldSimData().hiredsim(id);
            if (e != null) {
                e.setProfession(message.job);
                SimEventHandler.getWorldSimData().hiredsim(id);
                PacketHandler.INSTANCE.sendToAll(new HiringPacket(id,message.job));
            }


        });return null;
    }
}


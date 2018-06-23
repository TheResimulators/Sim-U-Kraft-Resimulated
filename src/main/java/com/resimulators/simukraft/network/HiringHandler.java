package com.resimulators.simukraft.network;

import com.jcraft.jogg.Packet;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class HiringHandler implements IMessageHandler<HiringPacket, IMessage> {

    @Override public IMessage onMessage(HiringPacket message, MessageContext ctx){
        IThreadListener mainThread;
        mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
                EntitySim sim = (EntitySim) ctx.getServerHandler().player.world.getEntityByID(message.sims);

                UUID id = sim.getUniqueID();
                System.out.println("Hiring sim");
                SimEventHandler.getWorldSimData().hiredsim(id);
                sim.setProfession(message.job);
            PacketHandler.INSTANCE.sendToAll(new ClientHirePacket(id));


        });return null;
    }
}


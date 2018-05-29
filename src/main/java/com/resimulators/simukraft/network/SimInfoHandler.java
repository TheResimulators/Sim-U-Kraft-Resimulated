package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class SimInfoHandler implements IMessageHandler<SimInfoPacket, IMessage> {

        @Override public IMessage onMessage(SimInfoPacket message, MessageContext ctx) {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getServerWorld();
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    UUID id = message.sims;
                    System.out.println("sim " + id);
                    if (!(SimEventHandler.getWorldSimData().getTotalSims().contains(id)))
                    {
                        SimEventHandler.getWorldSimData().addSim(id);
                        SimEventHandler.getWorldSimData().setUnemployed_sims(id);
                    }
                }

            }); return null;
        }
    }

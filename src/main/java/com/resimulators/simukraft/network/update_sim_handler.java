package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class update_sim_handler implements IMessageHandler<update_sim_packet, IMessage> {

    @Override public IMessage onMessage(update_sim_packet message, MessageContext ctx) {
        WorldServer server = ctx.getServerHandler().player.getServerWorld();
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                UUID e = message.sims;
                String list = message.List;
                System.out.println("sim " + e);
                if (list == "unemployed"){

                    SimToHire.unemployedsims.add(e);
                    System.out.println("adding SIMS");
                }
                if (list == "total"){

                    SimToHire.totalsims.add(e);
                    System.out.println("adding SIMS");
                }
            }

        }); return null;
    }
}

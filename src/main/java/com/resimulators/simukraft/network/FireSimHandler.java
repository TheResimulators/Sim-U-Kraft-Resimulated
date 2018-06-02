package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class FireSimHandler implements IMessageHandler<FireSimPacket, IMessage> {

    @Override public IMessage onMessage(FireSimPacket message, MessageContext ctx) {
        Side side = FMLCommonHandler.instance().getSide();
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                UUID id = message.sims;
                System.out.println("sim " + id);
                if (!(SimEventHandler.getWorldSimData().getTotalSims().contains(id)))
                {
                    SimEventHandler.getWorldSimData().setUnemployed_sims(id);
                }
            }

        }); return null;
    }
}
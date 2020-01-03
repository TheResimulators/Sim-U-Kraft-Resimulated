package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientUpdateMinerHandler implements IMessageHandler<ClientUpdateMIningPacket,IMessage> {
    @Override
    public IMessage onMessage(ClientUpdateMIningPacket message, MessageContext ctx) {
        IThreadListener mainthread = ctx.getServerHandler().player.server;

        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                World world = ctx.getServerHandler().player.world;
                TileMiner miner = (TileMiner) world.getTileEntity(message.pos);
                if (miner != null) {
                    miner.setShouldmine(message.mining);
                }
            }
        });
        return null;
    }
}

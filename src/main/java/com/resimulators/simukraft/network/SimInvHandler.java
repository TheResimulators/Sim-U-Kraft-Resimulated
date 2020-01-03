package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SimInvHandler implements IMessageHandler<SimInvPacket,IMessage> {
    public IMessage onMessage(SimInvPacket message, MessageContext ctx) {
        IThreadListener mainthread = ctx.getServerHandler().player.getServerWorld();
        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                ctx.getServerHandler().player.openGui(SimUKraft.instance, GuiHandler.GUI.SIMINV.ordinal(), ctx.getServerHandler().player.world, message.id, 0, 0);
            }
        });
        return null;
    }
}

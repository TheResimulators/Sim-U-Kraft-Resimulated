package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ModeChangeHandler implements IMessageHandler<ModeChangePacket,IMessage> {
    @Override
    public IMessage onMessage(ModeChangePacket message, MessageContext ctx) {
        IThreadListener mainthread = ctx.getServerHandler().player.getServerWorld();

        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                PlayerCapability capability = ctx.getServerHandler().player.getCapability(ModCapabilities.PlayerCap, null);
                capability.setenabled(true);
                capability.setmode(message.mode);
                SaveSimData.get(ctx.getServerHandler().player.world).getFaction(capability.getfactionid()).setMode(message.mode);


            }
        });
        return null;
    }
}

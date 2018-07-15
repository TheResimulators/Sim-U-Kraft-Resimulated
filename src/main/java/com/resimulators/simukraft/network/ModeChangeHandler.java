package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ModeChangeHandler implements IMessageHandler<ModeChangePacket,IMessage> {
    @Override
    public IMessage onMessage(ModeChangePacket message, MessageContext ctx) {
        IThreadListener mainthread = ctx.getServerHandler().player.getServerWorld();

        mainthread.addScheduledTask(() -> {
            SaveSimData.get(ctx.getServerHandler().player.world).setMode(message.id,message.mode);
            SaveSimData.get(ctx.getServerHandler().player.world).setEnabled(message.id,true);

        });
        return null;
    }
}

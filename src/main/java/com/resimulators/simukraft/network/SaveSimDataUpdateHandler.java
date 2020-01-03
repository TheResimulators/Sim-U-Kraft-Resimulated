package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SaveSimDataUpdateHandler implements IMessageHandler<SaveSimDataUpdatePacket,IMessage> {
    @Override
    public IMessage onMessage(SaveSimDataUpdatePacket message, MessageContext ctx) {
        IThreadListener mainthread = Minecraft.getMinecraft();

        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {

                SaveSimData.get(Minecraft.getMinecraft().world).deserializeNBT(message.data);
            }
        });
        return null;
    }
}

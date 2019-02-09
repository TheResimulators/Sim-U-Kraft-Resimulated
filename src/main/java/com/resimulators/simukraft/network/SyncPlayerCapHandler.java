package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncPlayerCapHandler implements IMessageHandler<SyncPlayerCapPacket,IMessage>{
    @Override
    public IMessage onMessage(SyncPlayerCapPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();


        mainThread.addScheduledTask(()->{
        Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).deserializeNBT(message.compound);
        System.out.println("handler "+ Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).getfactionid());
        });
        return null;
    }
}

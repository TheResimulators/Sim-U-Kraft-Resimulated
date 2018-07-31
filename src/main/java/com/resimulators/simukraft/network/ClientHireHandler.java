package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientHireHandler implements IMessageHandler<ClientHirePacket,IMessage> {
    @Override
    public IMessage onMessage(ClientHirePacket message, MessageContext messageContext) {
        IThreadListener mainthread = Minecraft.getMinecraft();
        System.out.println("This is being reached. WOOOOOOO");
        mainthread.addScheduledTask(() -> SaveSimData.get(Minecraft.getMinecraft().world).removeUnemployedSim(message.uuid, SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID())));
        return null;
    }
}

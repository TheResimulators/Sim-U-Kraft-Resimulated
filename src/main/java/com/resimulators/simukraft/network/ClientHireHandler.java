package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientHireHandler implements IMessageHandler<ClientHirePacket,IMessage> {
    @Override
    public IMessage onMessage(ClientHirePacket message, MessageContext messageContext) {
        IThreadListener mainthread = Minecraft.getMinecraft();

        mainthread.addScheduledTask(() -> SimEventHandler.getWorldSimData().hiredsim(message.uuid));
        return null;
    }
}

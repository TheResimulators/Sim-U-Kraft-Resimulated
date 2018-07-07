package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StartingGuiHandler implements IMessageHandler<StartingGuiPacket,IMessage> {
    @Override
    public IMessage onMessage(StartingGuiPacket message, MessageContext ctx) {
        Minecraft.getMinecraft().player.openGui(SimUKraft.instance, GuiHandler.GUI_START,Minecraft.getMinecraft().world,0,0,0);
        return null;
    }
}

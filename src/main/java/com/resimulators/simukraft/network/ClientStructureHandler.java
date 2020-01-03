package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.gui.GuiBuilding;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientStructureHandler implements IMessageHandler<ClientStructuresPacket,IMessage> {
    @Override
    public IMessage onMessage(ClientStructuresPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {

                if (Minecraft.getMinecraft().currentScreen instanceof GuiBuilding) {
                    ((GuiBuilding) Minecraft.getMinecraft().currentScreen).setstructures(message.industrial, message.residential, message.commercial, message.custom, message.structureinfos);
                }
            }
        });

        return null;
    }
}

package com.resimulators.simukraft.network;

import com.resimulators.simukraft.client.gui.GuiHire;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RefreshHandler  implements IMessageHandler<RefreshPacket,IMessage> {

    @Override public IMessage onMessage(RefreshPacket message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiHire)
        {
            GuiHire gui = (GuiHire) mc.currentScreen;
            gui.initGui();
        }
        return null;
    }
}

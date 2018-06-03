package com.resimulators.simukraft.network;

import com.resimulators.simukraft.client.gui.GuiHire;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ReturnSimIdHandler implements IMessageHandler<ReturnSimIdPacket,IMessage> {
    @Override
    public IMessage onMessage(ReturnSimIdPacket message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiHire){
        GuiHire gui =(GuiHire) mc.currentScreen;
        for(int sim: message.sim_ids)
        {
            gui.add_sim(sim);
        }
        }



        return null;
    }
}

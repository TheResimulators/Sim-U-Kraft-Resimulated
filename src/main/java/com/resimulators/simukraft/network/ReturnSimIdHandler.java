package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.gui.GuiHire;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ReturnSimIdHandler implements IMessageHandler<ReturnSimIdPacket,IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ReturnSimIdPacket message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.player.openGui(SimUKraft.instance, GuiHandler.GUI_HIRED, mc.world, message.x, message.y, message.z);
        GuiHire gui =(GuiHire) mc.currentScreen;
        for(int sim: message.sim_ids)
        {
            gui.add_sim(sim);
        }
        gui.initGui();
        return null;
    }
}

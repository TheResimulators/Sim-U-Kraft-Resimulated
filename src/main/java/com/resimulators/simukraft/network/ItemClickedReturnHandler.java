package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.gui.GuiStart;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClickedReturnHandler implements IMessageHandler<ItemClickedReturnPacket,IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ItemClickedReturnPacket message, MessageContext ctx) {
        IThreadListener maintThread = Minecraft.getMinecraft();

        maintThread.addScheduledTask(() -> {
            EnumHand handIn = message.handin;
            boolean isdedicated = message.isdedicated;
            EntityPlayerSP playerIn = Minecraft.getMinecraft().player;
            World worldIn = Minecraft.getMinecraft().world;

            if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).getmode() == -1){
                playerIn.openGui(SimUKraft.instance, GuiHandler.GUI.START.ordinal(), worldIn,0,0,0);
                if (Minecraft.getMinecraft().currentScreen instanceof GuiStart){
                ((GuiStart) Minecraft.getMinecraft().currentScreen).SetModeFactors(isdedicated,message.mode);}
                //playerIn.getHeldItem(handIn).shrink(playerIn.getHeldItem(handIn).getCount());

            }
        });
        return null;
    }
}

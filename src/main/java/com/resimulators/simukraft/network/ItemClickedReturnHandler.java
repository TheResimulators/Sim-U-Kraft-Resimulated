package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
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

public class ItemClickedReturnHandler implements IMessageHandler<ItemClickedReturnPacket,IMessage> {
    @Override
    public IMessage onMessage(ItemClickedReturnPacket message, MessageContext ctx) {
        IThreadListener maintThread = Minecraft.getMinecraft();

        maintThread.addScheduledTask(() -> {
            EnumHand handIn = message.handin;
            boolean isdedicated = message.isdedicated;
            EntityPlayerSP playerIn = Minecraft.getMinecraft().player;
            World worldIn = Minecraft.getMinecraft().world;

            if (!SaveSimData.get(worldIn).isEnabled(playerIn.getUniqueID())){
                playerIn.openGui(SimUKraft.instance, GuiHandler.GUI_START, worldIn,0,0,0);
                playerIn.getHeldItem(handIn).shrink(playerIn.getHeldItem(handIn).getCount());
            }
        });
        return null;
    }
}

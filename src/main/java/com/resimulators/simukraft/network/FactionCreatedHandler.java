package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FactionCreatedHandler implements IMessageHandler<FactionCreatedPacket,IMessage> {
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(FactionCreatedPacket message, MessageContext ctx) {
        IThreadListener mainthread = Minecraft.getMinecraft();

        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                SaveSimData.get(Minecraft.getMinecraft().world).addfaction(message.factionid, message.name);
                SaveSimData.get(Minecraft.getMinecraft().world).getFaction(message.factionid).addPlayer(Minecraft.getMinecraft().player.getUniqueID());
                System.out.println("this was called faction is updated on client");

            }
        });return null;
    }
}

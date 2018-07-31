package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class SimSpawnHandler implements IMessageHandler<SimSpawnPacket, IMessage> {
    @Override
    public IMessage onMessage(SimSpawnPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            Long playerid = SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID());
            if (!(SaveSimData.get(Minecraft.getMinecraft().world).getTotalSims(playerid).contains(id))) {
                SaveSimData.get(Minecraft.getMinecraft().world).addtotalSim(id,playerid);
                SaveSimData.get(Minecraft.getMinecraft().world).addUnemployedsim(id,playerid);
            }
        });
        return null;
    }
}

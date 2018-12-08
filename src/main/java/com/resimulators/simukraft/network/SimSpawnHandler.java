package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class SimSpawnHandler implements IMessageHandler<SimSpawnPacket, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(SimSpawnPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            PlayerCapability capability = Minecraft.getMinecraft().player.getCapability(ModCapabilities.getPlayerCap(),null);
            Long playerid = capability.getfactionid();
            FactionData data = SaveSimData.get(Minecraft.getMinecraft().world).getfaction(playerid);
            if (!data.getTotalSims().contains(id)) {
                data.addTotalSim(id);
                data.addUnemployedSim(id);
            }
        });
        return null;
    }
}

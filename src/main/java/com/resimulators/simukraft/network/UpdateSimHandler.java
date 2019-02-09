package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class UpdateSimHandler implements IMessageHandler<UpdateSimPacket, IMessage> {
    @Override
    public IMessage onMessage(UpdateSimPacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            UUID e = message.sims;
            String list = message.List;
            Long playerid = ctx.getServerHandler().player.getCapability(ModCapabilities.PlayerCap,null).getfactionid();
            if (list.equals("unemployed")) {
                SaveSimData.get(ctx.getServerHandler().player.world).getFaction(playerid).addUnemployedSim(e);
            }
            if (list.equals("total")) {
                SaveSimData.get(ctx.getServerHandler().player.world).getFaction(playerid).addTotalSim(e);
            }
        });
        return null;
    }
}

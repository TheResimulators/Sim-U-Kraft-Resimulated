package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class FireSimHandler implements IMessageHandler<FireSimPacket, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(FireSimPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            UUID playerid = Minecraft.getMinecraft().player.getUniqueID();
            if (!SaveSimData.get(Minecraft.getMinecraft().world).getUnemployedSims(SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID())).contains(id)) {
                SaveSimData.get(Minecraft.getMinecraft().world).addUnemployedsim(id,SaveSimData.get(Minecraft.getMinecraft().world).getPlayerFaction(Minecraft.getMinecraft().player.getUniqueID()));
                EntitySim sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.ids);
                if (sim != null) {
                    sim.setProfession(0);
                }
            }
        });
        return null;
    }
}
package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
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
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                UUID id = message.sims;
                if (!SaveSimData.get(Minecraft.getMinecraft().world).getFaction(Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap, null).getfactionid()).getUnemployedSims().contains(id)) {
                    SaveSimData.get(Minecraft.getMinecraft().world).getFaction(Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap, null).getfactionid()).getUnemployedSims().add(id);
                    EntitySim sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.ids);
                    if (sim != null) {
                        sim.setProfession(0);
                        sim.setNotWorking();
                    }
                }
            }
        });
        return null;
    }
}
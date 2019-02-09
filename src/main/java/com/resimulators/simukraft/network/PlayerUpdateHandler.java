package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PlayerUpdateHandler implements IMessageHandler<PlayerUpdatePacket, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(PlayerUpdatePacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        System.out.println("Faction: " + message.factionid);
        Long uuid = message.factionid;

        mainThread.addScheduledTask(() -> {
            Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).setmode(message.mode);
            SaveSimData.get(Minecraft.getMinecraft().world).getfaction(uuid).addPlayer(Minecraft.getMinecraft().player.getUniqueID());
            for (UUID id : message.totalsim) {
                SaveSimData.get(Minecraft.getMinecraft().world).getfaction(uuid).addTotalSim(id);
            }
            for (UUID id : message.unemployedsim) {
                SaveSimData.get(Minecraft.getMinecraft().world).getfaction(uuid).addUnemployedSim(id);
            }
            SimEventHandler.setCredits(message.credits);
        });
        return null;
    }
}
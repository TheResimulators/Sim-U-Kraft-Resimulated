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

public class SimDeathHandler implements IMessageHandler<SimDeathPacket, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(SimDeathPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            int id = message.sims;
            EntitySim e = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(id);
            System.out.println("entity " + e);
            System.out.println("entity long " + e.getFactionId());
            SaveSimData.get(Minecraft.getMinecraft().world).removeUnemployedSim(e.getUniqueID(),message.factionid);
            SaveSimData.get(Minecraft.getMinecraft().world).removeTotalSim(e.getUniqueID(),message.factionid);
        });
        return null;
    }
}

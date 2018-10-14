package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TeleportClientHandler implements IMessageHandler<TeleportClientPacket,IMessage> {
    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(TeleportClientPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();

        mainThread.addScheduledTask(() -> {
            EntitySim sim =(EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.id);
            sim.setSpawnpos(message.pos);

        });
        return null;
    }
}

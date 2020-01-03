package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HungerHandler implements IMessageHandler<HungerPacket,IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(HungerPacket message, MessageContext ctx) {
        IThreadListener mainthread = Minecraft.getMinecraft();

        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                EntitySim sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.id);
                if (sim != null) {
                    sim.setHunger(message.hunger);
                }
            }
        });
        return null;
    }
}

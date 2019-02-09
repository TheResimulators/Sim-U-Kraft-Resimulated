package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientTeleportHandler implements IMessageHandler<ClientTeleportPacket,IMessage> {
    @Override
    public IMessage onMessage(ClientTeleportPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();

        mainThread.addScheduledTask(() -> {
           EntitySim sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.simid);
           if (sim != null){
               EntityParticleSpawner spawner = (EntityParticleSpawner) Minecraft.getMinecraft().world.getEntityByID(message.particlespawner);
               System.out.println("spawner object " + spawner);
               System.out.println("spawner id " + message.particlespawner);
               spawner.setSim(sim);
           }
        });

        return null;
    }
}

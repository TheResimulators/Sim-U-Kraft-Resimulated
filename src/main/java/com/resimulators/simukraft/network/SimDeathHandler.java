package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.tileentity.TileFarm;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class SimDeathHandler implements IMessageHandler<SimDeathPacket, IMessage> {

    @Override public IMessage onMessage(SimDeathPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        System.out.println("Sim died packet recived");
        System.out.println("Sim id " + message.sims);
        mainThread.addScheduledTask(() -> {
            int id = message.sims;
            System.out.println(" removing sim " + id);
            EntitySim e = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(id);
            SimEventHandler.getWorldSimData().simDied(e.getUniqueID());
            System.out.println("Sim unique id: " + e.getUniqueID());
           

        }); return null;
    }
}

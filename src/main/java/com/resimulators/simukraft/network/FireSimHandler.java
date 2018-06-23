package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class FireSimHandler implements IMessageHandler<FireSimPacket, IMessage> {

    @Override public IMessage onMessage(FireSimPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            UUID id = message.sims;
            System.out.println("sim " + id);
            if (!(SimEventHandler.getWorldSimData().getTotalSims().contains(id)))
            {
                SimEventHandler.getWorldSimData().setUnemployed_sims(message.sims);
                EntitySim sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.ids);
                sim.setProfession(0);
            }
        }); return null;
    }
}
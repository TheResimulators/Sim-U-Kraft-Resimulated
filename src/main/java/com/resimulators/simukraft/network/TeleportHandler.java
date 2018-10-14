package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class TeleportHandler implements IMessageHandler<TeleportPacket,IMessage> {
    @Override
    public IMessage onMessage(TeleportPacket packet, MessageContext ctx) {
        IThreadListener mainThread;
        System.out.println("side " + FMLCommonHandler.instance().getEffectiveSide());
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
         mainThread = ctx.getServerHandler().player.getServerWorld();}
        else{ mainThread = Minecraft.getMinecraft();}

        mainThread.addScheduledTask(() -> {
            EntitySim sim;
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){ sim = (EntitySim) ctx.getServerHandler().player.getServerWorld().getEntityByID(packet.id);}
            else{
                sim =(EntitySim) Minecraft.getMinecraft().world.getEntityByID(packet.id);
            }
            System.out.println("teleport at packet " + packet.teleport);
            sim.setTeleport(packet.teleport);
            System.out.println("sim teleport after setting " + sim.getTeleport());

        });

        return null;
    }
}

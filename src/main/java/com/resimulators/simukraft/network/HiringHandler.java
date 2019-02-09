package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.ISimIndustrial;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class HiringHandler implements IMessageHandler<HiringPacket, IMessage> {
    @Override
    public IMessage onMessage(HiringPacket message, MessageContext ctx) {
        IThreadListener mainThread;
        mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            System.out.println("This is working");
            EntitySim sim = (EntitySim) ctx.getServerHandler().player.world.getEntityByID(message.sims);
            UUID id = sim.getUniqueID();
            FactionData data = SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).getfaction(ctx.getServerHandler().player.getCapability(ModCapabilities.PlayerCap,null).getfactionid());
            data.removeUnemployedSim(sim);
            data.sendFactionPacket(new ClientHirePacket(sim.getUniqueID(),message.x,message.y,message.z));
            sim.setProfession(message.job);
            sim.setJobBlockPos(new BlockPos(message.x,message.y,message.z));
            SaveSimData.get(sim.getEntityWorld()).getfaction(sim.getFactionId()).sendFactionPacket(new ClientHirePacket(id,message.x,message.y,message.z));
            if (ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)) instanceof ISimIndustrial ){
                ((ISimIndustrial)ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z))).setSimname(ctx.getServerHandler().player.getServerWorld().getEntityFromUuid(id).getEntityId());
                ((ISimIndustrial) ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z))).setHired(true);
            }
        });
        return null;
    }
}


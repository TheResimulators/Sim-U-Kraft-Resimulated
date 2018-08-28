package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.ISim;
import com.resimulators.simukraft.common.interfaces.ISimIndustrial;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.tileentity.TileCattle;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).removeUnemployedSim(id, SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).getPlayerFaction(ctx.getServerHandler().player.getUniqueID()));
            sim.setProfession(message.job);
            sim.setJobBlockPos(new BlockPos(message.x,message.y,message.z));
            SaveSimData.get(ctx.getServerHandler().player.world).SendFactionPacket(new ClientHirePacket(id,message.x,message.y,message.z),SaveSimData.get(ctx.getServerHandler().player.getServerWorld()).getPlayerFaction(ctx.getServerHandler().player.getUniqueID()));
            if (ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)) instanceof ISimIndustrial ){
            ((ISimIndustrial)ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z))).setSimname(ctx.getServerHandler().player.getServerWorld().getEntityFromUuid(id).getEntityId());
                ((ISimIndustrial) ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z))).setHired(true);
            }
        });
        return null;
    }
}


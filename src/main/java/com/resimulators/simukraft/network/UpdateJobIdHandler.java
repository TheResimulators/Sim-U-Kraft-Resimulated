package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateJobIdHandler implements IMessageHandler<UpdateJobIdPacket,IMessage> {
    @Override
    public IMessage onMessage(UpdateJobIdPacket message, MessageContext ctx) {
        IThreadListener mainthread = ctx.getServerHandler().player.getServerWorld();
        mainthread.addScheduledTask(() -> {
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(pos);
            EntitySim sim = (EntitySim) ctx.getServerHandler().player.getServerWorld().getEntityByID(message.id);
            if (tile instanceof ISimJob) {
                ((ISimJob) tile).setId(sim.getPersistentID());
                ((ISimJob)tile).setHired(true);
            }
        });
        return null;
    }
}

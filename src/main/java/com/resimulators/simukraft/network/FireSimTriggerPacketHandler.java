package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.tileentity.Events.TileEntityDestroyed;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FireSimTriggerPacketHandler implements IMessageHandler<FireSimTriggerPacket,IMessage> {
    @Override
    public IMessage onMessage(FireSimTriggerPacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();

        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                TileEntity tile = ctx.getServerHandler().player.world.getTileEntity(new BlockPos(message.x, message.y, message.z));
                if (tile instanceof ISimJob) {
                    ((ISimJob) tile).setHired(false);
                }
                TileEntityDestroyed.TileDestroy(new BlockPos(message.x, message.y, message.z), ctx.getServerHandler().player.getServerWorld());
            }
        });
        return null;
    }
}

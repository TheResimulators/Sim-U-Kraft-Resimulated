package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MinerUpdateDataHandler implements IMessageHandler<MinerUpdateDataPacket, IMessage> {
    @Override
    public IMessage onMessage(MinerUpdateDataPacket message, MessageContext ctx) {
        IThreadListener mainthread = ctx.getServerHandler().player.getServerWorld();


        mainthread.addScheduledTask(()->{
            TileEntity entity =ctx.getServerHandler().player.world.getTileEntity(message.pos);
            entity.readFromNBT(message.compound);
            ctx.getServerHandler().player.world.notifyBlockUpdate(message.pos,entity.getBlockType().getBlockState().getBaseState(),entity.getBlockType().getBlockState().getBaseState(),0);
        });
        return null;
    }
}

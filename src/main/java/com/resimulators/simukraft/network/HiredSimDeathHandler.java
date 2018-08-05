package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.interfaces.ISimJob;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HiredSimDeathHandler implements IMessageHandler<HiredSimDeathPacket, IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(HiredSimDeathPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();
        mainThread.addScheduledTask(() -> {
            BlockPos pos = new BlockPos(message.x, message.y, message.z);
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(pos);
            ((ISimJob) tile).removeSim(message.id);
            ((ISimJob) tile).setHired(false);
            ((ISimJob) tile).setId(null);
        });
        return null;
    }
}

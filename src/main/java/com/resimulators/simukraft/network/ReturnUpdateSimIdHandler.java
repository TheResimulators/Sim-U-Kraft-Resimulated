package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ReturnUpdateSimIdHandler implements IMessageHandler<ReturnUpdateSimIdPacket,IMessage> {
    @Override
    public IMessage onMessage(ReturnUpdateSimIdPacket message, MessageContext ctx) {
        IThreadListener mainthread = Minecraft.getMinecraft();
        mainthread.addScheduledTask(() -> {
        BlockPos pos = new BlockPos(message.x,message.y,message.z);
        TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(pos);

        if (tile instanceof iSimJob)
        {
            ((iSimJob)tile).setId(message.uuid);
        }
        });

        return null;
    }
}

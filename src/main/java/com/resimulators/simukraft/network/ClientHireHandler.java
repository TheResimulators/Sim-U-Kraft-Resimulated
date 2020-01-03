package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.ISim;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHireHandler implements IMessageHandler<ClientHirePacket,IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ClientHirePacket message, MessageContext messageContext) {
        IThreadListener mainthread = Minecraft.getMinecraft();
        mainthread.addScheduledTask(new Runnable() {
            @Override
            public void run() {

                long factionid = Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap, null).getfactionid();
                FactionData data = SaveSimData.get(Minecraft.getMinecraft().world).getFaction(factionid);
                ISim tileentity = ((ISim) Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z)));
                tileentity.setId(message.uuid);
                data.removeUnemployedSim(message.uuid);


            }
        });
        return null;
    }
}

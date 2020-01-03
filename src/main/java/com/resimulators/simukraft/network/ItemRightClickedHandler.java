package com.resimulators.simukraft.network;

import com.resimulators.simukraft.ConfigHandler;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemRightClickedHandler implements IMessageHandler<ItemRightClickedPacket,ItemClickedReturnPacket> {
    private int mode;
    @Override
    public ItemClickedReturnPacket onMessage(ItemRightClickedPacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();


        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                if (ctx.getServerHandler().player.getServer().isDedicatedServer()) {
                    mode = ConfigHandler.Server_Configs.mode;
                } else if (SaveSimData.get(ctx.getServerHandler().player.world).getFaction(ctx.getServerHandler().player.getCapability(ModCapabilities.PlayerCap, null).getfactionid()).getMode() != -1) {
                    mode = SaveSimData.get(ctx.getServerHandler().player.world).getFaction(ctx.getServerHandler().player.getCapability(ModCapabilities.PlayerCap, null).getfactionid()).getMode();
                } else {
                    mode = -2;
                }
                ctx.getServerHandler().player.getHeldItem(message.handin).shrink(1);
                System.out.println("modes " + mode);
            }
        });

    return new ItemClickedReturnPacket(ctx.getServerHandler().player.getServer().isDedicatedServer(),message.handin,message.stack,mode);
    }
}

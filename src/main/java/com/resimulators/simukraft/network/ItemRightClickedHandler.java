package com.resimulators.simukraft.network;

import com.resimulators.simukraft.ConfigHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class ItemRightClickedHandler implements IMessageHandler<ItemRightClickedPacket,ItemClickedReturnPacket> {
    private int mode;
    @Override
    public ItemClickedReturnPacket onMessage(ItemRightClickedPacket message, MessageContext ctx) {
        if (ctx.getServerHandler().player.getServer().isDedicatedServer()){
             mode = ConfigHandler.Server_Configs.mode;
        }else if (!SaveSimData.get(ctx.getServerHandler().player.world).getModeMap().isEmpty()){
            UUID uuid = SaveSimData.get(ctx.getServerHandler().player.world).getModeMap().keySet().iterator().next();
            mode = SaveSimData.get(ctx.getServerHandler().player.world).getModeMap().get(uuid);

        }
        else{mode = -2;}
    return new ItemClickedReturnPacket(ctx.getServerHandler().player.getServer().isDedicatedServer(),message.handin,message.stack,mode);
    }
}

package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GetSimIdHandler implements IMessageHandler<GetSimIdPacket,ReturnSimIdPacket> {
    @Override
    public ReturnSimIdPacket onMessage(GetSimIdPacket message, MessageContext ctx) {
        WorldServer world = ctx.getServerHandler().player.getServerWorld();
        int x = message.x;
        int y = message.y;
        int z = message.z;
        long playerid = ctx.getServerHandler().player.getCapability(ModCapabilities.PlayerCap,null).getfactionid();
        return new ReturnSimIdPacket(world, x, y, z, SaveSimData.get(ctx.getServerHandler().player.world).getFaction(playerid).getUnemployedSims().size(),ctx.getServerHandler().player.getUniqueID(),message.guiid);
    }
}

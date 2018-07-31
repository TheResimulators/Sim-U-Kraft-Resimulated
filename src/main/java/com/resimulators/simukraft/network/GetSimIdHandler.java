package com.resimulators.simukraft.network;

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
        long playerid = SaveSimData.get(ctx.getServerHandler().player.world).getPlayerFaction(ctx.getServerHandler().player.getUniqueID());
        return new ReturnSimIdPacket(world, x, y, z, SaveSimData.get(ctx.getServerHandler().player.world).getUnemployedSims(playerid).size(),ctx.getServerHandler().player.getUniqueID());
    }
}

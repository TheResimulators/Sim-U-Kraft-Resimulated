package com.resimulators.simukraft.network;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GetSimIdHandler implements IMessageHandler<GetSimIdPacket,ReturnSimIdPacket> {
    @Override
    public ReturnSimIdPacket onMessage(GetSimIdPacket message, MessageContext ctx) {
        WorldServer world = ctx.getServerHandler().player.getServerWorld();

        return new ReturnSimIdPacket(world);
    }
}

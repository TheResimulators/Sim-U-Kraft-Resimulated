package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.world.World;
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
        System.out.println(message.x + " " + message.y  + " " + message.z);
        System.out.println("Message recieved");
        System.out.println("Sending packet");
        return new ReturnSimIdPacket(world,x,y,z, SimEventHandler.getWorldSimData().getUnemployed_sims().size());
    }
}

package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReturnSimIdPacket implements IMessage {
    WorldServer world;
    Set<Integer> sim_ids;
    public ReturnSimIdPacket() {}

    public ReturnSimIdPacket(WorldServer world)
    {
        this.world = world;
    }
    @Override
    public void fromBytes(ByteBuf bytebuf)
    {
        sim_ids = new HashSet<>();
        for (int i = 0;i<SimEventHandler.getWorldSimData().getUnemployed_sims().size();i++)
        {
        sim_ids.add(bytebuf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf bytebuf)
    {
        for (UUID sim: SimEventHandler.getWorldSimData().getUnemployed_sims())
        {
            EntitySim entitySim = (EntitySim) world.getEntityFromUuid(sim);;
            int id = entitySim.getEntityId();
            bytebuf.writeInt(id);
        }
    }
}

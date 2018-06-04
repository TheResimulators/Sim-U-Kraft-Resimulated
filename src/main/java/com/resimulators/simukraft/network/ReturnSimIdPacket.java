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
    int x;
    int y;
    int z;
    int amount;
    WorldServer world;
    Set<Integer> sim_ids;
    public ReturnSimIdPacket() {}

    public ReturnSimIdPacket(WorldServer world,int x, int y, int z, int amount)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
    }
    @Override
    public void fromBytes(ByteBuf bytebuf)
    {
        this.amount = bytebuf.readInt();
        sim_ids = new HashSet<>();
        for (int i = 0;i<amount;i++)
        {
        sim_ids.add(bytebuf.readInt());
        }
        this.x = bytebuf.readInt();
        this.y = bytebuf.readInt();
        this.z = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytebuf)
    {
        bytebuf.writeInt(amount);
        for (UUID sim: SimEventHandler.getWorldSimData().getUnemployed_sims())
        {
            EntitySim entitySim = (EntitySim) world.getEntityFromUuid(sim);;
            int id = entitySim.getEntityId();
            bytebuf.writeInt(id);
        }
        bytebuf.writeInt(x);
        bytebuf.writeInt(y);
        bytebuf.writeInt(z);
    }
}

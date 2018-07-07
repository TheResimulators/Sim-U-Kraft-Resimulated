package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.*;

public class ReturnSimIdPacket implements IMessage {
    int x;
    int y;
    int z;
    int amount;
    WorldServer world;
    Set<Integer> sim_ids;
    List<String> sim_names = new ArrayList<>();
    public ReturnSimIdPacket(){}
    public ReturnSimIdPacket(WorldServer world, int x, int y, int z, int amount) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.amount = bytebuf.readInt();
        sim_ids = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            sim_ids.add(bytebuf.readInt());
            String name = ByteBufUtils.readUTF8String(bytebuf);
            System.out.println(sim_names);
            System.out.println("Name: " + name);
            sim_names.add(name);
        }
        this.x = bytebuf.readInt();
        this.y = bytebuf.readInt();
        this.z = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        int num = 0;
        bytebuf.writeInt(amount);
        for (UUID sim : SimEventHandler.getWorldSimData().getUnemployed_sims()) {
            EntitySim entitySim = (EntitySim) world.getEntityFromUuid(sim);
            int id = entitySim.getEntityId();
            bytebuf.writeInt(id);
            num++;
            System.out.println(entitySim.getName());
            ByteBufUtils.writeUTF8String(bytebuf, entitySim.getName());
        }
        System.out.println(num);
        System.out.println(amount);
        bytebuf.writeInt(x);
        bytebuf.writeInt(y);
        bytebuf.writeInt(z);
    }
}

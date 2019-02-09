package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.*;

public class ReturnSimIdPacket implements IMessage {
    int x;
    int y;
    int z;
    int amount;
    int guiid;
    UUID playerid;
    WorldServer world;
    Set<Integer> sim_ids;
    List<String> sim_names = new ArrayList<>();
    public ReturnSimIdPacket(){}
    public ReturnSimIdPacket(WorldServer world, int x, int y, int z, int amount,UUID playerid,int guiid) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.amount = amount;
        this.playerid = playerid;
        this.guiid = guiid;
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.amount = bytebuf.readInt();
        sim_ids = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            sim_ids.add(bytebuf.readInt());
            String name = ByteBufUtils.readUTF8String(bytebuf);
            sim_names.add(name);
        }
        this.x = bytebuf.readInt();
        this.y = bytebuf.readInt();
        this.z = bytebuf.readInt();
        this.guiid = bytebuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        int num = 0;
        long factionid = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getPlayerEntityByUUID(playerid).getCapability(ModCapabilities.PlayerCap,null).getfactionid();
        bytebuf.writeInt(amount);
        for (UUID sim : SaveSimData.get(world).getFaction(factionid).getUnemployedSims()) {
            EntitySim entitySim = (EntitySim) world.getEntityFromUuid(sim);
            int id = entitySim.getEntityId();
            bytebuf.writeInt(id);
            num++;
            ByteBufUtils.writeUTF8String(bytebuf, entitySim.getName());
        }
        bytebuf.writeInt(x);
        bytebuf.writeInt(y);
        bytebuf.writeInt(z);
        bytebuf.writeInt(guiid);
    }
}

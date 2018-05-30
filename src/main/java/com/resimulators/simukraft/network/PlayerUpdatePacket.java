package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerUpdatePacket implements IMessage {
    Set<UUID> totalsim = new HashSet<>();
    int totalsimsize;
    Set<UUID> unemployedsim = new HashSet<>();
    int unemployedsize;
    float credits;
    SaveSimData data;
    public PlayerUpdatePacket(Set<UUID> totalsims, Set<UUID> unemployedsims, float credits)
    {
        this.totalsim = totalsims;
        this.unemployedsim = unemployedsims;
        this.credits = credits;
        this.totalsimsize = totalsim.size();
        this.unemployedsize = unemployedsim.size();
    }
    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.totalsimsize = bytebuf.readInt();
        for (int i = 0;i< this.totalsimsize;i++)
        {
            totalsim.add(UUID.fromString(ByteBufUtils.readUTF8String(bytebuf)));

        }
        this.unemployedsize = bytebuf.readInt();
        for (int i = 0;i<this.unemployedsize;i++)
        {
            unemployedsim.add(UUID.fromString(ByteBufUtils.readUTF8String(bytebuf)));
        }
        this.credits = bytebuf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(totalsimsize);
        for (UUID sim: totalsim) {
            ByteBufUtils.writeUTF8String(bytebuf, sim.toString());
        }
        bytebuf.writeInt(unemployedsize);
        for (UUID sim: unemployedsim)
        {
            ByteBufUtils.writeUTF8String(bytebuf, sim.toString());
        }
        bytebuf.writeFloat(credits);
    }


    class PlayerupdateHandler implements IMessageHandler<PlayerUpdatePacket, IMessage>

    {

        @Override public IMessage onMessage(PlayerUpdatePacket message, MessageContext ctx){
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            if(SimEventHandler.getWorldSimData() == null)
            {
                SimEventHandler.setWorldSimData(SaveSimData.get(ctx.getServerHandler().player.world));
            }
            for (UUID id: message.totalsim) {
                SimEventHandler.getWorldSimData().addSim(id);
            }
            for (UUID id: message.totalsim) {
                SimEventHandler.getWorldSimData().setUnemployed_sims(id);
            }
            SimEventHandler.setCredits(credits);
        });return null;
    }
    }
}

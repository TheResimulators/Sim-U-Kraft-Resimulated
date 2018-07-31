package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerUpdatePacket implements IMessage {
    Set<UUID> totalsim;
    int totalsimsize;
    Set<UUID> unemployedsim;
    int unemployedsize;
    float credits;
    long factionid;
    public PlayerUpdatePacket(){}
    public PlayerUpdatePacket(Set<UUID> totalsims, Set<UUID> unemployedsims, float credits,long factionid) {
        this.totalsim = totalsims;
        this.unemployedsim = unemployedsims;
        this.credits = credits;
        this.factionid = factionid;
        if (totalsims != null && unemployedsims != null){
        this.totalsimsize = totalsim.size();
        this.unemployedsize = unemployedsim.size();
    }else{
            this.totalsimsize = 0;
            this.unemployedsize = 0;
        }
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.totalsimsize = bytebuf.readInt();
        totalsim = new HashSet<>(totalsimsize);
        for (int i = 0; i < this.totalsimsize; i++) {
            UUID sim = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));
            totalsim.add(sim);
        }
        this.unemployedsize = bytebuf.readInt();
        unemployedsim = new HashSet<>(unemployedsize);
        for (int i = 0; i < this.unemployedsize; i++) {
            unemployedsim.add(UUID.fromString(ByteBufUtils.readUTF8String(bytebuf)));
        }
        this.credits = bytebuf.readFloat();
        this.factionid = bytebuf.readLong();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(totalsimsize);
        for (UUID sim : totalsim) {
            ByteBufUtils.writeUTF8String(bytebuf, sim.toString());
        }
        bytebuf.writeInt(unemployedsize);
        System.out.println("unemployed sims " + unemployedsim);
        for (UUID sim : unemployedsim) {
            ByteBufUtils.writeUTF8String(bytebuf, sim.toString());
        }
        bytebuf.writeFloat(credits);
        bytebuf.writeLong(factionid);
    }
}


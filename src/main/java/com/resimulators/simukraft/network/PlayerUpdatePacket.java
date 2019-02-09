package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.*;

public class PlayerUpdatePacket implements IMessage {
    List<UUID> totalsim;
    int totalsimsize;
    List<UUID> unemployedsim;
    int unemployedsize;
    float credits;
    long factionid;
    int mode;
    public PlayerUpdatePacket(){}
    public PlayerUpdatePacket(List<UUID> totalsims, List<UUID> unemployedsims, float credits, long factionid, int mode) {
        this.totalsim = totalsims;
        this.unemployedsim = unemployedsims;
        this.credits = credits;
        this.factionid = factionid;
        if (totalsims != null && unemployedsims != null){
        this.totalsimsize = totalsim.size();
        this.unemployedsize = unemployedsim.size();
        this.mode = mode;
    }else{
            this.totalsimsize = 0;
            this.unemployedsize = 0;
        }
    }

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.mode = bytebuf.readInt();
        this.totalsimsize = bytebuf.readInt();
        totalsim = new ArrayList<>(totalsimsize);
        for (int i = 0; i < this.totalsimsize; i++) {
            UUID sim = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));
            totalsim.add(sim);
        }
        this.unemployedsize = bytebuf.readInt();
        unemployedsim = new ArrayList<>(unemployedsize);
        for (int i = 0; i < this.unemployedsize; i++) {
            unemployedsim.add(UUID.fromString(ByteBufUtils.readUTF8String(bytebuf)));
        }
        this.credits = bytebuf.readFloat();
        this.factionid = bytebuf.readLong();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(mode);
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


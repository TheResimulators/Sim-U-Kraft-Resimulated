package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.player.SaveSimData;
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

    public PlayerUpdatePacket(Set<UUID> totalsims, Set<UUID> unemployedsims, float credits) {
        this.totalsim = totalsims;
        this.unemployedsim = unemployedsims;
        this.credits = credits;
        this.totalsimsize = totalsim.size();
        this.unemployedsize = unemployedsim.size();
    }
    public PlayerUpdatePacket(){}

    @Override
    public void fromBytes(ByteBuf bytebuf) {
        this.totalsimsize = bytebuf.readInt();
        totalsim = new HashSet<>(totalsimsize);
        for (int i = 0; i < this.totalsimsize; i++) {
            UUID sim = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));
            System.out.println("Reading sim " + sim);
            totalsim.add(sim);

        }
        this.unemployedsize = bytebuf.readInt();
        unemployedsim = new HashSet<>(unemployedsize);
        for (int i = 0; i < this.unemployedsize; i++) {
            unemployedsim.add(UUID.fromString(ByteBufUtils.readUTF8String(bytebuf)));
        }
        this.credits = bytebuf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf bytebuf) {
        bytebuf.writeInt(totalsimsize);
        for (UUID sim : totalsim) {
            System.out.println("writing uuid " + sim);
            ByteBufUtils.writeUTF8String(bytebuf, sim.toString());
        }
        bytebuf.writeInt(unemployedsize);
        for (UUID sim : unemployedsim) {
            ByteBufUtils.writeUTF8String(bytebuf, sim.toString());
        }
        bytebuf.writeFloat(credits);
    }
}


package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientTeleportPacket implements IMessage {
    int simid;
    int particlespawner;
    public ClientTeleportPacket(){}
    public ClientTeleportPacket(int sim,int particle){
        this.simid = sim;
        this.particlespawner = particle;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.simid = byteBuf.readInt();
        this.particlespawner = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(simid);
        byteBuf.writeInt(particlespawner);

    }
}

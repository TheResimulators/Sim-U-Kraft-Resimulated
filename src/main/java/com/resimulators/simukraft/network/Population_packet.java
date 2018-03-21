package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.apache.http.util.TextUtils;
import sun.plugin.dom.core.Text;

import java.util.Arrays;
import java.util.List;

public abstract class Population_packet implements IMessage {

    public Population_packet(){}


    private String unemployedsim = Arrays.toString(SimToHire.unemployedsims.toArray());
    public Population_packet(String unemployedsim){
        this.unemployedsim = unemployedsim;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf,unemployedsim);

    }
    public void fromBytes(ByteBuf bytebuf){
        unemployedsim = ByteBufUtils.readUTF8String(bytebuf);
    }
}


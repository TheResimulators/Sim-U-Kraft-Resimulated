package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.Charset;
import java.util.UUID;

import static java.nio.charset.Charset.defaultCharset;

public class Siminfo_packet implements IMessage {
    public NBTTagCompound nbt;
    private UUID sim;

    public  Siminfo_packet(){

    }
    UUID sims;
    public void setsim(){
        sims = SimToHire.getSimID();
    }

    @Override
    public void toBytes(ByteBuf bytebuf){
        bytebuf.writeCharSequence(sims.toString(), defaultCharset());
    }

    @Override
    public void fromBytes(ByteBuf bytebuf){
        UUID sims = UUID.fromString(bytebuf.readCharSequence(bytebuf.readableBytes(), Charset.defaultCharset()).toString());

    }
}

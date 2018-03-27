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


    public  Siminfo_packet(UUID id){
        this.sims = id;

    }
    public Siminfo_packet(){

    }
    UUID sims;
    @Override
    public void toBytes(ByteBuf bytebuf){
        ByteBufUtils.writeUTF8String(bytebuf, sims.toString());

    }

    @Override
    public void fromBytes(ByteBuf bytebuf){
        this.sims = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));

    }
}

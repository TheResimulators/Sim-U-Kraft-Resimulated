package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import scala.util.control.Exception;

import java.util.ArrayList;
import java.util.List;

public class ClientStructuresPacket implements IMessage {
    List<String> residential = new ArrayList<>();
    List<String> industrial= new ArrayList<>();
    List<String> commercial= new ArrayList<>();
    List<String> custom= new ArrayList<>();
    public ClientStructuresPacket(){}
    public ClientStructuresPacket(List<String> residential, List<String> industrial, List<String> commercial, List<String> custom){
        System.out.println("industrial at packet " + industrial);
        this.residential = residential;
        this.industrial = industrial;
        this.commercial = commercial;
        this.custom = custom;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        int residentialsize = byteBuf.readInt();
        for (int i = 0;i<residentialsize;i++){
            this.residential.add(ByteBufUtils.readUTF8String(byteBuf));
        }
        int industrialsize = byteBuf.readInt();
        for (int i = 0;i<industrialsize;i++){
            this.industrial.add(ByteBufUtils.readUTF8String(byteBuf));
        }
        int commercialsize = byteBuf.readInt();
        for (int i = 0;i<commercialsize;i++){
            commercial.add(ByteBufUtils.readUTF8String(byteBuf));
        }
        int customsize = byteBuf.readInt();
        for (int i = 0;i<customsize;i++){
            custom.add(ByteBufUtils.readUTF8String(byteBuf));
        }
        System.out.println("industrial after being read "+ industrial);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(residential.size());
        for (String string: residential){
            ByteBufUtils.writeUTF8String(byteBuf,string);
        }
        byteBuf.writeInt(industrial.size());
        for (String string: industrial){
            ByteBufUtils.writeUTF8String(byteBuf,string);
        }
        byteBuf.writeInt(commercial.size());
        for (String string: commercial){
            ByteBufUtils.writeUTF8String(byteBuf,string);
        }
        byteBuf.writeInt(custom.size());
        for (String string:custom){
            ByteBufUtils.writeUTF8String(byteBuf,string);
        }

    }
}

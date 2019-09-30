package com.resimulators.simukraft.network;

import com.resimulators.simukraft.structure.TemplatePlus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import scala.Array;
import scala.util.control.Exception;

import java.util.ArrayList;
import java.util.List;

public class ClientStructuresPacket implements IMessage {
    List<String> residential = new ArrayList<>();
    List<String> industrial= new ArrayList<>();
    List<String> commercial= new ArrayList<>();
    List<String> custom= new ArrayList<>();
    List<TemplatePlus> structures = new ArrayList<>();
    List<StructureInfo> structureinfos = new ArrayList<>();

    public ClientStructuresPacket(){}
    public ClientStructuresPacket(List<String> residential, List<String> industrial, List<String> commercial, List<String> custom, ArrayList<TemplatePlus> structures){
        System.out.println("industrial at packet " + industrial);
        this.residential = residential;
        this.industrial = industrial;
        this.commercial = commercial;
        this.custom = custom;
        this.structures = structures;
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
        int structuresint = byteBuf.readInt();
        for (int i = 0;i<structuresint;i++){
            double price = byteBuf.readLong();
            String profession = ByteBufUtils.readUTF8String(byteBuf);
            String author = ByteBufUtils.readUTF8String(byteBuf);
            long size = byteBuf.readLong();

            StructureInfo info = new StructureInfo(price,profession,author,size);
            structureinfos.add(info);
        }
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

        byteBuf.writeInt(structures.size());
        for (TemplatePlus structure:structures){
            byteBuf.writeDouble(structure.getPrice());
            ByteBufUtils.writeUTF8String(byteBuf,structure.getProfession());
            ByteBufUtils.writeUTF8String(byteBuf,structure.getAuthor());
            byteBuf.writeLong(structure.getSize().toLong());
        }

    }


    public class StructureInfo{
        private double price;
        private String profession;
        private String author;
        private BlockPos size;
        StructureInfo(double price, String profession,String author,long size){
            this.price = price;
            this.profession = profession;
            this.author = author;
            this.size = BlockPos.fromLong(size);
        }

        public String getProfession() {
            return profession;
        }

        public BlockPos getSize() {
            return size;
        }

        public String getAuthor() {
            return author;
        }

        public double getPrice() {
            return price;
        }
    }
}

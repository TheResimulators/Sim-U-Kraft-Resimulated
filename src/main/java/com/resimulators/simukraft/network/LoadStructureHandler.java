package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;

public class LoadStructureHandler implements IMessageHandler<LoadStructurePacket,IMessage> {
    @Override
    public IMessage onMessage(LoadStructurePacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        String type = message.type;
        String name = message.name;

        mainThread.addScheduledTask(()->{
            System.out.println("this is being reached loading file now");
            TileConstructor tile = null;
            System.out.println("block at pos " + message.x+ " "+ message.y +" " + message.z);
            if (ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)) instanceof TileConstructor){
                tile = ((TileConstructor)ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)));
                System.out.println("setting tile");
            }
            System.out.println(Loader.instance().getConfigDir() +"\\simukraft\\structures\\"+type +"\\" +name+".struct");
            if (new File(Loader.instance().getConfigDir() +"\\simukraft\\structures\\"+type +"\\" +name+".struct").exists()){
                System.out.println("this file exists");
                Structure file = Structure.load(new File(Loader.instance().getConfigDir() +"\\simukraft\\structures\\"+type +"\\" +name+".struct"));
                System.out.println("tile " + tile);
                if (tile != null){
                tile.setStructure(file);
                tile.building = true;
                System.out.println("called");
            }}

        });
        return null;
    }
}

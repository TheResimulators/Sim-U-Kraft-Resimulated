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
            TileConstructor tile = null;
            if (ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)) instanceof TileConstructor){
                tile = ((TileConstructor)ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)));
            }
            if (new File(Loader.instance().getConfigDir() +"\\simukraft\\structures\\"+type +"\\" +name+".struct").exists()){
                Structure file = Structure.load(new File(Loader.instance().getConfigDir() +"\\simukraft\\structures\\"+type +"\\" +name+".struct"));
                file.setFacing(ctx.getServerHandler().player.getHorizontalFacing());
                if (tile != null){
                tile.setStructure(file);
                tile.building = true;
            }}

        });
        return null;
    }
}

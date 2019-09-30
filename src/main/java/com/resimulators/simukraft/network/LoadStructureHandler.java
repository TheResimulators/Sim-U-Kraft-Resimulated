package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.structure.StructureUtils;
import com.resimulators.simukraft.structure.TemplateManagerPlus;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;

public class LoadStructureHandler implements IMessageHandler<LoadStructurePacket,IMessage> {
    @Override
    public IMessage onMessage(LoadStructurePacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        String name = message.name;
        mainThread.addScheduledTask(()->{
            TileConstructor tile = null;
            WorldServer world = ctx.getServerHandler().player.getServerWorld();
            if (ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)) instanceof TileConstructor){
                tile = ((TileConstructor)ctx.getServerHandler().player.getServerWorld().getTileEntity(new BlockPos(message.x,message.y,message.z)));
            }
            TemplatePlus template = StructureUtils.loadStructure(world.getMinecraftServer(),ctx.getServerHandler().player.world,message.name.replace(".nbt",""));
            EntitySim sim = ((EntitySim)world.getEntityFromUuid(tile.getId()));
            sim.setStructure(template);
            sim.setFacing(tile.getFacing());
            sim.setStartPos(tile.getPos().offset(tile.getFacing()));
            sim.setAllowedToBuild(true);
        });
        return null;
    }
}

package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerStructureHandler implements IMessageHandler<ServerStructurePacket,IMessage> {
    @Override
    public ClientStructuresPacket onMessage(ServerStructurePacket message, MessageContext ctx) {
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        List<String> residentialList = new ArrayList<>();
        List<String> industrialList = new ArrayList<>();
        List<String> commercialList = new ArrayList<>();
        List<String> customList = new ArrayList<>();
        mainThread.addScheduledTask(() ->{

            if (new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\").exists()){
                System.out.println("the config file exists");
               if (new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\residential\\").exists()){
                   System.out.println("residential folder exists");
                   File residential = new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\residential\\");
                   residentialList.addAll(Arrays.asList(residential.list()));
               }
                if (new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\industrial\\").exists()){
                    System.out.println("industrial buildings exist");
                    File industrial = new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\industrial\\");
                    industrialList.addAll(Arrays.asList(industrial.list()));
           }
                if (new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\commercial\\").exists()){
                    System.out.println("commercial works");
                    File commercial = new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\commercial\\");
                    commercialList.addAll(Arrays.asList(commercial.list()));
            }
                if (new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\special\\").exists()){
                    System.out.println("custom works");
                    File custom = new File(Loader.instance().getConfigDir()+"\\simukraft\\structures\\special\\");
                    customList.addAll(Arrays.asList(custom.list()));
                }
                System.out.println("config dir " + Loader.instance().getConfigDir());
                System.out.println("files " + residentialList + " " + industrialList + " " + commercialList + " " + customList);}
                PacketHandler.INSTANCE.sendTo(new ClientStructuresPacket(residentialList,industrialList,commercialList,customList),ctx.getServerHandler().player);
            System.out.println("poses " + message.x + message.y + message.z);
            Minecraft.getMinecraft().player.openGui(SimUKraft.instance, GuiHandler.GUI_BUILDER,ctx.getServerHandler().player.getServerWorld(),message.x,message.y,message.z);
        });
        return null;
    }
}

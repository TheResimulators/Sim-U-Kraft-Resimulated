package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.structure.StructureUtils;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
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
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                MinecraftServer server = ctx.getServerHandler().player.server;
                String filepath = ctx.getServerHandler().player.getServerWorld().getSaveHandler().getWorldDirectory().getAbsolutePath() + "/structures/";
                File folder = new File(filepath);
                ArrayList<TemplatePlus> structures = new ArrayList<>();
                System.out.println(folder.listFiles());
                if (folder.exists()) {
                    if (folder.listFiles().length > 0) {
                        for (File file : folder.listFiles()) {

                            TemplatePlus structure = StructureUtils.loadStructure(server, ctx.getServerHandler().player.world, file.getName().replace(".nbt", ""));
                            String category = structure.getCategory();

                            switch (category) {

                                default:
                                    customList.add(file.getName());
                                    break;
                                case "residential":
                                    residentialList.add(file.getName());
                                    break;
                                case "commercial":
                                    commercialList.add(file.getName());
                                    break;
                                case "industrial":
                                    industrialList.add(file.getName());
                                    break;

                            }
                            structures.add(structure);

                        }
                    }
                }
                PacketHandler.INSTANCE.sendTo(new ClientStructuresPacket(residentialList, industrialList, commercialList, customList, structures), ctx.getServerHandler().player);
                Minecraft.getMinecraft().player.openGui(SimUKraft.instance, GuiHandler.GUI.BUILDER.ordinal(), ctx.getServerHandler().player.getServerWorld(), message.x, message.y, message.z);
            }
        });
        return null;
    }
}

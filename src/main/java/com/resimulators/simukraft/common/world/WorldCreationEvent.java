package com.resimulators.simukraft.common.world;


import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.server.FMLServerHandler;
import java.io.File;
@Mod.EventBusSubscriber
public class WorldCreationEvent {
    private static MinecraftServer server;

    @SubscribeEvent
    public static void worldCreation(WorldEvent.Load event) {
        if (event.getWorld().isRemote) {
            System.out.println("this is the client");
        } else {
            if (event.getWorld().getMinecraftServer().isDedicatedServer()){
                server = FMLServerHandler.instance().getServer();
            }else{
                server = event.getWorld().getMinecraftServer();
            }

            System.out.println("this happened");

            if (server != null) {
                if (!new File("./saves/" + server.getWorldName() + "/structures/").exists()) {
                    File folder = new File(server.getDataDirectory() + "/saves/" + server.getWorldName() + "/structures/");
                    if (folder.mkdir()) {
                        System.out.println("Structure folder created");
                    }
                }
            }
        }
    }
}
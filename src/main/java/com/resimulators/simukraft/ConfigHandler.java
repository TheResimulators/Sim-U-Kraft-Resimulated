package com.resimulators.simukraft;

import com.google.common.collect.Lists;
import jdk.nashorn.internal.ir.annotations.Ignore;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.sql.Ref;
import java.util.List;

@Config(modid=Reference.MOD_ID, name = Reference.MOD_ID)
public class ConfigHandler {


    @Config.Name("Special Spawn Chance")
    @Config.Comment("Decides from 1 out of X what chance special sims spawn with.")
    public static int specialSpawn = 500;

    @Config.Name("Server Configs")
    @Config.Comment("Only used for dedicated server")
    public static ServerConfigs Server_Configs = new ServerConfigs();

    @Mod.EventBusSubscriber(modid =Reference.MOD_ID)
    public static class ConfigEvents {

        @SubscribeEvent
        public static void ChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Reference.MOD_ID)) {
                ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }

    }
    @Ignore
    public static class ServerConfigs{
        @Config.Name("Server Mode")
        @Config.Comment("used to set the mode of the mod on a dedicated server\n" +
                "-1: Disabled\n 1: Survival\n 2: Creative\n Default is -1")
        public int mode = -1;
    }
}


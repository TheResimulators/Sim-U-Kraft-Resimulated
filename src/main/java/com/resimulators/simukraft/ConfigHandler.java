package com.resimulators.simukraft;

import jdk.nashorn.internal.ir.annotations.Ignore;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID, name = Reference.MOD_NAME)
public class ConfigHandler {


    @Config.Name("Special Spawn Chance")
    @Config.Comment("Decides from 1 out of X what chance special sims spawn with. Settings this to 1 means that every Sim is a special Sim.")
    public static int specialSpawn = 500;

    @Config.Name("Building Price Multiplier")
    @Config.Comment("The price of the buildings is multiplied by this value.")
    public static int build_price = 10;

    @Config.Name("Server Configs")
    @Config.Comment("Only used for dedicated server")
    public static ServerConfigs Server_Configs = new ServerConfigs();

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
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


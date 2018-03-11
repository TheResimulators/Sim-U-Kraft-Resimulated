package com.resimulators.simukraft;

import com.google.common.collect.Lists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.List;

/**
 * Created by fabbe on 11/03/2018 - 6:52 PM.
 */
public class ConfigHandler {
    public static ConfigHandler config = new ConfigHandler();

    static Configuration configFile;

    public static ConfigCategory general;

    public static void load (FMLPreInitializationEvent event) {
        configFile = new Configuration(new File(event.getModConfigurationDirectory() + "\\simukraft\\", "simukraft.cfg"), Reference.CONFIGURATION_VERSION, false);

        MinecraftForge.EVENT_BUS.register(config);

        syncConfig();
    }

    @SubscribeEvent
    public void update(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MOD_ID)) {
            syncConfig();
        }
    }

    public static void syncConfig() {
        Property prop;

        /* General */
        {
            String cat = "General";
            List<String> propOrder = Lists.newArrayList();
            general = configFile.getCategory(cat);

        }

        if(configFile.hasChanged()) {
            configFile.save();
        }
    }
}

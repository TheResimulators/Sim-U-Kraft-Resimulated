package com.resimulators.simukraft.proxy;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.command.CommandStructure;
import com.resimulators.simukraft.common.entity.entitysim.NameStorage;
import com.resimulators.simukraft.common.entity.entitysim.SimDeath;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.common.entity.player.PlayerFirstJoin;
import com.resimulators.simukraft.common.world.Rent_collection;
import com.resimulators.simukraft.init.*;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by fabbe on 06/01/2018 - 2:43 AM.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        NameStorage.init();
        ModFluids.init();
        ModBlocks.init();
        ModItems.init();
        ModTileEntities.init();
        ModEntities.init();
        PacketHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SimUKraft.instance, new GuiHandler());

        ModOreDict.init();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandStructure());
    }

    public void onServerStarted(FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new SimToHire());
        MinecraftForge.EVENT_BUS.register(new PlayerFirstJoin());
        MinecraftForge.EVENT_BUS.register(new SimDeath());
        MinecraftForge.EVENT_BUS.register(new Rent_collection());
    }
}

package com.resimulators.simukraft.proxy;

import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.resimulators.simukraft.ConfigHandler;
import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.render.RenderSim;
import com.resimulators.simukraft.common.command.CommandStructure;
import com.resimulators.simukraft.common.entity.entitysim.NameStorage;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.common.entity.entitysim.SpecialNameStorage;
import com.resimulators.simukraft.common.entity.player.PlayerFirstJoin;
import com.resimulators.simukraft.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;
import java.net.Proxy;
import java.util.UUID;

/**
 * Created by fabbe on 06/01/2018 - 2:43 AM.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.load(event);
        NameStorage.init();
        SpecialNameStorage.init();
        ModFluids.init();
        ModBlocks.init();
        ModItems.init();
        ModTileEntities.init();
        ModEntities.init();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SimUKraft.instance, new GuiHandler());

        ModOreDict.init();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandStructure());
        RenderSim.initSkinService();
    }

    public void onServerStarted(FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new SimToHire());
        MinecraftForge.EVENT_BUS.register(new PlayerFirstJoin());
    }
}

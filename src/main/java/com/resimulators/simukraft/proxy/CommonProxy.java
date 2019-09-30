package com.resimulators.simukraft.proxy;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.command.CommandStructure;
import com.resimulators.simukraft.common.entity.entitysim.NameStorage;
import com.resimulators.simukraft.common.entity.entitysim.SpecialNameStorage;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.entitysim.SpawnSimEntity;
import com.resimulators.simukraft.common.entity.player.PlayerFirstJoin;
import com.resimulators.simukraft.common.interfaces.CowCapability;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import com.resimulators.simukraft.common.tileentity.Events.TileEntityCreate;
import com.resimulators.simukraft.common.tileentity.TileFarm;
import com.resimulators.simukraft.common.world.Rent_collection;
import com.resimulators.simukraft.common.world.WorldCreationEvent;
import com.resimulators.simukraft.init.*;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.structure.StructureHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by fabbe on 06/01/2018 - 2:43 AM.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        NameStorage.init();
        SpecialNameStorage.init();
        ModFluids.init();
        ModBlocks.init();
        ModItems.init();
        ModTileEntities.init();
        ModEntities.init();
        PacketHandler.init();
        CapabilityManager.INSTANCE.register(CowCapability.class,new CowCapability.Storage(),CowCapability.Impl::new);
        CapabilityManager.INSTANCE.register(PlayerCapability.class,new PlayerCapability.Storage(),PlayerCapability.Impl::new);
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(SimUKraft.instance, new GuiHandler());
        ModOreDict.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        new StructureHandler(event.getServer().getEntityWorld());
        event.registerServerCommand(new CommandStructure());

    }

    public void onServerStarted(FMLServerStartedEvent event) {
        MinecraftForge.EVENT_BUS.register(new SimEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerFirstJoin());
        MinecraftForge.EVENT_BUS.register(new Rent_collection());
        MinecraftForge.EVENT_BUS.register(new SpawnSimEntity());
        MinecraftForge.EVENT_BUS.register(new TileFarm());
        MinecraftForge.EVENT_BUS.register(new TileEntityCreate());

    }
}

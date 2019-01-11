package com.resimulators.simukraft.proxy;

import com.resimulators.simukraft.client.gui.HudGui;
import com.resimulators.simukraft.client.render.RenderParticleEntity;
import com.resimulators.simukraft.client.render.RenderSim;
import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by fabbe on 06/01/2018 - 2:43 AM.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntitySim.class, RenderSim::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityParticleSpawner.class, RenderParticleEntity::new);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new HudGui());
        MinecraftForge.EVENT_BUS.register(new RenderHandEvent());
        RenderSim.initSkinService();
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}

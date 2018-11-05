package com.resimulators.simukraft.init;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by fabbe on 19/01/2018 - 9:34 PM.
 */
public class ModEntities {
    public static void init() {
        createEntity(new ResourceLocation(Reference.MOD_ID + ":sim"), EntitySim.class, "sim", SimUKraft.instance, 0, 64, 1, true, 0x000000, 0x000000);
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID + ":particlespawner"), EntityParticleSpawner.class,"particle spawner", 1,SimUKraft.instance,0,1,false);
    }

    private static void createEntity(ResourceLocation location, Class<? extends Entity> clazz, String name, Object modInstance, int id, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary) {
        net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(location, clazz, name, id, modInstance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
    }
}

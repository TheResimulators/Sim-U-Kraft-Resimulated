package com.resimulators.simukraft.init;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fabbe on 19/01/2018 - 9:34 PM.
 */
public class ModEntities {
    private static int modEntityid = 0;
    public static Set<EntityEntry> ENTITIES = new HashSet<>();
    public static List<String> ENTITY_NAMES = new ArrayList<>();



    private static <T extends Entity> void addEntity(Class<T> entityClass, String name, int eggPrimary, int eggSecondary)
    {
        ENTITIES.add(createBuilder(entityClass, name)
                .egg(eggPrimary, eggSecondary)
                .build());
    }

    private static <T extends Entity> void addEntity(Class<T> entityClass, String name) {
        ENTITIES.add(createBuilder(entityClass,name).build());

    }


    public static void init() {
        addEntity(EntitySim.class, "sim", 0x000000, 0x000000);


    }
    @SideOnly(Side.CLIENT)
    public static void ClientEntitiesinit(){
        addEntity(EntityParticleSpawner.class, "particle_spawner");
    }
    private static EntityEntryBuilder<Entity> createBuilder(Class<? extends Entity> entityClass, String name)
    {
        System.out.println("entity registry id = " + modEntityid);
        String entityName = String.format("%s.%s", Reference.MOD_ID, name);
        System.out.println(entityName);
        ENTITY_NAMES.add(entityName);
        return EntityEntryBuilder.create()
                .entity(entityClass)
                .name(entityName)
                .id(new ResourceLocation(Reference.MOD_ID,name),modEntityid++)
                .tracker(64,1,true);
    }

    public static Set<EntityEntry> getEntities()
    {
        if(ENTITIES.isEmpty()) init();
        return ENTITIES;
    }
}

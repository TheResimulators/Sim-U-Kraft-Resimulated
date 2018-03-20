package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.fml.relauncher.Side.CLIENT;

public class SimToHire {

    public static List<EntitySim> unemployedsims = new ArrayList<>();
    public static List<EntitySim> totalsims = new ArrayList<>();

    Entity name;

    @SubscribeEvent
    @SideOnly(CLIENT)
    public void availableSims(EntityJoinWorldEvent event) {
        World world = Minecraft.getMinecraft().world;
        if (!(event.getEntity() instanceof EntitySim)) {
            return;
        } else {
            if (world != null) {
                if (!world.isRemote) {
                    System.out.println(world);
                    if (!((EntitySim) event.getEntity()).inlist) {
                        System.out.println("adding sim");
                        name = event.getEntity();
                        unemployedsims.add((EntitySim) name);
                        totalsims.add((EntitySim) name);
                        System.out.println("added" + name);
                        ((EntitySim) event.getEntity()).inlist = true;
                        return;
                    }
                }
            }
        }
    }
}
package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class SpawnSimEntity {
    int ticks;

    @SubscribeEvent
    public void Tick(TickEvent.PlayerTickEvent event) {
            World world = event.player.getEntityWorld();
            if (SaveSimData.get(world).isEnabled(event.player.getUniqueID())){
            if (event.phase == TickEvent.Phase.START){
            if (!world.isRemote){
            Random rand = world.rand;
            ticks++;
                EntityPlayer player = event.player;
                if (ticks/20 == 20) {

                    ticks = 0;

                    if (SaveSimData.get(world).getUnemployedSims(SaveSimData.get(world).getPlayerFaction(player.getUniqueID())).size() < 1) {
                        EntitySim entity = new EntitySim(world);
                        entity.setFactionid(SaveSimData.get(world).getPlayerFaction(event.player.getUniqueID()));
                        double entityx = player.posX + rand.nextInt(11)-5;
                        double entityz = player.posZ + rand.nextInt(11)-5;
                        int height = world.getHeight((int)entityx,(int)entityz);
                        entity.setPosition(entityx,height,entityz);
                        entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                        world.spawnEntity(entity);
                        entity.setProfession(rand.nextInt(2));
                        MinecraftForge.EVENT_BUS.post(new LivingSpawnEvent(entity,world,(float)entityx,height,(float)entityz));
                    }
                }
            }

        }}
    }
}
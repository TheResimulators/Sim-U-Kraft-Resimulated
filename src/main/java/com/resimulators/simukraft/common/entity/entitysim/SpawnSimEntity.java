package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SpawnSimEntity {
    int tick;
    private Map<UUID,Integer> ticks= new HashMap<>();
    @SubscribeEvent
    public void Tick(TickEvent.PlayerTickEvent event) {
            World world = event.player.getEntityWorld();

            if (event.phase == TickEvent.Phase.START){

                if (SaveSimData.get(world).isMode(event.player.getUniqueID()) != null){
                    if (!world.isRemote){
                        System.out.println("players mode = " + SaveSimData.get(world).isMode(event.player.getUniqueID()));
                    Random rand = world.rand;
                        EntityPlayer player = event.player;
                            if (ticks.get(player.getUniqueID()) == null){
                                 tick = 0;
                            } else{
                             tick = ticks.get(player.getUniqueID());}
                            tick++;
                            ticks.put(player.getUniqueID(),tick);
                            System.out.println("tick " + tick/20);
                        if (ticks.get(player.getUniqueID())/20 > 5) {
                            ticks.put(player.getUniqueID(),0);
                            System.out.println("players unemployed sims " + SaveSimData.get(world).getUnemployedSims(SaveSimData.get(world).getPlayerFaction(player.getUniqueID())).size());
                            if (SaveSimData.get(world).getUnemployedSims(SaveSimData.get(world).getPlayerFaction(player.getUniqueID())).size() < 1) {
                                EntitySim entity = new EntitySim(world);
                                entity.setFactionid(SaveSimData.get(world).getPlayerFaction(event.player.getUniqueID()));
                                System.out.println("entity faction id " + entity.getFactionId());
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
            }
        }
    }
}
package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.datafix.fixes.SpawnEggNames;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFMLHandledException;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class SpawnSimEntity {
    int tick;
    private Map<UUID,Integer> ticks= new HashMap<>();
    @SubscribeEvent
    public void Tick(TickEvent.PlayerTickEvent event) {
            World world = event.player.getEntityWorld();

            if (event.phase == TickEvent.Phase.START){
                if (event.player.hasCapability(ModCapabilities.PlayerCap,null)){
                if (event.player.getCapability(ModCapabilities.PlayerCap,null).getmode() != -1){
                    if (!world.isRemote){
                    Random rand = world.rand;
                        EntityPlayer player = event.player;
                            if (ticks.get(player.getUniqueID()) == null){
                                 tick = 0;
                            } else{
                             tick = ticks.get(player.getUniqueID());}
                            tick++;
                            ticks.put(player.getUniqueID(),tick);
                        if (ticks.get(player.getUniqueID())/20 > 5) {
                            ticks.put(player.getUniqueID(),0);
                            int size = SaveSimData.get(world).getfaction(event.player.getCapability(ModCapabilities.PlayerCap,null).getfactionid()).getUnemployedSims().size();
                            long factionid = event.player.getCapability(ModCapabilities.PlayerCap,null).getfactionid();
                            FactionData data = SaveSimData.get(world).getfaction(factionid);
                            List<UUID> unemployedsimms =  data.getUnemployedSims();
                            data.getUnemployedSims();
                            if (size < 1) {
                                EntitySim entity = new EntitySim(world);
                                SpawnEggNames name = new SpawnEggNames();
                                entity.setFactionid(factionid);
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
}}
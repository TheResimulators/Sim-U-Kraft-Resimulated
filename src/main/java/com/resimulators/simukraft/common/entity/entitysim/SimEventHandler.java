package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import com.resimulators.simukraft.network.HiredSimDeathPacket;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SimDeathPacket;
import com.resimulators.simukraft.network.SimSpawnPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.*;


public class SimEventHandler {
    private static float credits = 10;

    public static float getCredits() {

        return credits;
    }

    public static float setCredits(float credit) {
        credits = credit;

        return credits;
    }



    @SubscribeEvent
    public void Sim_Spawn(LivingSpawnEvent event) {
        World world = event.getWorld();
        if (event.getEntity() instanceof EntitySim) {
            if (!world.isRemote) {
                UUID id = event.getEntity().getUniqueID();
                if (SaveSimData.get(world) != null) {
                    PlayerCapability capability = Minecraft.getMinecraft().player.getCapability(ModCapabilities.getPlayerCap(),null);
                    Long playerid = capability.getfactionid();
                    FactionData data = SaveSimData.get(Minecraft.getMinecraft().world).getfaction(playerid);
                    if (!data.getTotalSims().contains(id)) {
                        data.addTotalSim(id);
                        data.addUnemployedSim(id);
                    }
                    }
                }
            }
        }

    @SubscribeEvent
    public void Sim_Death(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntitySim) {
            EntitySim sim = (EntitySim) event.getEntity();
            World world = sim.world;
            if (!world.isRemote) {
                UUID id = event.getEntity().getPersistentID();
                int ids = event.getEntity().getEntityId();
                long factionid = sim.getFactionId();
                FactionData data = SaveSimData.get(world).getfaction(factionid);
                data.removeTotalSim(sim);
                data.removeUnemplyedSim(sim);
                for (TileEntity entity : data.getJobblocks()) {
                    ISimJob tile = (ISimJob) entity;
                    if (tile.getId() == id) {
                        tile.setHired(false);
                        tile.setId(null);
                        tile.removeSim(ids);
                        return;

                    }
                }
                //dropping items
                IItemHandler handler = event.getEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                assert handler != null;
                for (int slot = 0; slot < handler.getSlots() - 1; slot++) {
                    ItemStack stack = handler.getStackInSlot(slot);
                    InventoryHelper.spawnItemStack(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, stack);
                }
            }

        }
    }
}
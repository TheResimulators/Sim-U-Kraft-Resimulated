package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.network.HiredSimDeathPacket;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.SimDeathPacket;
import com.resimulators.simukraft.network.SimSpawnPacket;
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
    private static SaveSimData data;
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
                if (SaveSimData.get(world) != null) {
                    if (!SaveSimData.get(world).getTotalSims(((EntitySim) event.getEntity()).getFactionId()).contains(event.getEntity().getUniqueID())) {
                        SaveSimData.get(world).addtotalSim(event.getEntity().getUniqueID(), ((EntitySim) event.getEntity()).getFactionId());
                        SaveSimData.get(world).addUnemployedsim(event.getEntity().getUniqueID(), ((EntitySim) event.getEntity()).getFactionId());
                        SaveSimData.get(world).SendFactionPacket(new SimSpawnPacket(event.getEntity().getUniqueID()), ((EntitySim) event.getEntity()).getFactionId());
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

                SaveSimData.get(world).removeTotalSim(event.getEntity().getUniqueID(),((EntitySim) event.getEntity()).getFactionId());
                SaveSimData.get(world).removeUnemployedSim(event.getEntity().getUniqueID(),((EntitySim) event.getEntity()).getFactionId());
                SaveSimData.get(event.getEntity().world).SendFactionPacket(new SimDeathPacket(ids,((EntitySim) event.getEntity()).getFactionId()),((EntitySim) event.getEntity()).getFactionId());
                for (TileEntity entity : SaveSimData.get(world).getJob_tiles()) {
                    ISimJob tile = (ISimJob) entity;
                    if (tile.getId() == id) {

                        tile.setHired(false);
                        tile.setId(null);
                        tile.removeSim(ids);

                        SaveSimData.get(event.getEntity().world).SendFactionPacket(new HiredSimDeathPacket(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), ids),((EntitySim) event.getEntity()).getFactionId());
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
package com.resimulators.simukraft.common.entity;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.CowCapability;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class FarmAnimalEvents {

    @SubscribeEvent
    public static void CowDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityCow) {
            if (event.getSource().getTrueSource() instanceof EntitySim) {
                if (!event.getEntity().world.isRemote) {
                    EntityCow cow = (EntityCow) event.getEntity();
                    if (cow.hasCapability(ModCapabilities.getCAP(), null)) {
                        if (cow.getCapability(ModCapabilities.getCAP(), null).iscontroledspawn()) {
                            event.setCanceled(true);
                            List<EntityItem> items = event.getDrops();
                            for (EntityItem item : items) {
                                EntitySim sim = ((EntitySim) event.getSource().getTrueSource());
                                for (int i = 0; i < sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getSlots(); i++) {
                                    if (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).isEmpty() || (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getItem().equals(item.getItem().getItem())) && !(sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getCount() + item.getItem().getCount() > 64)) {
                                        if ((sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).equals(item.getItem()))) {
                                            ItemStack stack = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i);
                                            ItemStack mergestack = new ItemStack(stack.getItem(), stack.getCount() + item.getItem().getCount());
                                            sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).insertItem(i, mergestack, true);
                                            break;
                                        }
                                        sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).insertItem(i, item.getItem(), false);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void milkingcooldown(LivingEvent.LivingUpdateEvent event){
        if ((event.getEntity() instanceof EntityCow)){
            if (!event.getEntity().world.isRemote){
                EntityCow cow = (EntityCow) event.getEntity();
                if (cow.hasCapability(ModCapabilities.getCAP(),null)){
                    if (cow.getCapability(ModCapabilities.getCAP(),null) != null){
                        if (cow.getCapability(ModCapabilities.getCAP(),null).iscontroledspawn()){
                        CowCapability cap = Objects.requireNonNull(cow.getCapability(ModCapabilities.getCAP(),null));
                        if (!cap.ismilkable()){
                        if (cap.getCooldown() >= 0 ){
                            cap.resetmilkcooldown();
                            }else cap.decrementMilkcooldown();
                        }}
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void Sheepdrop(LivingDropsEvent event){
        if (!event.getEntity().world.isRemote){
            if (event.getSource().getTrueSource() instanceof EntitySim){
                if (event.getEntity() instanceof EntitySheep){
                    EntitySheep sheep =(EntitySheep) event.getEntity();
                    if (sheep.getEntityData().hasKey("spawned")){
                        if (sheep.getEntityData().getByte("spawned") == (byte) 1){
                        event.setCanceled(true);
                        List<EntityItem> items = event.getDrops();
                        for (EntityItem item : items) {
                            EntitySim sim = ((EntitySim) event.getSource().getTrueSource());
                            for (int i = 0; i < sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH).getSlots(); i++) {
                                if (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).isEmpty() || (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getItem().equals(item.getItem().getItem())) && !(sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).getCount() + item.getItem().getCount() > 64)) {
                                    if ((sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i).equals(item.getItem()))) {
                                        ItemStack stack = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).getStackInSlot(i);
                                        ItemStack mergestack = new ItemStack(stack.getItem(), stack.getCount() + item.getItem().getCount());
                                        sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).insertItem(i, mergestack, true);
                                        break;
                                            }else{
                                        sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH).insertItem(i, item.getItem(), false);
                                        break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void sheepSpawn(LivingSpawnEvent.CheckSpawn event){
        if (!event.getWorld().isRemote){
            if (event.getEntity() instanceof EntitySheep){
                event.getEntity().getEntityData().setByte("spawned",(byte) 0);
            }
        }
    }
}
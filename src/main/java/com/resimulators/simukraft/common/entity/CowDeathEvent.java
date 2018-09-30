package com.resimulators.simukraft.common.entity;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.CowCapability;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class CowDeathEvent {

    @SubscribeEvent
    public static void CowDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityLiving) {
            if (event.getSource().getTrueSource() instanceof EntitySim) {
                if (!event.getEntity().world.isRemote) {
                    EntityLiving cow = (EntityCow) event.getEntity();
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
    public void milkingcooldown(LivingEvent.LivingUpdateEvent event){
        if (!(event.getEntity() instanceof EntityCow)){
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
}
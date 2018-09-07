package com.resimulators.simukraft.common.entity;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CowDeathEvent {

    @SubscribeEvent
    public static void CowDeath(LivingDropsEvent event){
        if (event.getEntity() instanceof EntityCow){
            if (!event.getEntity().world.isRemote){
                EntityCow cow = (EntityCow) event.getEntity();
                if (cow.hasCapability(ModCapabilities.getCAP(),null)){
                if(cow.getCapability(ModCapabilities.getCAP(),null).iscontroledspawn()){
                    event.setCanceled(true);
                    List<EntityItem> items = event.getDrops();
                    System.out.println("items " + items);
                    for (EntityItem item :items){
                        EntitySim sim = ((EntitySim)event.getSource().getTrueSource());
                        for (int i = 0;i<sim.gethandler().getSlots();i++){
                            System.out.println(sim.gethandler().getStackInSlot(i).getItem());
                            System.out.println(item.getItem().getItem());
                            if (sim.gethandler().getStackInSlot(i).isEmpty() || (sim.gethandler().getStackInSlot(i).getItem().equals(item.getItem().getItem())) && sim.gethandler().getStackInSlot(i).getCount() != 64){
                                if ((sim.gethandler().getStackInSlot(i).equals(item.getItem()))){
                                    ItemStack stack  = sim.gethandler().getStackInSlot(i);
                                    ItemStack mergestack = new ItemStack(stack.getItem(),stack.getCount() + item.getItem().getCount());
                                    sim.gethandler().insertItem(i,mergestack,true);
                                }
                                sim.gethandler().insertItem(i,item.getItem(),false);
                                System.out.println("adding items");
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

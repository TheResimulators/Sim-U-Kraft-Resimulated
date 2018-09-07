package com.resimulators.simukraft.common.capabilities;

import com.resimulators.simukraft.common.interfaces.CowCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModCapabilities {


    @CapabilityInject(CowCapability.class)
    private static Capability<CowCapability> CAP = null;

    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof EntityCow){
            System.out.println("adding capability");
            event.addCapability(CowCapability.RL,new CowCapability.Provider(CAP));
        }
    }


    public static Capability<CowCapability> getCAP() {
        return CAP;
    }
}

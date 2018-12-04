package com.resimulators.simukraft;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.interfaces.CowCapability;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityHandler {




    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof EntityCow){
            event.addCapability(CowCapability.RL,new CowCapability.Provider(ModCapabilities.getCAP()));
        }
        if (event.getObject() instanceof EntityPlayer){
            event.addCapability(PlayerCapability.RL,new PlayerCapability.Provider(ModCapabilities.getPlayerCap()));
        }

    }
}

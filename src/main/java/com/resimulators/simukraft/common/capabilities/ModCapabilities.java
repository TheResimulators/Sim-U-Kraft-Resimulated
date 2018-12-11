package com.resimulators.simukraft.common.capabilities;

import com.resimulators.simukraft.common.interfaces.CowCapability;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModCapabilities {


    @CapabilityInject(CowCapability.class)
    public static Capability<CowCapability> CAP = null;

    @CapabilityInject(PlayerCapability.class)
    public static Capability<PlayerCapability> PlayerCap = null;
}

package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.PlayerUpdatePacket;
import com.sun.glass.ui.View;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PlayerJoinDataEvent {
    static private Random rnd = new Random();
    @SubscribeEvent
    public static void Player_Join(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote) {
            if (event.player.hasCapability(ModCapabilities.PlayerCap,null)){
                System.out.println(event.player.getCapability(ModCapabilities.PlayerCap, null));
            Long factionid = event.player.getCapability(ModCapabilities.PlayerCap,null).getfactionid();
            System.out.println("faction id " + factionid);
            PacketHandler.INSTANCE.sendTo(new PlayerUpdatePacket(SaveSimData.get(event.player.world).getfaction(factionid).getTotalSims(), SaveSimData.get(event.player.world).getfaction(factionid).getUnemployedSims(), SimEventHandler.getCredits(),factionid,event.player.getCapability(ModCapabilities.PlayerCap,null).getmode()), (EntityPlayerMP) event.player);
        }
    }
}}


package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.PlayerUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
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
            Long factionid = SaveSimData.get(event.player.world).getPlayerFaction(event.player.getUniqueID());
            System.out.println("faction id " + factionid);
            PacketHandler.INSTANCE.sendTo(new PlayerUpdatePacket(SaveSimData.get(event.player.world).getTotalSims(factionid), SaveSimData.get(event.player.world).getUnemployedSims(factionid), SimEventHandler.getCredits(),factionid), (EntityPlayerMP) event.player);
        }
    }
}


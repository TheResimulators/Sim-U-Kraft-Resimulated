package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.PlayerUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerJoinDataEvent {

    @SubscribeEvent
    public void Player_Join(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote) {
            PacketHandler.INSTANCE.sendTo(new PlayerUpdatePacket(SimEventHandler.getWorldSimData().getTotalSims(), SimEventHandler.getWorldSimData().getUnemployed_sims(), SimEventHandler.getCredits()), (EntityPlayerMP) event.player);
            System.out.println("Data sent");
        }
    }}

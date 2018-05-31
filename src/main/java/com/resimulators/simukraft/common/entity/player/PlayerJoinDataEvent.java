package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.PlayerUpdatePacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerJoinDataEvent {

    @SubscribeEvent
    public static void Player_Join(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP){
        if (!event.getWorld().isRemote) {
            PacketHandler.INSTANCE.sendTo(new PlayerUpdatePacket(SimEventHandler.getWorldSimData().getTotalSims(), SimEventHandler.getWorldSimData().getUnemployed_sims(), SimEventHandler.getCredits()), (EntityPlayerMP) event.getEntity());
            System.out.println("Data sent");
        }}
    }}

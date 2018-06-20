package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.PlayerUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
@Mod.EventBusSubscriber
public class PlayerJoinDataEvent {

    @SubscribeEvent
    public static void Player_Join(PlayerEvent.PlayerLoggedInEvent event) {

        System.out.println("Player joined");
        if (!event.player.world.isRemote) {
            System.out.println(SaveSimData.get(event.player.world));
            System.out.println(SaveSimData.get(event.player.world).getTotalSims());
            System.out.println(SaveSimData.get(event.player.world).getUnemployed_sims());
            System.out.println(SimEventHandler.getCredits());

            PacketHandler.INSTANCE.sendTo(new PlayerUpdatePacket(SaveSimData.get(event.player.world).getTotalSims(), SaveSimData.get(event.player.world).getUnemployed_sims(), SimEventHandler.getCredits()), (EntityPlayerMP) event.player);
            System.out.println("Data sent");
        }}
    }


package com.resimulators.simukraft.common.entity.player;

import com.jcraft.jogg.Packet;
import com.resimulators.simukraft.common.FactionData;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import com.resimulators.simukraft.network.*;
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
            Random rnd = new Random();
            if (event.player.hasCapability(ModCapabilities.PlayerCap,null)){
            Long factionid = event.player.getCapability(ModCapabilities.PlayerCap,null).getfactionid();
            if (factionid == 0){
                long id = rnd.nextLong();
                event.player.getCapability(ModCapabilities.PlayerCap,null).setfaction(id);
                SaveSimData.get(event.player.world).addfaction(id,event.player.getName());
                SaveSimData.get(event.player.world).getfaction(id).addPlayer(event.player.getUniqueID());

                factionid = id;
                FactionData data = SaveSimData.get(event.player.world).getfaction(factionid);
                data.sendFactionPacket(new FactionCreatedPacket(id,event.player.getName()));
            }
            FactionData data = SaveSimData.get(event.player.world).getfaction(factionid);
            if (SaveSimData.get(event.player.world) == null)
                return;
                event.player.getCapability(ModCapabilities.PlayerCap,null).updateClient(event.player);
                PacketHandler.INSTANCE.sendTo(new SaveSimDataUpdatePacket(SaveSimData.get(event.player.world).serializeNBT()),(EntityPlayerMP) event.player);
            PacketHandler.INSTANCE.sendTo(new PlayerUpdatePacket(SaveSimData.get(event.player.world).getfaction(factionid).getTotalSims(), SaveSimData.get(event.player.world).getfaction(factionid).getUnemployedSims(), SimEventHandler.getCredits(),factionid,event.player.getCapability(ModCapabilities.PlayerCap,null).getmode()), (EntityPlayerMP) event.player);
            PacketHandler.INSTANCE.sendToServer(new UpdateClientFactionPacket(data.serializeNBT()));
        }
    }
}}


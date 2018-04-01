package com.resimulators.simukraft.common.world;

import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.network.Credits_packets;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Rent_collection{
    float credits;
    @SubscribeEvent
    protected void Collect_Rent(WorldEvent event){
        World world = event.getWorld();
        float rent = 4;
        credits = SimToHire.getCredits();
        long time = world.getWorldTime();
        if (0 ==(time % 2400)){
            credits = credits + rent;
            SimToHire.setCredits(credits);
            System.out.println("Wakey wakey you just gained rent of " + rent + "to have a total of " + credits + " credits");
            PacketHandler.INSTANCE.sendToServer(new Credits_packets());
        }

    }
}
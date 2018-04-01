package com.resimulators.simukraft.common.world;

import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.network.Credits_packets;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Rent_collection{
    boolean rent_paid = true;
    float credits;
    @SubscribeEvent
    protected void Collect_Rent(TickEvent.WorldTickEvent event){
        World world = event.world;
        float rent = 4;
        if (!world.isRemote){
        credits = SimToHire.getCredits();
        long time = world.getWorldTime();
        if (time == 0 && rent_paid == false){

            credits = credits + rent;
            SimToHire.setCredits(credits);
            System.out.println("Wakey wakey you just gained rent of " + rent + "to have a total of " + credits + " credits " + rent_paid);
            PacketHandler.INSTANCE.sendToAll(new Credits_packets());
            rent_paid = true;
            } else {rent_paid = false;}

        }
    }
}
package com.resimulators.simukraft.common.world;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.network.CreditsPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Rent_collection {
    boolean rent_paid = true;
    float credits;

    @SubscribeEvent
    protected void Collect_Rent(TickEvent.WorldTickEvent event) {
        World world = event.world;
        float rent = 4;
        if (!world.isRemote) {
            credits = SimEventHandler.getCredits();
            long time = world.getWorldTime() % 24000;
            if (time == 0 && !rent_paid) {
                credits = credits + rent;
                SimEventHandler.setCredits(credits);
                System.out.println("Wakey wakey you just gained rent of " + rent + "to have a total of " + credits + " credits " + rent_paid);
                PacketHandler.INSTANCE.sendToAll(new CreditsPacket());
                rent_paid = true;
            }
            if (time != 0) {
                rent_paid = false;
            }
        }
    }
}
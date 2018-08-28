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

    protected void Collect_Rent(TickEvent.WorldTickEvent event) {
        World world = event.world;
        float rent = 4;
        if (!world.isRemote) {
            credits = SimEventHandler.getCredits();
            long time = world.getWorldTime() % 24000;
            if (time == 1 && !rent_paid) {
                credits = credits + rent;
                SimEventHandler.setCredits(credits);
                PacketHandler.INSTANCE.sendToAll(new CreditsPacket());
                rent_paid = true;
                System.out.println("is this happening twice on the server");
            }
            if (time > 1) {
                rent_paid = false;
            }
        }
    }
}
package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class Hiring_handler  implements IMessageHandler<Hiring_packet, IMessage> {

    @Override public IMessage onMessage(Hiring_packet message, MessageContext ctx){
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
                UUID Id = message.sims;
                System.out.println(" removing sim " + Id);
                EntitySim e = (EntitySim) ctx.getServerHandler().player.getServerWorld().getEntityFromUuid(Id);
                SimToHire.unemployedsims.remove(e);
                e.setProfession(message.job);
        }); return null;
    }
}
        //System.out.println("incoming value =:" + message.credit);
        //System.out.println("Credits equal: " + SimToHire.getCredits());



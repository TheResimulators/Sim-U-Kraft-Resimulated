package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TriggerRefreshHandler implements IMessageHandler<TriggerRefreshPacket,RefreshPacket> {

    @Override public RefreshPacket onMessage(TriggerRefreshPacket message, MessageContext ctx){
        PacketHandler.INSTANCE.sendToAll(new RefreshPacket());
        return null;

    }

}

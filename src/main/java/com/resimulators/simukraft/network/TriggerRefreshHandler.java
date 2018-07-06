package com.resimulators.simukraft.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TriggerRefreshHandler implements IMessageHandler<TriggerRefreshPacket,RefreshPacket> {
    @Override
    public RefreshPacket onMessage(TriggerRefreshPacket message, MessageContext ctx) {
        PacketHandler.INSTANCE.sendToAll(new RefreshPacket());
        return null;
    }
}

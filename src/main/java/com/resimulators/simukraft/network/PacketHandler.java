package com.resimulators.simukraft.network;

import com.resimulators.simukraft.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by fabbe on 16/02/2018 - 11:14 PM.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
    public static void init() {
        INSTANCE.registerMessage(Credits_packet_handler.class,Credits_packets.class,0, Side.CLIENT);
        INSTANCE.registerMessage(Credits_packet_handler.class,Credits_packets.class,1,Side.SERVER);

    }
}

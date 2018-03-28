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
        INSTANCE.registerMessage(Credits_Handler.class,Credits_packets.class,0,Side.SERVER);
        INSTANCE.registerMessage(Siminfo_handler.class,Siminfo_packet.class,1,Side.SERVER);
        INSTANCE.registerMessage(SimDeath_handler.class,SimDeath_packet.class,2,Side.SERVER);
    }

}


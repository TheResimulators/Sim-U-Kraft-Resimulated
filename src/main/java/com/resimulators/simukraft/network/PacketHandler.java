package com.resimulators.simukraft.network;

import com.resimulators.simukraft.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * Created by fabbe on 16/02/2018 - 11:14 PM.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void init() {

    }
}

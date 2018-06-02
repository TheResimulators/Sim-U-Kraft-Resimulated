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
        INSTANCE.registerMessage(CreditsHandler.class,CreditsPacket.class,0,Side.SERVER);
        INSTANCE.registerMessage(SimSpawnHandler.class,SimSpawnPacket.class,1,Side.SERVER);
        INSTANCE.registerMessage(SimDeathHandler.class,SimDeathPacket.class,2,Side.SERVER);
        INSTANCE.registerMessage(HiringHandler.class,HiringPacket.class,3,Side.SERVER);
        INSTANCE.registerMessage(HiringHandler.class,HiringPacket.class,4,Side.CLIENT);
        INSTANCE.registerMessage(TriggerRefreshHandler.class,TriggerRefreshPacket.class,5,Side.SERVER);
        INSTANCE.registerMessage(RefreshHandler.class,RefreshPacket.class,6,Side.CLIENT);
        INSTANCE.registerMessage(PlayerUpdateHandler.class,PlayerUpdatePacket.class ,7,Side.CLIENT);

    }

}


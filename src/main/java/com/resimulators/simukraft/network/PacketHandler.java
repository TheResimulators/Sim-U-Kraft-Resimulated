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
        INSTANCE.registerMessage(CreditsHandler.class,CreditsPacket.class,0,Side.CLIENT);
        INSTANCE.registerMessage(SimSpawnHandler.class,SimSpawnPacket.class,1,Side.CLIENT);
        INSTANCE.registerMessage(SimDeathHandler.class,SimDeathPacket.class,2,Side.CLIENT);
        INSTANCE.registerMessage(HiringHandler.class,HiringPacket.class,3,Side.SERVER);
        INSTANCE.registerMessage(HiringHandler.class,HiringPacket.class,4,Side.CLIENT);
        INSTANCE.registerMessage(TriggerRefreshHandler.class,TriggerRefreshPacket.class,5,Side.SERVER);
        INSTANCE.registerMessage(RefreshHandler.class,RefreshPacket.class,6,Side.CLIENT);
        INSTANCE.registerMessage(PlayerUpdateHandler.class,PlayerUpdatePacket.class ,7,Side.CLIENT);
        INSTANCE.registerMessage(GetSimIdHandler.class,GetSimIdPacket.class,8,Side.SERVER);
        INSTANCE.registerMessage(ReturnSimIdHandler.class,ReturnSimIdPacket.class,9,Side.CLIENT);
        INSTANCE.registerMessage(HiredSimDeathHandler.class,HiredSimDeathPacket.class,10,Side.CLIENT);
        INSTANCE.registerMessage(UpdateJobIdHandler.class,UpdateJobIdPacket.class,11,Side.SERVER);
        INSTANCE.registerMessage(ReturnUpdateSimIdHandler.class,ReturnUpdateSimIdPacket.class,12,Side.CLIENT);
        INSTANCE.registerMessage(ClientHireHandler.class,ClientHirePacket.class,13,Side.CLIENT);
        INSTANCE.registerMessage(FireSimHandler.class,FireSimPacket.class,14,Side.CLIENT);
        INSTANCE.registerMessage(FireSimTriggerPacketHandler.class,FireSimTriggerPacket.class,15,Side.SERVER);
        INSTANCE.registerMessage(SimInvHandler.class,SimInvPacket.class,16,Side.SERVER);
    }

}


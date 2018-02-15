package com.resimulators.simukraft.common.entity.entitysim;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SimToHire {
    private EntitySim entitySim;
    private String simName;
    EntityPlayer player ;
    @SubscribeEvent
    public void Avalablesims(LivingSpawnEvent event){
        if (!(event.getEntity() instanceof EntitySim)){
            return;

        }
    }
}
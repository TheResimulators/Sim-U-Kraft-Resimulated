package com.resimulators.simukraft.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class TeleportParticle extends Particle{
    public TeleportParticle(World p_i1219_1_, double p_i1219_2_, double p_i1219_4_, double p_i1219_4_2, double p_i1219_6_, double p_i1219_6_2, double p_i1219_8_) {
        super(p_i1219_1_, p_i1219_2_, p_i1219_4_, p_i1219_4_2, p_i1219_6_, p_i1219_6_2, p_i1219_8_);
        motionX = 0;
        motionZ = 0;
        motionY = p_i1219_6_2;
        this.setMaxAge(40);
        this.setRBGColorF(148,0,211);
        particleScale = 3;
        System.out.println("is this being called");
    }


    @Override
    public void onUpdate(){
        super.onUpdate();
    }
}

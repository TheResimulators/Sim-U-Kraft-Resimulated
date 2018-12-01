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
        float lvt_14_1_ = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
        this.particleRed = lvt_14_1_ * 0.9F;
        this.particleGreen = lvt_14_1_ * 0.3F;
        this.particleBlue = lvt_14_1_;
        particleScale = 3;
        System.out.println("is this being called");
    }


    @Override
    public void onUpdate(){
        super.onUpdate();
    }
}

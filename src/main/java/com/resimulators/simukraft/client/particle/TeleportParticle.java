package com.resimulators.simukraft.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class TeleportParticle extends ParticlePortal{
    public TeleportParticle(World p_i1219_1_, double p_i1219_2_, double p_i1219_4_, double p_i1219_4_2, double p_i1219_6_, double p_i1219_6_2, double p_i1219_8_) {
        super(p_i1219_1_, p_i1219_2_, p_i1219_4_, p_i1219_4_2, p_i1219_6_, p_i1219_6_2, p_i1219_8_);
        this.motionX = 0;
        this.motionZ = 0;
        this.particleMaxAge += 60;
    }
    @Override
    public void onUpdate(){
        this.posY = this.posY + this.motionY;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
    }
}

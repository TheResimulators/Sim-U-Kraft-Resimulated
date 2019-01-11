package com.resimulators.simukraft.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class TeleportParticle extends ParticlePortal{
    private final float portalParticleScale;
    private final double portalPosX;
    private final double portalPosY;
    private final double portalPosZ;

    public TeleportParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX = xSpeedIn;
        this.motionY = ySpeedIn;
        this.motionZ = zSpeedIn;
        this.posX = xCoordIn;
        this.posY = yCoordIn;
        this.posZ = zCoordIn;
        this.portalPosX = this.posX;
        this.portalPosY = this.posY;
        this.portalPosZ = this.posZ;
        this.particleMaxAge += 60;
        this.particleScale = this.rand.nextFloat() * 0.6F + 6;
        System.out.println("particle scale " + particleScale);
        this.portalParticleScale = this.particleScale;

    }
    @Override
    public void onUpdate(){
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        this.posY = this.portalPosY + this.motionY*(f*((particleAge/3)*2));

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        float f = ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge + 5;
        f = 1.0F - f;
        f = f * f;
        f = 1.0F + f;
        this.particleScale = this.portalParticleScale * f + 200;
        System.out.println("particle scale when rendering " + particleScale);
        this.width = particleScale;
        this.height = particleScale;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    }

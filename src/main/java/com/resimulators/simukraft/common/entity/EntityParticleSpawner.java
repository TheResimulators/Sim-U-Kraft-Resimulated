package com.resimulators.simukraft.common.entity;


import com.resimulators.simukraft.client.particle.TeleportParticle;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.UUID;

public class EntityParticleSpawner extends Entity {
    private EntitySim sim;
    private boolean teleport;
    private int particlecooldown;
    public EntityParticleSpawner(World worldIn, EntitySim sim) {
        super(worldIn);
        this.sim = sim;
        System.out.println("particle spawner is spawned");
    }
    public EntityParticleSpawner(World world){
        super(world);
    }
    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        UUID simid = compound.getUniqueId("sim id");
        if (simid != null) this.sim = (EntitySim) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(simid);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setUniqueId("sim id",sim.getUniqueID());

    }

    public void setTeleport(boolean teleport){
        this.teleport = teleport;
    }

    public boolean isTeleport(){
        return teleport;
    }


    @Override
    public void onUpdate(){
        if (!sim.getTeleport()){
            world.removeEntity(this);
        }

        if (world.isRemote){
            if (particlecooldown <= 0){
                int num = rand.nextInt(10) + 5;
                for (int i = 0;i<num;i++){
                    double simposx = posX + rand.nextFloat()-0.5;
                    double simposz = posZ + rand.nextFloat()-0.5;
                    particlecooldown = 3;
                    Minecraft.getMinecraft().effectRenderer.addEffect(new TeleportParticle(world,simposx,posY+ 2.5d + rand.nextFloat()-0.5,simposz));
                }}else{particlecooldown--;}
        }
    }
}

package com.resimulators.simukraft.common.entity;


import com.resimulators.simukraft.client.particle.TeleportParticle;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.ClientTeleportPacket;
import com.resimulators.simukraft.network.PacketHandler;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.UUID;

public class EntityParticleSpawner extends Entity {
    private EntitySim sim;
    private boolean teleport;
    private int particlecooldown;
    public EntityParticleSpawner(World worldIn, EntitySim sim) {
        super(worldIn);
        this.sim = sim;

    }
    public EntityParticleSpawner(World world){
        super(world);
    }


    @Override
    protected void entityInit() {

    }
    public void updateClient(){
        System.out.println(sim);
        System.out.println(this);
        PacketHandler.INSTANCE.sendToAllAround(new ClientTeleportPacket(sim.getEntityId(),this.getEntityId()),new NetworkRegistry.TargetPoint(sim.dimension,sim.posX,sim.posY,sim.posZ,128));
        System.out.println("particle spawner is spawned");
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

    public void setSim(EntitySim sim){
        this.sim = sim;
    }
    @Override
    public void onUpdate(){

        if (!world.isRemote){
            if (sim != null){
        if (!sim.getTeleport() || !sim.isParticlspawning() || sim.getTeleporttarget() == null){
            world.removeEntity(this);
        }}}

        if (world.isRemote){

            if (particlecooldown <= 0){
                particlecooldown = 3;
                int num = rand.nextInt(10) + 5;
                for (int i = 0;i<num;i++){
                    double posx = posX + rand.nextFloat()-0.5;
                    double posz = posZ + rand.nextFloat()-0.5;
                    Minecraft.getMinecraft().effectRenderer.addEffect(new TeleportParticle(world,posx,posY+ 3d + rand.nextFloat()-0.5,posz,0,-0.075,0));
                    if (sim != null){
                    float simposx = (float) sim.getPosition().getX() + rand.nextFloat();
                    float simposy = (float) sim.getPosition().getY()+ 3 + rand.nextFloat() - 0.5f;
                    float simposz = (float) sim.getPosition().getZ() + rand.nextFloat();
                    Minecraft.getMinecraft().effectRenderer.addEffect(new TeleportParticle(world,simposx,simposy,simposz,0,-0.075,0));}
                }}else{particlecooldown--;}
        }
    }
}

package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import com.resimulators.simukraft.network.GetSimIdPacket;
import com.resimulators.simukraft.network.HiredSimDeathPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.*;

public class TileFarm extends TileEntity implements ITickable,iSimJob {
    private boolean building;
    protected int width = 5;
    protected int length = 5;
    private String profession = "Farmer";
    private UUID id;
    private int professionint = 2;
    private Boolean hired = false;
    private Set<Integer> sims = new HashSet<>();
    private List<String> sims_name = new ArrayList<>();

    public void CreateFarm() {}

    public void update() {
    if (!building) {
        CreateFarm();
        building = true;
        }

    }
    public TileFarm(){}
    @Override
    public int getProfessionint(){return professionint;}
    @Override
    public void setHired(boolean hired){
        this.hired = hired;
    }
    @Override
    public boolean getHired()
    {
        return this.hired;
    }
    @Override
    public String getProfession(){return profession;}

    @Override
    public void removeSimName(String name)
    {
        sims_name.remove(name);
    }

    @Override
    public void addSimName(String name)
    {
        sims_name.add(name);
    }

    @Override
    public void addSim(int sim) {
        sims.add(sim);
    }

    @Override
    public List<String> getnames()
    {
        return sims_name;
    }


    @Override
    public void removeSim(int sim){sims.remove(sim);}
    @Override
    public Set<Integer> getSims()
    {
        return sims;
    }

    @Nullable
    public void setId(UUID id)
    {
        this.id = id;
    }
    @Override
    public UUID getId()
    {
        return id;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        setHired(nbt.getBoolean("hired"));
        professionint = nbt.getInteger("profession");
        if (nbt.hasKey("id")){
        id = nbt.getUniqueId("id");}
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("hired",getHired());
        nbt.setInteger("profession" , professionint);
        if (id != null){
        nbt.setUniqueId("id",id);}
        return super.writeToNBT(nbt);
    }

    public void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn){
        if (getHired())
        {
            playerIn.openGui(SimUKraft.instance, GuiHandler.GUI_FARM, worldIn, pos.getX(), pos.getY(), pos.getZ());
        } else
            {
                PacketHandler.INSTANCE.sendToServer(new GetSimIdPacket(pos.getX(),pos.getY(),pos.getZ()));

            }
    }}

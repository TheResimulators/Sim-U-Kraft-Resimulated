package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.interfaces.CowCapability;
import com.resimulators.simukraft.common.interfaces.ISimIndustrial;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.network.GetSimIdPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class TileCattle extends TileEntity implements ITickable,ISimIndustrial {

    private boolean building;
    protected int width = 5;
    protected int length = 5;
    private String profession = "Cattle Farm";
    private UUID id;
    private String simname;
    private int professionint = 5;
    private Boolean hired = false;
    private Set<Integer> sims = new HashSet<>();
    private List<String> sims_name = new ArrayList<>();

    public void update() {
     if (!world.isRemote) {
         if (getHired()) {
             if (world.getWorldTime() % 20 == 0) {
                 int numCows = world.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(pos.getX() - 4, pos.getY(), pos.getZ() - 4, pos.getX() + 4, pos.getY() + 2, pos.getZ() + 4)).size();
                 for (int i = numCows; i < 5; i++) {
                     EntityCow cow = new EntityCow(world);
                     attachCapability(cow);
                     cow.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
                     world.spawnEntity(cow);
                    }
                }
            }
        }
    }

    private void attachCapability(EntityCow cow){
        if (cow.hasCapability(ModCapabilities.getCAP(),null)){
            cow.getCapability(ModCapabilities.getCAP(),null).setcontroledspawn();
            System.out.println("this is being reached");
        }

    }

    @Override
    public int getProfessionint() {
        return professionint;
    }

    @Override
    public void setHired(boolean hired) {
        this.hired = hired;
        markDirty();
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
        markDirty();
    }

    @Override
    public boolean getHired() {
        return this.hired;
    }

    @Override
    public String getProfession() {
        return profession;
    }
    @Override
    public void addSimName(String name) {
        sims_name.add(name);
    }

    @Override
    public void addSim(int sim) {
        sims.add(sim);
    }

    @Override
    public List<String> getnames() {
        return sims_name;
    }

    @Override
    public Set<Integer> getSims() {
        return sims;
    }

    @Override
    public void removeSim(int sim) {
        sims.remove(sim);
        markDirty();
    }

    @Override
    public void removeSimName(String name) {
        sims_name.remove(name);
        markDirty();
    }

    @Nullable
    public void addId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        System.out.println("hired " + getHired());
        PacketHandler.INSTANCE.sendToServer(new GetSimIdPacket(pos.getX(), pos.getY(), pos.getZ(),GuiHandler.GUI_CATTLE));


        }
    @Override
    public void setSimname(int id){
        simname = Minecraft.getMinecraft().world.getEntityByID(id).getName();
        System.out.println("sim name " + simname);
        markDirty();

    }

    public String getSimname(){
        return simname;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setBoolean("hired", getHired());
        nbt.setInteger("profession", professionint);

        System.out.println("sim name writing" + simname);
        if (simname!= null) nbt.setString("sim name",simname);
        if (id != null)nbt.setUniqueId("sim id",id);

        return nbt;
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        setHired(nbt.getBoolean("hired"));
        professionint = nbt.getInteger("profession");
        if (nbt.hasKey("sim name"))simname = nbt.getString("sim name");
        if (nbt.hasKey("sim id")) id = nbt.getUniqueId("sim id");
        System.out.println("sim name after reading " + simname);
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos,0,writeToNBT(getUpdateTag()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());

    }

}

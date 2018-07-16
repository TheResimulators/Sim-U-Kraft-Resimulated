package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.network.GetSimIdPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class TileFarm extends TileEntity implements ITickable,ISimJob {
    private boolean building;
    protected int width = 5;
    protected int length = 5;
    private String profession = "Farmer";
    private UUID id;
    private int professionint = 2;
    private Boolean hired = false;
    private Set<Integer> sims = new HashSet<>();
    private List<String> sims_name = new ArrayList<>();
    private int seed;

    public void CreateFarm() {
    }

    public void update() {
        if (!building) {
            CreateFarm();
            building = true;
        }
    }

    public TileFarm() {
    }

    @Override
    public int getProfessionint() {
        return professionint;
    }

    @Override
    public void setHired(boolean hired) {
        this.hired = hired;
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
    public void removeSimName(String name) {
        sims_name.remove(name);
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
    public void removeSim(int sim) {
        sims.remove(sim);
    }

    @Override
    public Set<Integer> getSims() {
        return sims;
    }

    @Nullable
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        setHired(nbt.getBoolean("hired"));
        professionint = nbt.getInteger("profession");
        seed = nbt.getInteger("seed");
        if (nbt.hasKey("id")) {
            id = nbt.getUniqueId("id");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("hired", getHired());
        nbt.setInteger("profession", professionint);
        nbt.setInteger("seed", seed);
        if (id != null) {
            nbt.setUniqueId("id", id);
        }
        return nbt;
    }

    public void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (getHired()) {
            playerIn.openGui(SimUKraft.instance, GuiHandler.GUI_FARM, worldIn, pos.getX(), pos.getY(), pos.getZ());
        } else {
            PacketHandler.INSTANCE.sendToServer(new GetSimIdPacket(pos.getX(), pos.getY(), pos.getZ()));

        }
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos,1,writeToNBT(getUpdateTag()));
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());

    }
}

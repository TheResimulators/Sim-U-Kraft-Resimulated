package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.tileentity.base.TileBuilderBase;
import com.resimulators.simukraft.network.GetSimIdPacket;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.ServerStructurePacket;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Astavie on 25/01/2018 - 5:05 PM.
 */
public class TileConstructor extends TileBuilderBase implements ITickable,ISimJob {
	public boolean building;
	private String profession = "Builder";
	private UUID id;
	private int professionint = 1;
	private Boolean hired = false;
	private Set<Integer> sims = new HashSet<>();
	private List<String> sims_name = new ArrayList<>();
	private EnumFacing facing;
	private BlockPos posA;
	private BlockPos posB;
	private boolean render;
	@Override
	public void update() {

		if (building)
			if (isFinished()){
			    progress = 0;
				building = false;}
			else
				build();
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
	public UUID getId() {
		return this.id;
	}

	@Override
	public boolean getHired() {
		return hired;
	}

	@Override
	public String getProfession() {
		return profession;
	}

	@Override
	public int getProfessionID() {
		return professionint;
	}

	@Override
	public void addSim(int sim) {
		sims.add(sim);
        markDirty();
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

	@Override
	public void addSimName(String name) {
		sims_name.add(name);
        markDirty();
	}

	@Override
	public List<String> getnames() {
		return sims_name;
	}

	public void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn){
		if (getHired()) {
			PacketHandler.INSTANCE.sendToServer(new ServerStructurePacket(pos.getX(),pos.getY(),pos.getZ()));
		} else{
			PacketHandler.INSTANCE.sendToServer(new GetSimIdPacket(pos.getX(),pos.getY(),pos.getZ(),GuiHandler.GUI.HIRED.ordinal()));

		}
	}

	public void setfacing(EnumFacing facing){
		this.facing = facing;
	}

	public EnumFacing getFacing(){
		return this.facing;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		setHired(nbt.getBoolean("hired"));
		professionint = nbt.getInteger("profession");
		if (nbt.hasKey("id")) {
			id = nbt.getUniqueId("id");
		}
		render = nbt.getBoolean("render");
		posA = BlockPos.fromLong(nbt.getLong("posA"));
		posB = BlockPos.fromLong(nbt.getLong("PosB"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
		nbt.setBoolean("hired", getHired());
		nbt.setInteger("profession", professionint);
		if (id != null) {
			nbt.setUniqueId("id", id);
		}
		nbt.setBoolean("render",render);
		nbt.setLong("posA",posA.toLong());
		nbt.setLong("posB",posB.toLong());


		return nbt;
	}
	
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos,0,writeToNBT(getUpdateTag()));
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
	    readFromNBT(packet.getNbtCompound());

    }


    public void setPosA(BlockPos pos){
		posA = pos;
	}

	public void setPosB(BlockPos pos){
		posB = pos;
	}

	public BlockPos getPosA(){
		return posA;
	}

	public BlockPos getPosB(){
		return posB;
	}
	public void setrender(boolean bool){
		render = bool;
		System.out.println(render);

	}

	public boolean isRender(){
		return render;
	}
}
package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.tileentity.base.TileBuilderBase;
import com.resimulators.simukraft.network.GetSimIdPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

/**
 * Created by Astavie on 25/01/2018 - 5:05 PM.
 */
public class TileConstructor extends TileBuilderBase implements ITickable,ISimJob {
	public boolean building;
	private String profession = "Farmer";
	private UUID id;
	private int professionint = 1;
	private Boolean hired = false;
	private Set<Integer> sims = new HashSet<>();
	private List<String> sims_name = new ArrayList<>();
	@Override
	public void update() {
		if (building)
			if (isFinished())
				building = false;
			else
				build();
	}

	@Override
	public void setHired(boolean hired) {
		this.hired = hired;
	}

	@Override
	public void setId(UUID id) {
		this.id = id;
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
	public int getProfessionint() {
		return professionint;
	}

	@Override
	public void addSim(int sim) {
		sims.add(sim);
	}

	@Override
	public Set<Integer> getSims() {
		return sims;
	}

	@Override
	public void removeSim(int sim) {
		sims.remove(sim);
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
	public List<String> getnames() {
		return sims_name;
	}

	public void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn){
		if (getHired())
		{
			playerIn.openGui(SimUKraft.instance, GuiHandler.GUI_BUILDER, worldIn, pos.getX(), pos.getY(), pos.getZ());
		} else
		{
			PacketHandler.INSTANCE.sendToServer(new GetSimIdPacket(pos.getX(),pos.getY(),pos.getZ()));

		}
	}}
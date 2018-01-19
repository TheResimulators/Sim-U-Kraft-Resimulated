package com.resimulators.simukraft.common.tiles.base;

import com.resimulators.simukraft.common.tiles.structure.Structure;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Created by Astavie on 19/01/2018 - 7:24 PM.
 */
public class TileBuilderBase extends TileEntity implements ITickable {

	private Structure structure;
	private boolean building;
	private int progress;

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public boolean isBuilding() {
		return building;
	}

	public void setBuilding(boolean building) {
		this.building = building;
	}

	@Override
	public void update() {
		if (building) {
			if (structure == null) { // Just in case this ever happens
				building = false;
				progress = 0;
			} else {
				int x = (progress % structure.getWidth());
				int z = (progress / structure.getWidth()) % structure.getDepth();
				int y = (progress / structure.getWidth()) / structure.getDepth();
				if (y >= structure.getHeight()) {
					building = false;
					progress = 0;
					// TODO: Implement "finished" logic
				} else {
					// TODO: Check for items in adjacent inventories
					world.setBlockState(getPos().add(x + 1, y, z), structure.getBlock(x, y, z));
					progress++;
				}
			}
		}
	}

}

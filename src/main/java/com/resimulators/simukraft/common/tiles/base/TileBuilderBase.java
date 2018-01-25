package com.resimulators.simukraft.common.tiles.base;

import com.resimulators.simukraft.common.tiles.structure.Structure;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Astavie on 19/01/2018 - 7:24 PM.
 */
public class TileBuilderBase extends TileEntity {

	protected Structure structure;
	protected int progress;

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	protected void build() {
		if (structure == null) { // Just in case this ever happens
			progress = 0;
		} else {
			int x = (progress % structure.getWidth());
			int z = (progress / structure.getWidth()) % structure.getDepth();
			int y = (progress / structure.getWidth()) / structure.getDepth();
			if (!isFinished()) {
				// TODO: Check for items in adjacent inventories
				world.setBlockState(getPos().add(x + 1, y, z), structure.getBlock(x, y, z));
				progress++;
			}
		}
	}

	protected boolean isFinished() {
		return progress >= structure.getWidth() * structure.getHeight() * structure.getDepth();
	}

}

package com.resimulators.simukraft.common.tileentity.base;

import com.resimulators.simukraft.common.block.BlockConstructorBox;
import com.resimulators.simukraft.common.block.BlockControlBox;
import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
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
		System.out.println("building");
		if (structure == null) { // Just in case this ever happens
			progress = 0;
		} else {
			int x = (progress % structure.getWidth());
			int z = (progress / structure.getWidth()) % structure.getDepth();
			int y = (progress / structure.getWidth()) / structure.getDepth();
			if (!isFinished())
				// TODO: Check for items in adjacent inventories
				world.setBlockState(getPos().add(x + 1, y, z), structure.getBlock(x, y, z));
			    if (structure.getBlock(x,y,z).getBlock() instanceof BlockControlBox){
                    ((BlockControlBox)structure.getBlock(x,y,z).getBlock()).name = structure.getName();
                    ((BlockControlBox)structure.getBlock(x,y,z).getBlock()).createNewTileEntity(world,0);
                }
				progress++;
			}
		}

	protected boolean isFinished() {
		if (structure == null){
			return false;
		}
		System.out.println("progress " + progress + " strutuce thing " +structure.getWidth() * structure.getHeight() * structure.getDepth());
		return progress >= structure.getWidth() * structure.getHeight() * structure.getDepth();
	}
}

package com.resimulators.simukraft.common.tiles.structure;

import net.minecraft.block.state.IBlockState;

/**
 * Created by Astavie on 19/01/2018 - 7:45 PM.
 */
public class Structure {

	private final IBlockState[][][] data;
	private final int width, height, depth;

	public Structure(IBlockState[][][] data) {
		this.data = data;
		this.width = data.length;
		this.height = data[0].length;
		this.depth = data[0][0].length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}

	public IBlockState getBlock(int x, int y, int z) {
		return data[x][y][z];
	}

	// TODO: JSON loading

}

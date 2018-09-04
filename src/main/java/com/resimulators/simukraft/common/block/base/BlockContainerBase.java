package com.resimulators.simukraft.common.block.base;

import com.resimulators.simukraft.SimUKraft;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Astavie on 19/01/2018 - 7:13 PM.
 */
public abstract class BlockContainerBase extends BlockBase implements ITileEntityProvider {

	public BlockContainerBase(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
		super(name, tab, blockMaterialIn, blockMapColorIn);
		this.hasTileEntity = true;
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		super.breakBlock(world, pos, state);
		world.removeTileEntity(pos);
	}

	@Override
	public boolean eventReceived(IBlockState state, World world, BlockPos pos, int i0, int i1) {
		super.eventReceived(state, world, pos, i0, i1);
		TileEntity tile = world.getTileEntity(pos);
		return tile != null && tile.receiveClientEvent(i0, i1);
	}

}

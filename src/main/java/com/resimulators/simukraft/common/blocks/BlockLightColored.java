package com.resimulators.simukraft.common.blocks;

import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLightColored extends BlockLight {

	public BlockLightColored(String name, CreativeTabs tab, Material blockMaterialIn) {
		super(name, tab, blockMaterialIn, MapColor.SNOW);
		setDefaultState(blockState.getBaseState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public int damageDropped(IBlockState p_damageDropped_1_) {
		return p_damageDropped_1_.getValue(BlockColored.COLOR).getMetadata();
	}

	@Override
	public void getSubBlocks(CreativeTabs p_getSubBlocks_1_, NonNullList<ItemStack> p_getSubBlocks_2_) {
		for (EnumDyeColor lvt_6_1_ : EnumDyeColor.values())
			p_getSubBlocks_2_.add(new ItemStack(this, 1, lvt_6_1_.getMetadata()));
	}

	@Override
	public MapColor getMapColor(IBlockState p_getMapColor_1_, IBlockAccess p_getMapColor_2_, BlockPos p_getMapColor_3_) {
		return MapColor.getBlockColor(p_getMapColor_1_.getValue(BlockColored.COLOR));
	}

	@Override
	public IBlockState getStateFromMeta(int p_getStateFromMeta_1_) {
		return this.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(p_getStateFromMeta_1_));
	}

	@Override
	public int getMetaFromState(IBlockState p_getMetaFromState_1_) {
		return p_getMetaFromState_1_.getValue(BlockColored.COLOR).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockColored.COLOR);
	}

}

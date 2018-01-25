package com.resimulators.simukraft.common.blocks;

import com.resimulators.simukraft.common.blocks.base.BlockBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by fabbe on 15/01/2018 - 8:50 PM.
 */
public class BlockFarmBox extends BlockBase {
    public BlockFarmBox(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, tab, blockMaterialIn, blockMapColorIn);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        //TODO: implement logic
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        //TODO: implement logic
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //TODO: implement logic
        return false;
    }
}

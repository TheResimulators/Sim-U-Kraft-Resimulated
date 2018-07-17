package com.resimulators.simukraft.common.block;

import com.resimulators.simukraft.common.block.base.BlockBase;
import com.resimulators.simukraft.common.tileentity.TileController;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by fabbe on 15/01/2018 - 8:47 PM.
 */
public class BlockControlBox extends BlockBase implements ITileEntityProvider {
    public BlockControlBox(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, tab, blockMaterialIn, blockMapColorIn);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //TODO: implement logic
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileController();
    }
}

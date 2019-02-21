package com.resimulators.simukraft.common.block;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.model.RenderOutline;
import com.resimulators.simukraft.common.block.base.BlockBase;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Astavie on 25/01/2018 - 5:22 PM.
 */
public class BlockMineBox extends BlockBase implements ITileEntityProvider {
	EnumFacing facing;
	public BlockMineBox(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
		super(name, tab, blockMaterialIn, blockMapColorIn);
	}

	@Override
	public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) {
		//TODO: implement logic
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		facing = placer.getHorizontalFacing();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
		    return true;
		} else {
            TileEntity entity = worldIn.getTileEntity(pos);
            if (entity instanceof TileMiner){
                ((TileMiner) entity).openGui(worldIn,pos,playerIn);
            }

		}
		return true;
	}


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
	    return new TileMiner(facing);
	}
}

package com.resimulators.simukraft.common.blocks;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.blocks.base.BlockContainerBase;
import com.resimulators.simukraft.common.tiles.TileConstructor;
import com.resimulators.simukraft.common.tiles.structure.Structure;
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
import net.minecraftforge.fml.common.Loader;

import java.io.File;

/**
 * Created by fabbe on 15/01/2018 - 8:43 PM.
 */
public class BlockConstructorBox extends BlockContainerBase {
    private static final Structure structure = Structure.load(new File(Loader.instance().getConfigDir(), Reference.MOD_ID + ".struct"));

    public BlockConstructorBox(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, tab, blockMaterialIn, blockMapColorIn);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        //TODO: implement logic
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        //TODO: implement logic
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileConstructor)
            ((TileConstructor) tile).building = true;
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileConstructor tile = new TileConstructor();
        tile.setStructure(structure);
        return tile;
    }
}

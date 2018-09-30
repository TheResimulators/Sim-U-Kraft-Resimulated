package com.resimulators.simukraft.common.block;

import com.resimulators.simukraft.common.block.base.BlockBase;
import com.resimulators.simukraft.common.enums.enumStructure;
import com.resimulators.simukraft.common.tileentity.TileCattle;
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
    public String name;
    public boolean isresidential;

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);
        System.out.println("open");
        System.out.println(entity);
        if (worldIn.isRemote){
        if (entity instanceof TileCattle)((TileCattle) entity).openGui(worldIn,pos,playerIn);}
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        System.out.println("name " + name );
        if (name == null) return null;
        if (enumStructure.FarmStructure.byName(name) == null && !isresidential){System.out.println("null");return null;}
        if (!isresidential){
            return enumStructure.FarmStructure.byName(name);

        }
        return enumStructure.FarmStructure.RESIDENTIAL.teSupplier.get();

    }
}

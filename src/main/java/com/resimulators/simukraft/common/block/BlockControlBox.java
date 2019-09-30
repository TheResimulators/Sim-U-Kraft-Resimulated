package com.resimulators.simukraft.common.block;

import com.resimulators.simukraft.common.block.base.BlockBase;
import com.resimulators.simukraft.common.enums.enumStructure;
import com.resimulators.simukraft.common.interfaces.ISim;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.common.tileentity.*;
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

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Created by fabbe on 15/01/2018 - 8:47 PM.
 */
public class BlockControlBox extends BlockBase implements ITileEntityProvider {
    public BlockControlBox(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, tab, blockMaterialIn, blockMapColorIn);
    }
    public String profession;

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity entity = worldIn.getTileEntity(pos);

        if (worldIn.isRemote){
        if (entity instanceof ISim)((ISim) entity).openGui(worldIn,pos,playerIn);}
        else if (entity instanceof TileStructureDetails){

        }
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        TileEntity entity = null;
        if (profession != null){
            entity = EnumProfession.getEnumByName(profession);
        }else {
            entity = new TileStructureDetails();
        }
        return entity;
    }


    private enum EnumProfession{
        RESIDENTIAL("residential", TileResidential::new),
        COW("cow",TileCattle::new),
        SHEEP("sheep", TileSheep::new),
        PIG("pig", TilePig::new),
        BUTCHER("butcher", TileWIP::new),
        GROCERY("grocery",TileWIP::new),
        GLASS("glass",TileWIP::new),
        CHEESE("cheese",TileWIP::new);

        public String name;
        public Supplier<TileEntity> teSupplier;


        public static TileEntity getEnumByName(String name){
            for (EnumProfession value:EnumProfession.values()){
                if (value.name.equals(name)) return value.teSupplier.get();
            }
            return null;
        }
        EnumProfession(String name, Supplier<TileEntity> teSupplier){
            this.name= name;
            this.teSupplier = teSupplier;
        }
    }
}

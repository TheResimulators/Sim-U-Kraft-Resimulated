package com.resimulators.simukraft.common.blocks.base;

import com.resimulators.simukraft.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by fabbe on 06/01/2018 - 4:54 AM.
 */
public class BlockBase extends Block {
    public BlockBase(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        this.setName(name);
        this.setCreativeTab(tab);
    }

    private void setName(String name) {
        this.setRegistryName(Reference.MOD_ID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
}

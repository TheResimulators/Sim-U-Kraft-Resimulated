package com.resimulators.simukraft.common.blocks;

import com.resimulators.simukraft.common.blocks.base.BlockBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockredLight extends BlockBase {
    public BlockredLight(String name, CreativeTabs tab, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(name, tab, blockMaterialIn, blockMapColorIn);
        this.setLightLevel(.9375f);
    }
}

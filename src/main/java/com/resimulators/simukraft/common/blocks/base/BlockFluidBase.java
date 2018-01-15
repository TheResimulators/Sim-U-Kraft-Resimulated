package com.resimulators.simukraft.common.blocks.base;

import com.resimulators.simukraft.Reference;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by fabbe on 15/01/2018 - 8:52 PM.
 */
public class BlockFluidBase extends BlockFluidClassic {
    public BlockFluidBase(Fluid fluid, String name, CreativeTabs tab, Material material) {
        super(fluid, material);
        this.setName(name);
        this.setCreativeTab(tab);
    }

    private void setName(String name) {
        this.setRegistryName(Reference.MOD_ID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
}

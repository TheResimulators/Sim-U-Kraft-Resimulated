package com.resimulators.simukraft.init;

import com.resimulators.simukraft.SimUTab;
import com.resimulators.simukraft.common.block.*;
import com.resimulators.simukraft.common.block.base.BlockBase;
import com.resimulators.simukraft.common.block.base.BlockFluidBase;
import com.resimulators.simukraft.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fluids.Fluid;

import java.util.Arrays;

/**
 * Created by fabbe on 06/01/2018 - 2:47 AM.
 */
public class ModBlocks {
    //Basic Blocks
    public static final Block CHEESE_BLOCK = new BlockBase("cheese_block", SimUTab.SUTab, Material.SPONGE, MapColor.YELLOW);
    public static final Block COMPOSITE_BRICK = new BlockBase("composite_brick", SimUTab.SUTab, Material.ROCK, MapColor.STONE);

    //Sim-U Kraft Structure Blocks
    public static final Block CITY_BOX = new BlockCityBox("city_box", SimUTab.SUTab, Material.IRON, MapColor.IRON);
    public static final Block CONSTRUCTOR_BOX = new BlockConstructorBox("constructor_box", SimUTab.SUTab, Material.ROCK, MapColor.GREEN);
    public static final Block CONTROL_BOX = new BlockControlBox("control_box", SimUTab.SUTab, Material.IRON, MapColor.IRON);
    public static final Block FARM_BOX = new BlockFarmBox("farm_box",SimUTab.SUTab, Material.ROCK, MapColor.DIRT);
    public static final Block MINE_BOX = new BlockMineBox("mine_box",SimUTab.SUTab, Material.ROCK, MapColor.WOOD);
    public static final Block TOWNHALL_BOX = new BlockTownhall(("Town Hall"),SimUTab.SUTab,Material.ROCK,MapColor.WOOD);
    //Sim-u Kraft Deco Blocks
    public static final Block LIGHT = new BlockLightColored("light",SimUTab.SUTab, Material.WOOD);
    public static final Block RAINBOW_LIGHT = new BlockLight("rainbow_light",SimUTab.SUTab,Material.WOOD,MapColor.SILVER);

    //Fluid Blocks
    public static final Block MILK_BLOCK = new BlockFluidBase(ModFluids.MILK, "milk_block", null, Material.WATER);

    public static void init() {
        RegistryHandler.registerBlock(CHEESE_BLOCK);
        RegistryHandler.registerBlock(COMPOSITE_BRICK);

        RegistryHandler.registerBlock(CITY_BOX);
        RegistryHandler.registerBlock(CONSTRUCTOR_BOX);
        RegistryHandler.registerBlock(CONTROL_BOX);
        RegistryHandler.registerBlock(FARM_BOX);
        RegistryHandler.registerBlock(MINE_BOX);
        RegistryHandler.registerBlock(TOWNHALL_BOX);

        RegistryHandler.registerBlock(LIGHT, Arrays.stream(EnumDyeColor.values()).map(EnumDyeColor::getName).toArray(String[]::new));
        RegistryHandler.registerBlock(RAINBOW_LIGHT);

        registerFluidBlock(MILK_BLOCK, ModFluids.MILK);
    }

    private static void registerFluidBlock(Block block, Fluid fluid) {
        RegistryHandler.registerFluid(block, fluid);
        fluid.setBlock(block);
    }
}

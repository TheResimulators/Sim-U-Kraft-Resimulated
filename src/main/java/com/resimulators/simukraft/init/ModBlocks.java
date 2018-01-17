package com.resimulators.simukraft.init;

import com.resimulators.simukraft.SimUTab;
import com.resimulators.simukraft.common.blocks.BlockCityBox;
import com.resimulators.simukraft.common.blocks.BlockControlBox;
import com.resimulators.simukraft.common.blocks.BlockConstructorBox;
import com.resimulators.simukraft.common.blocks.BlockFarmingBox;
import com.resimulators.simukraft.common.blocks.base.BlockBase;
import com.resimulators.simukraft.common.blocks.base.BlockFluidBase;
import com.resimulators.simukraft.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
///add minerblock 18/1 - 1030
/**
 * Created by fabbe on 06/01/2018 - 2:47 AM.
 */
public class ModBlocks {
    //Basic Blocks
    public static final Block CHEESE_BLOCK = new BlockBase("cheeseblock", SimUTab.SUTab, Material.SPONGE, MapColor.YELLOW);
    public static final Block COMPOSITE_BRICK = new BlockBase("compositebrick", SimUTab.SUTab, Material.ROCK, MapColor.GRAY);

    //Sim-U Kraft Structure Blocks
    public static final Block CITY_BOX = new BlockCityBox("citybox", SimUTab.SUTab, Material.ANVIL, MapColor.GRAY);
    public static final Block CONSTRUCTOR_BOX = new BlockConstructorBox("constructorbox", SimUTab.SUTab, Material.ANVIL, MapColor.GRAY);
    public static final Block CONTROL_BOX = new BlockControlBox("controlbox", SimUTab.SUTab, Material.ANVIL, MapColor.GRAY);
    public static final Block FARM_BOX = new BlockFarmingBox("farmbox",SimUTab.SUTab, Material.ANVIL, MapColor.GRAY);
    public static final Block MINER_BOX = new BlockFarmingBox("minerbox",SimUTab.SUTab, Material.ANVIL, MapColor.GRAY);
    //Fluid Blocks
    public static final Block MILK_BLOCK = new BlockFluidBase(ModFluids.MILK, "milkblock", null, Material.WATER);

    public static void init() {
        RegistryHandler.registerBlock(CHEESE_BLOCK);
        RegistryHandler.registerBlock(COMPOSITE_BRICK);

        RegistryHandler.registerBlock(CITY_BOX);
        RegistryHandler.registerBlock(CONSTRUCTOR_BOX);
        RegistryHandler.registerBlock(CONTROL_BOX);
        RegistryHandler.registerBlock(FARM_BOX);
        RegistryHandler.registerBlock(MINER_BOX);
        registerFluidBlock(MILK_BLOCK, ModFluids.MILK);
    }

    private static void registerFluidBlock(Block block, Fluid fluid) {
        RegistryHandler.registerFluid(block, fluid);
        fluid.setBlock(block);
    }
}

package com.resimulators.simukraft.init;

import com.resimulators.simukraft.SimUTab;
import com.resimulators.simukraft.common.blocks.*;
import com.resimulators.simukraft.common.blocks.base.BlockBase;
import com.resimulators.simukraft.common.blocks.base.BlockFluidBase;
import com.resimulators.simukraft.registry.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

import static net.minecraft.block.material.MapColor.GRAY;
///add minerblock 18/1 - 1030
/**
 * Created by fabbe on 06/01/2018 - 2:47 AM.
 */
public class ModBlocks {
    //Basic Blocks
    public static final Block CHEESE_BLOCK = new BlockBase("cheeseblock", SimUTab.SUTab, Material.SPONGE, MapColor.YELLOW);
    public static final Block COMPOSITE_BRICK = new BlockBase("compositebrick", SimUTab.SUTab, Material.ROCK, GRAY);

    //Sim-U Kraft Structure Blocks
    public static final Block CITY_BOX = new BlockCityBox("citybox", SimUTab.SUTab, Material.ANVIL, GRAY);
    public static final Block CONSTRUCTOR_BOX = new BlockConstructorBox("constructorbox", SimUTab.SUTab, Material.ANVIL, GRAY);
    public static final Block CONTROL_BOX = new BlockControlBox("controlbox", SimUTab.SUTab, Material.ANVIL, GRAY);
    public static final Block FARM_BOX = new BlockFarmingBox("farmbox",SimUTab.SUTab, Material.ANVIL, GRAY);
    public static final Block MINER_BOX = new BlockFarmingBox("minerbox",SimUTab.SUTab, Material.ANVIL, GRAY);
    //Sim-u Kraft deco blocks
    public static final Block LIGHT_WHITE_BLOCK = new BlockwhiteLight("whitelight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_ORANGE_BLOCK = new BlockorangeLight("orangelight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_GREEN_BLOCK = new BlockgreenLight("greenlight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_PURPLE_BLOCK = new BlockpurpleLight("purplelight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_RED_BLOCK = new BlockredLight("redlight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_YELLOW_BLOCK = new BlockyellowLight("yellowlight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_BLUE_BLOCK = new BlockblueLight("bluelight",SimUTab.SUTab,Material.ANVIL,GRAY);
    public static final Block LIGHT_RAINBOW_BLOCK = new BlockrainbowLight("rainbowlight",SimUTab.SUTab,Material.ANVIL,GRAY);

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

        RegistryHandler.registerBlock(LIGHT_WHITE_BLOCK);
        RegistryHandler.registerBlock(LIGHT_ORANGE_BLOCK);
        RegistryHandler.registerBlock(LIGHT_GREEN_BLOCK);
        RegistryHandler.registerBlock(LIGHT_PURPLE_BLOCK);
        RegistryHandler.registerBlock(LIGHT_RED_BLOCK);
        RegistryHandler.registerBlock(LIGHT_YELLOW_BLOCK);
        RegistryHandler.registerBlock(LIGHT_BLUE_BLOCK);
        RegistryHandler.registerBlock(LIGHT_RAINBOW_BLOCK);


        registerFluidBlock(MILK_BLOCK, ModFluids.MILK);
    }

    private static void registerFluidBlock(Block block, Fluid fluid) {
        RegistryHandler.registerFluid(block, fluid);
        fluid.setBlock(block);
    }
}

package com.resimulators.simukraft.registry;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

/**
 * Created by fabbe on 06/01/2018 - 2:45 AM.
 */
public class RegistryHandler {
    public static void registerBlock(Block block, String... names) {
        registerBlock(block, new ItemBlock(block).setRegistryName(block.getRegistryName()), names);
    }

    public static void registerBlock(Block block, Item item, String... names) {
        BlockRegistrationHandler.blocks.add(block);
        ItemRegistrationHandler.items.put(item, names);
    }

    public static void registerFluid(Block block, Fluid fluid) {
        BlockRegistrationHandler.blocks.add(block);
        BlockRegistrationHandler.fluids.put(block, fluid);
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    private static class BlockRegistrationHandler {
        private static final Set<Block> blocks = new HashSet<>();
        private static final Map<Block, Fluid> fluids = new HashMap<>();

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> reg = event.getRegistry();
            for (final Block block : blocks) {
                if (block.getRegistryName() != null) {
                    reg.register(block);
                    SimUKraft.getLogger().info("Successfully added block: " + block.getRegistryName().getResourcePath() + " to the game.");
                } else
                    SimUKraft.getLogger().warn("Tried to register block without registry name, ignoring.");
            }
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            for (Map.Entry<Block, Fluid> entry: fluids.entrySet())
                ModelLoader.setCustomStateMapper(entry.getKey(), new FluidStateMapper(entry.getValue()));
        }
    }

    public static void registerItem(Item item, String... names) {
        ItemRegistrationHandler.items.put(item, names);
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    private static class ItemRegistrationHandler {
        private static final Map<Item, String[]> items = new HashMap<>();

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> reg = event.getRegistry();
            for (Item item : items.keySet()) {
                if (item.getRegistryName() != null) {
                    reg.register(item);
                    SimUKraft.getLogger().info("Successfully added item: " + item.getRegistryName().getResourcePath() + " to the game.");
                } else
                    SimUKraft.getLogger().warn("Tried to register item without registry name, ignoring.");
            }
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            for (Map.Entry<Item, String[]> entry : items.entrySet()) {
                String[] s = entry.getValue();
                if (s.length == 0)
                    registerItemModel(entry.getKey());
                else for (int i = 0; i < s.length; i++)
                    registerItemModel(entry.getKey(), i, s[i]);
            }
        }

        private static void registerItemModel(Item item) {
            registerItemModel(item, 0);
        }

        private static void registerItemModel(Item item, int meta) {
            registerItemModel(item, meta, item.getRegistryName().getResourcePath());
        }

        private static void registerItemModel(Item item, int meta, String name) {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID + ":" + name, "inventory"));
        }
    }

    @SideOnly(Side.CLIENT)
    private static class FluidStateMapper extends StateMapperBase {

        private final ModelResourceLocation location;

        private FluidStateMapper(Fluid fluid) {
            this.location = new ModelResourceLocation(Reference.MOD_ID + ":fluid_block" ,fluid.getName());
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
            return location;
        }

    }
}

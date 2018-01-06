package com.resimulators.simukraft.registry;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;

/**
 * Created by fabbe on 06/01/2018 - 2:45 AM.
 */
public class RegistryHandler {
    public static void registerBlock(Block block) {
        BlockRegistrationHandler.blocks.add(block);
    }

    public static void registerBlock(Block block, ItemBlock itemBlock) {
        ForgeRegistries.BLOCKS.register(block);
        itemBlock.setRegistryName(block.getRegistryName());
        ForgeRegistries.ITEMS.register(itemBlock);
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class BlockRegistrationHandler {
        public static final Set<Block> BLOCK_LIST = new HashSet<>();
        public static final Set<Item> ITEM_LIST = new HashSet<>();

        private static final Set<Block> registeredBlockList = new HashSet<>();
        public static final List<Block> blocks = new ArrayList<>();

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> reg = event.getRegistry();
            for (final Block block : blocks) {
                if (block.getRegistryName() != null) {
                    reg.register(block);
                    BLOCK_LIST.add(block);
                    SimUKraft.getLogger().info("Successfully added block: " + block.getRegistryName().getResourcePath() + " to the game.");
                } else
                    SimUKraft.getLogger().warn("Tried to register block without registry name, ignoring.");
            }
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> reg = event.getRegistry();
            for (final Block item : blocks) {
                if (item.getRegistryName() != null) {
                    ItemBlock itemBlock = (ItemBlock) new ItemBlock(item).setRegistryName(item.getRegistryName());
                    reg.register(itemBlock);
                    ITEM_LIST.add(itemBlock);
                }
            }
        }

        @SubscribeEvent
        public static void registerModels(final ModelRegistryEvent event) {
            for (Block block : BLOCK_LIST)
                if (!registeredBlockList.contains(block))
                    registerBlockModel(block);
        }

        private static void registerBlockModel(final Block block) {
            final String registryName = block.getRegistryName().toString();
            final ModelResourceLocation location = new ModelResourceLocation(registryName, "inventory");
            registerBlockModel(block, location);
        }

        private static void registerBlockModel(final Block block, final ModelResourceLocation modelResourceLocation) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, modelResourceLocation);
            registeredBlockList.add(block);
        }

        private static void registerBlockModelVariants(Block block, int meta, String fileName) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, fileName), "inventory"));
        }
    }

    public static void registerItem(Item item) {
        ItemRegistrationHandler.items.add(item);
    }

    public static void registerItem(Item item, String... names) {
        ItemRegistrationHandler.items.add(item);
        Map<Item, String[]> itemStringMap = new HashMap<>();
        itemStringMap.put(item, names);
        ItemRegistrationHandler.itemLoc.add(itemStringMap);
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class ItemRegistrationHandler {
        private static final Set<Item> ITEM_LIST = new HashSet<>();
        private static final Set<Item> registeredItemList = new HashSet<>();
        public static final List<Item> items = new ArrayList<>();
        public static final List<Map<Item, String[]>> itemLoc = new ArrayList<>();

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> reg = event.getRegistry();
            for (Item item : items) {
                if (item.getRegistryName() != null) {
                    reg.register(item);
                    ITEM_LIST.add(item);
                    SimUKraft.getLogger().info("Successfully added item: " + item.getRegistryName().getResourcePath() + " to the game.");
                } else
                    SimUKraft.getLogger().warn("Tried to register item without registry name, ignoring.");
            }
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            for (Map<Item, String[]> map : itemLoc) {
                String[] s = (String[])map.values().toArray()[0];
                for (int i = 0; i < s.length; i++) {
                    registerItemModel((Item)map.keySet().toArray()[0], i, s[i]);
                }
            }

            for (Item item : ITEM_LIST) {
                registerItemModel(item);
            }
        }

        private static void registerItemModel(Item item) {
            String registryName = item.getRegistryName().toString();
            ModelResourceLocation location = new ModelResourceLocation(registryName, "inventory");
            registerItemModel(item, 0, location);
        }

        private static void registerItemModel(Item item, int meta) {
            registerItemModel(item, meta, new ModelResourceLocation(item.getRegistryName().toString()));
        }

        private static void registerItemModel(Item item, int meta, String name) {
            registerItemModel(item, meta, new ModelResourceLocation(Reference.MOD_ID + ":" + name, "inventory"));
        }

        private static void registerItemModel(Item item, int meta, ModelResourceLocation location) {
            if (!registeredItemList.contains(item) || meta > 0) {
                ModelLoader.setCustomModelResourceLocation(item, meta, location);
                if (!registeredItemList.contains(item))
                    registeredItemList.add(item);
            }
        }
    }
}

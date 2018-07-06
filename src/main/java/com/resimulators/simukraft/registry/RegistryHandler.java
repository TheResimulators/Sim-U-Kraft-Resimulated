package com.resimulators.simukraft.registry;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by fabbe on 06/01/2018 - 2:45 AM.
 */
public class RegistryHandler {
    public static void registerBlock(Block block, String... names) {
        registerBlock(block, new ItemBlockMulti(block, names).setRegistryName(block.getRegistryName()), names);
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
            for (Map.Entry<Block, Fluid> entry : fluids.entrySet())
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
                    registerItemModel(entry.getKey(), i, new ResourceLocation(entry.getKey().getRegistryName() + "_" + s[i]));
            }
        }

        @SideOnly(Side.CLIENT)
        private static void registerItemModel(Item item) {
            registerItemModel(item, 0);
        }

        @SideOnly(Side.CLIENT)
        private static void registerItemModel(Item item, int meta) {
            registerItemModel(item, meta, item.getRegistryName());
        }

        @SideOnly(Side.CLIENT)
        private static void registerItemModel(Item item, int meta, ResourceLocation location) {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(location, "inventory"));
        }
    }

    private static class ItemBlockMulti extends ItemBlock {
        private final String[] names;

        private ItemBlockMulti(Block block, String... names) {
            super(block);
            this.names = names;
            if (names.length > 1)
                this.setHasSubtypes(true);
        }

        @Override
        public String getUnlocalizedName(ItemStack p_getUnlocalizedName_1_) {
            if (p_getUnlocalizedName_1_.getMetadata() < names.length)
                return super.getUnlocalizedName(p_getUnlocalizedName_1_) + "." + names[p_getUnlocalizedName_1_.getMetadata()];
            return super.getUnlocalizedName(p_getUnlocalizedName_1_);
        }

        @Override
        public int getMetadata(int p_getMetadata_1_) {
            return p_getMetadata_1_ < names.length ? p_getMetadata_1_ : 0;
        }
    }
}

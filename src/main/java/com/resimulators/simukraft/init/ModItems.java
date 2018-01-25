package com.resimulators.simukraft.init;

import com.resimulators.simukraft.SimUTab;
import com.resimulators.simukraft.common.items.ItemGranules;
import com.resimulators.simukraft.common.items.base.ItemFoodBase;
import com.resimulators.simukraft.registry.RegistryHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by fabbe on 06/01/2018 - 2:47 AM.
 */
public class ModItems {
    public static final Item GRANULES = new ItemGranules("granules", SimUTab.SUTab);
    public static final Item BURGER = new ItemFoodBase("burger", SimUTab.SUTab, 8, 8, false);
    public static final Item CHEESE = new ItemFoodBase("cheese", SimUTab.SUTab, 4, 4, false);
    public static final Item CHEESEBURGER = new ItemFoodBase("cheeseburger", SimUTab.SUTab, 14, 10, false);
    public static final Item FRIES = new ItemFoodBase("fries", SimUTab.SUTab, 5, 6, false);

    public static void init() {
        RegistryHandler.registerItem(GRANULES, "granules-copper", "granules-gold", "granules-iron", "granules-tin");
        RegistryHandler.registerItem(BURGER);
        RegistryHandler.registerItem(CHEESE);
        RegistryHandler.registerItem(CHEESEBURGER);
        RegistryHandler.registerItem(FRIES);
        OreDictionary.registerOre("dustCopper",new ItemStack(GRANULES, 1, 0));
        OreDictionary.registerOre("dustGold",new ItemStack(GRANULES, 1, 2));
        OreDictionary.registerOre("dustIron",new ItemStack(GRANULES, 1, 3));
        OreDictionary.registerOre("dustTin",new ItemStack(GRANULES, 1, 4));
    }
}

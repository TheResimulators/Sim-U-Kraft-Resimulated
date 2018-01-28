package com.resimulators.simukraft.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Astavie on 27/01/2018 - 5:30 PM.
 */
public class ModOreDict {

	public static void init() {
		OreDictionary.registerOre("dustCopper", new ItemStack(ModItems.GRANULES, 1, 0));
		OreDictionary.registerOre("dustGold", new ItemStack(ModItems.GRANULES, 1, 2));
		OreDictionary.registerOre("dustIron", new ItemStack(ModItems.GRANULES, 1, 3));
		OreDictionary.registerOre("dustTin", new ItemStack(ModItems.GRANULES, 1, 4));
	}

}

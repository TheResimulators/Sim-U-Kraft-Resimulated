package com.resimulators.simukraft.common.item.base;

import com.resimulators.simukraft.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

/**
 * Created by fabbe on 06/01/2018 - 6:16 AM.
 */
public class ItemFoodBase extends ItemFood {
    public ItemFoodBase(String name, CreativeTabs tab, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setName(name);
        this.setCreativeTab(tab);
    }

    private void setName(String name) {
        this.setRegistryName(Reference.MOD_ID, name);
        this.setTranslationKey(this.getRegistryName().toString());
    }
}

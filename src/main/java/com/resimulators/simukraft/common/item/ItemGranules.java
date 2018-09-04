package com.resimulators.simukraft.common.item;

import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by fabbe on 06/01/2018 - 5:22 AM.
 */
public class ItemGranules extends ItemBase {
    public static final String[] TYPES = new String[]{"copper", "gold", "iron", "tin"};

    public ItemGranules(String name, CreativeTabs tab) {
        super(name, tab);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        int i = stack.getMetadata();
        if (i > 3)
            i = 0;
        return super.getUnlocalizedNameInefficiently(stack) + "." + TYPES[i];
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 4; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
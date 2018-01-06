package com.resimulators.simukraft.init;

import com.resimulators.simukraft.SimUTab;
import com.resimulators.simukraft.common.items.ItemGranules;
import com.resimulators.simukraft.registry.RegistryHandler;
import net.minecraft.item.Item;

/**
 * Created by fabbe on 06/01/2018 - 2:47 AM.
 */
public class ModItems {
    public static final Item GRANULES = new ItemGranules("granules", SimUTab.SUTab);

    public static void init() {
        RegistryHandler.registerItem(GRANULES, "granules-copper", "granules-gold", "granules-iron", "granules-tin");
    }
}

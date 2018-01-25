package com.resimulators.simukraft.init;

import com.resimulators.simukraft.common.tiles.TileConstructor;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by fabbe on 06/01/2018 - 2:48 AM.
 */
public class ModTileEntities {
    public static void init() {
        TileEntity.register("constructor", TileConstructor.class);
    }
}

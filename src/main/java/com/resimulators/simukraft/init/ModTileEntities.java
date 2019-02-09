package com.resimulators.simukraft.init;

import com.resimulators.simukraft.common.tileentity.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;

/**
 * Created by fabbe on 06/01/2018 - 2:48 AM.
 */
public class ModTileEntities {
    public static void init() {
        TileEntity.register("constructor", TileConstructor.class);
        TileEntity.register("farm", TileFarm.class);
        TileEntity.register("control", TileResidential.class);
        TileEntity.register("cattle", TileCattle.class);
        TileEntity.register("sheep", TileSheep.class);
        TileEntity.register("miner",TileMiner.class);
    }
}


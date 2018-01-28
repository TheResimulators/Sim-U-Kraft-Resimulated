package com.resimulators.simukraft.debug;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

/**
 * Created by fabbe on 27/01/2018 - 4:37 PM.
 */
public class StructureStore {
    public static IBlockState[][][] debugStructure1 = new IBlockState[3][3][3];

    public static void init() {
        for (int x = 0; x < debugStructure1.length; x++) {
            for (int y = 0; y < debugStructure1[x].length; y++) {
                for (int z = 0; z < debugStructure1[x][y].length; z++) {
                    debugStructure1[x][y][z] = Blocks.LOG.getDefaultState();
                }
            }
        }
    }
}

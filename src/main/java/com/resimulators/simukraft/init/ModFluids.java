package com.resimulators.simukraft.init;

import com.resimulators.simukraft.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by fabbe on 15/01/2018 - 8:56 PM.
 */
public class ModFluids {
    public static final Fluid MILK = new Fluid("milk", new ResourceLocation(Reference.MOD_ID + ":blocks/milk_still"), new ResourceLocation(Reference.MOD_ID + ":blocks/milk_flow"));

    public static void init() {
        registerFluid(MILK);
    }

    private static void registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        FluidRegistry.addBucketForFluid(fluid);
    }
}

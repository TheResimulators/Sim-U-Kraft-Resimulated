package com.resimulators.simukraft.registry;

import com.resimulators.simukraft.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Astavie on 19/01/2018 - 7:34 PM.
 */
@SideOnly(Side.CLIENT)
public class FluidStateMapper extends StateMapperBase {

	private final ModelResourceLocation location;

	public FluidStateMapper(Fluid fluid) {
		this.location = new ModelResourceLocation(Reference.MOD_ID + ":fluid_block" ,fluid.getName());
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
		return location;
	}

}
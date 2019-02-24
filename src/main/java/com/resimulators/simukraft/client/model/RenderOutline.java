package com.resimulators.simukraft.client.model;

import com.resimulators.simukraft.common.tileentity.TileMiner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderOutline extends TileEntitySpecialRenderer<TileMiner> {
	private static final ResourceLocation TEXTURE_OUTLINE_BEAM = new ResourceLocation(
			"textures/entity/beacon_beam.png");

	public RenderOutline() {

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(TileMiner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (te.isRenderOutline() && (te.getMode() == 1 || te.getMode() == 0)) {
			super.render(te, x, y, z, partialTicks, destroyStage, alpha);
			drawBoundingBox(te, x, y, z);
		}
	}

	private void drawBoundingBox(TileMiner miner, double x, double y, double z) {
		this.setLightmapDisabled(true);

		GlStateManager.disableLighting();

		// the N S E W (not to be confused with nsfw) is for north south east west
		double xposlowN = x, xposlowS = x, xposlowE = x, xposlowW = x;
		double yposlowN = y, yposlowS = y, yposlowE = y, yposlowW = y;
		double zposlowN = z, zposlowS = z, zposlowE = z, zposlowW = z;

		double xposhighN = 1, xposhighS = 1, xposhighE = 1, xposhighW = 1;
		double yposhighN = 1, yposhighS = 1, yposhighE = 1, yposhighW = 1;
		double zposhighN = 1, zposhighS = 1, zposhighE = 1, zposhighW = 1;

		// init min max position for all four directions
		{

			{
				xposlowN = x + 0.75;
				xposhighN = x + 0.25;
				zposhighN = zposlowN - miner.getDepth() - 1;
			}
			{
				zposlowE = z + 0.25;
				zposhighE = z + 0.75;
				xposlowE += 1;
				xposhighE = xposlowE + miner.getDepth() + 1;
			}
			{
				xposlowS = x + 0.25;
				xposhighS = x + 0.75;
				zposlowS += 1;
				zposhighS = zposlowS + miner.getDepth() + 1;
			}
			{
				zposlowW = z + 0.75;
				zposhighW = z + 0.25;
				xposhighW = xposlowW - miner.getDepth() - 1;
			}
		}

		this.bindTexture(TEXTURE_OUTLINE_BEAM);

		EnumFacing facing = miner.getFacing();

		// draw mine border depending on the facing of the block
		switch (facing) {
		case NORTH:
			xposhighE = xposlowE + miner.getWidth() + 1;
			drawBoundingBox(xposlowN, y + 0.75, zposlowN, xposhighN, y + 0.25, zposhighN, 255, 255, 255, 255, true);
			drawBoundingBox(xposlowE, y + 0.75, zposlowE, xposhighE, y + 0.25, zposhighE, 255, 255, 255, 255, false);
			drawBoundingBox(xposlowN + miner.getWidth() + 0.25 + 1, y + 0.75, zposlowN + 0.25,
					xposhighN + miner.getWidth() + 0.25 + 1, y + 0.25, zposhighN, 255, 255, 255, 255, true);
			drawBoundingBox(xposlowE - 0.25, y + 0.75, zposlowE - miner.getDepth() - 0.25 - 1, xposhighE - 0.5,
					y + 0.25, zposhighE - miner.getDepth() - 0.25 - 1, 255, 255, 255, 255, false);
			break;

		case EAST:
			zposhighS = zposlowS + miner.getWidth() + 1;
			drawBoundingBox(xposlowE, y + 0.75, zposlowE, xposhighE, y + 0.25, zposhighE, 255, 255, 255, 255, false);
			drawBoundingBox(xposlowS, y + 0.75, zposlowS, xposhighS, y + 0.25, zposhighS, 255, 255, 255, 255, true);
			drawBoundingBox(xposlowE - 0.25, y + 0.75, zposlowE + miner.getWidth() + 0.25 + 1, xposhighE, y + 0.25,
					zposhighE + miner.getWidth() + 0.25 + 1, 255, 255, 255, 255, false);
			drawBoundingBox(xposlowS + miner.getDepth() + 0.25 + 1, y + 0.75, zposlowS - 0.25,
					xposhighS + miner.getDepth() + 0.25 + 1, y + 0.25, zposhighS - 0.5, 255, 255, 255, 255, true);
			break;

		case SOUTH:
			xposhighW = xposlowW - miner.getWidth() - 1;
			drawBoundingBox(xposlowS, y + 0.75, zposlowS, xposhighS, y + 0.25, zposhighS, 255, 255, 255, 255, true);
			drawBoundingBox(xposlowW, y + 0.75, zposlowW, xposhighW, y + 0.25, zposhighW, 255, 255, 255, 255, false);
			drawBoundingBox(xposlowS - miner.getWidth() - 0.25 - 1, y + 0.75, zposlowS - 0.25,
					xposhighS - miner.getWidth() - 0.25 - 1, y + 0.25, zposhighS, 255, 255, 255, 255, true);
			drawBoundingBox(xposlowW + 0.25, y + 0.75, zposlowW + miner.getDepth() + 0.25 + 1, xposhighW + 0.5,
					y + 0.25, zposhighW + miner.getDepth() + 0.25 + 1, 255, 255, 255, 255, false);
			break;

		case WEST:
			zposhighN = zposlowN - miner.getWidth() - 1;
			drawBoundingBox(xposlowW, y + 0.75, zposlowW, xposhighW, y + 0.25, zposhighW, 255, 255, 255, 255, false);
			drawBoundingBox(xposlowN, y + 0.75, zposlowN, xposhighN, y + 0.25, zposhighN, 255, 255, 255, 255, true);
			drawBoundingBox(xposlowW + 0.25, y + 0.75, zposlowW - miner.getWidth() - 0.25 - 1, xposhighW, y + 0.25,
					zposhighW - miner.getWidth() - 0.25 - 1, 255, 255, 255, 255, false);
			drawBoundingBox(xposlowN - miner.getDepth() - 0.25 - 1, y + 0.75, zposlowN + 0.25,
					xposhighN - miner.getDepth() - 0.25 - 1, y + 0.25, zposhighN + 0.5, 255, 255, 255, 255, true);
			break;
		default:
			break;
		}

		this.setLightmapDisabled(false);
	}

	public void drawBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red,
			float green, float blue, float alpha, boolean southeast) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		drawBox(bufferbuilder, minX, minY, minZ, maxX, maxY, maxZ, 255, 30, 28, 128, southeast);
		tessellator.draw();
	}

	public static void drawBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, int red, int green, int blue, int alpha, boolean southeast) {

		// laser anim calculations
		double d0 = Minecraft.getMinecraft().world.getTotalWorldTime()
				+ Minecraft.getMinecraft().getRenderPartialTicks();
		double d1 = -d0;
		double d2 = MathHelper.frac(d1 * 0.2D - (double) MathHelper.floor(d1 * 0.1D));
		double d14 = -1.0D + d2;

		if (!southeast) // renders the texture in a weird way if pointing to south or north due to the
						// way i render this
		{
			// laser anim east west
			double d15 = (double) 0.5 * ((maxX - minX) * 2) * (0.5D / 0.5D) + d14;
			// sides
			buffer.pos(minX, minY, maxZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, maxZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, maxZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();

			buffer.pos(minX, minY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, minZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, minZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, minZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			// top and bottom
			buffer.pos(minX, minY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, minY, maxZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, minZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();

			buffer.pos(minX, maxY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, minZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, maxZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			// front and back
			buffer.pos(minX, minY, minZ).tex(0D, 0D).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, minZ).tex(0D, 1D).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, maxZ).tex(1D, 1D).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, minY, maxZ).tex(1D, 0D).color(red, green, blue, alpha).endVertex();

			buffer.pos(maxX, minY, minZ).tex(0D, 0D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, maxZ).tex(1D, 0D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, maxZ).tex(1D, 1D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, minZ).tex(0D, 1D).color(red, green, blue, alpha).endVertex();

		} else {
			// laser anim north south
			double d15 = (double) 0.5 * ((maxZ - minZ) * 2) * (0.5D / 0.5D) + d14;
			// front and back
			buffer.pos(minX, minY, minZ).tex(0D, 0D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, minZ).tex(1D, 0D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, minZ).tex(1D, 1D).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, minZ).tex(0D, 1D).color(red, green, blue, alpha).endVertex();

			buffer.pos(minX, minY, maxZ).tex(1D, 0D).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, maxZ).tex(1D, 1D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, maxZ).tex(0D, 1D).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, maxZ).tex(0D, 0D).color(red, green, blue, alpha).endVertex();
			// top and bottom
			buffer.pos(minX, minY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, minY, maxZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, minZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();

			buffer.pos(minX, maxY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, minZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, maxZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();
			// sides
			buffer.pos(minX, minY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, minZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, maxY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(minX, minY, maxZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();

			buffer.pos(maxX, minY, minZ).tex(1D, d14).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, minY, maxZ).tex(1D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, maxZ).tex(0D, d15).color(red, green, blue, alpha).endVertex();
			buffer.pos(maxX, maxY, minZ).tex(0D, d14).color(red, green, blue, alpha).endVertex();
		}
	}

	@Override
	public boolean isGlobalRenderer(TileMiner miner) {
		return true;
	}

}

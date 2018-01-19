package com.resimulators.simukraft.client.render;

import com.resimulators.simukraft.client.model.ModelSim;
import com.resimulators.simukraft.common.entities.EntitySim;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

/**
 * Created by fabbe on 19/01/2018 - 9:04 PM.
 */
public class RenderSim extends RenderLiving<EntitySim> {
    private final boolean smallArms;

    public RenderSim(RenderManager renderManager) {
        this(renderManager, false);
    }

    public RenderSim(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, new ModelSim(0.0F, useSmallArms), 0.5F);
        this.smallArms = useSmallArms;
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerEntityOnShoulder(renderManager));
    }

    public ModelSim getMainModel() {
        return (ModelSim)super.getMainModel();
    }

    public void doRender(EntitySim entity, double x, double y, double z, float entityYaw, float partialTicks) {
        double d0 = y;
        this.bindEntityTexture(entity);

        if (entity.isSneaking()) {
            d0 = y - 0.125D;
        }

        this.setModelVisibilities(entity);
        super.doRender(entity, x, d0, z, entityYaw, partialTicks);
    }

    private void setModelVisibilities(EntitySim entitySim) {
        ModelSim model = this.getMainModel();
        ItemStack itemstack = entitySim.getHeldItemMainhand();
        ItemStack itemstack1 = entitySim.getHeldItemOffhand();
        model.setVisible(true);
        model.isSneak = entitySim.isSneaking();
        ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
        ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

        if (!itemstack.isEmpty()) {
            modelbiped$armpose = ModelBiped.ArmPose.ITEM;

            if (entitySim.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.BLOCK) {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction == EnumAction.BOW) {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (!itemstack1.isEmpty()) {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if (entitySim.getItemInUseCount() > 0) {
                EnumAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == EnumAction.BLOCK) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction1 == EnumAction.BOW) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (entitySim.getPrimaryHand() == EnumHandSide.RIGHT) {
            model.rightArmPose = modelbiped$armpose;
            model.leftArmPose = modelbiped$armpose1;
        } else {
            model.rightArmPose = modelbiped$armpose1;
            model.leftArmPose = modelbiped$armpose;
        }

    }

    public ResourceLocation getEntityTexture(EntitySim entity) {
        //TODO: add actual resource table
        return new ResourceLocation("textures/entity/steve.png");
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    protected void preRenderCallback(EntitySim entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void renderRightArm(EntitySim entitySim) {
        float f = 1.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelSim model = this.getMainModel();
        this.setModelVisibilities(entitySim);
        GlStateManager.enableBlend();
        model.swingProgress = 0.0F;
        model.isSneak = false;
        model.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entitySim);
        model.bipedRightArm.rotateAngleX = 0.0F;
        model.bipedRightArm.render(0.0625F);
        model.bipedRightArmwear.rotateAngleX = 0.0F;
        model.bipedRightArmwear.render(0.0625F);
        GlStateManager.disableBlend();
    }

    public void renderLeftArm(EntitySim entitySim) {
        float f = 1.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        float f1 = 0.0625F;
        ModelSim model = this.getMainModel();
        this.setModelVisibilities(entitySim);
        GlStateManager.enableBlend();
        model.isSneak = false;
        model.swingProgress = 0.0F;
        model.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, entitySim);
        model.bipedLeftArm.rotateAngleX = 0.0F;
        model.bipedLeftArm.render(0.0625F);
        model.bipedLeftArmwear.rotateAngleX = 0.0F;
        model.bipedLeftArmwear.render(0.0625F);
        GlStateManager.disableBlend();
    }

    protected void renderLivingAt(EntitySim entityLivingBaseIn, double x, double y, double z) {
        super.renderLivingAt(entityLivingBaseIn, x, y, z);
    }

    protected void applyRotations(EntitySim entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
    }
}

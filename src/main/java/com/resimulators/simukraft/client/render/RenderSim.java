package com.resimulators.simukraft.client.render;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.client.model.ModelSim;
import com.resimulators.simukraft.common.entities.entitysim.EntitySim;
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
    public RenderSim(RenderManager renderManager) {
        super(renderManager, new ModelSim(0.0F), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    public ModelSim getMainModel() {
        return (ModelSim)super.getMainModel();
    }

    public void doRender(EntitySim entitySim, double x, double y, double z, float entityYaw, float partialTicks) {
        double d0 = y;
        this.bindEntityTexture(entitySim);

        if (entitySim.isSneaking()) {
            d0 = y - 0.125D;
        }

        this.setModelVisibilities(entitySim);
        super.doRender(entitySim, x, d0, z, entityYaw, partialTicks);
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

    public ResourceLocation getEntityTexture(EntitySim entitySim) {
        //TODO: this system is all temporary
        if (entitySim.getFemale())
            return new ResourceLocation(Reference.MOD_ID, "textures/entities/sims/human/female/" + entitySim.getVariation() + ".png");
        else if (!entitySim.getFemale())
            return new ResourceLocation(Reference.MOD_ID, "textures/entities/sims/human/male/" + entitySim.getVariation() + ".png");
        else
            return new ResourceLocation("textures/entity/steve.png");
    }

    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    protected void preRenderCallback(EntitySim entitySim, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    protected void renderLivingAt(EntitySim entitySim, double x, double y, double z) {
        super.renderLivingAt(entitySim, x, y, z);
    }

    protected void applyRotations(EntitySim entitySim, float rotationPitch, float rotationYaw, float partialTicks) {
        super.applyRotations(entitySim, rotationPitch, rotationYaw, partialTicks);
    }
}

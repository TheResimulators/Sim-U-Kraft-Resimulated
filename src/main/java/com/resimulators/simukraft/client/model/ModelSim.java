package com.resimulators.simukraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by fabbe on 19/01/2018 - 9:01 PM.
 */
@SideOnly(Side.CLIENT)
public class ModelSim extends ModelBiped {
    private boolean smallArms;

    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    public ModelRenderer femaleLeftArmwear;
    public ModelRenderer femaleRightArmwear;
    ModelRenderer femaleArmLeft;
    ModelRenderer femaleArmRight;
    ModelRenderer maleArmLeft;

    public ModelSim(float modelSize) {
        super(modelSize, 0.0F, 64, 64);
        this.smallArms = false;

        this.femaleArmLeft = new ModelRenderer(this, 32, 48);
        this.femaleArmLeft.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
        this.femaleArmLeft.setRotationPoint(5.0F, 2.5F, 0.0F);
        this.femaleArmRight = new ModelRenderer(this, 40, 16);
        this.femaleArmRight.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
        this.femaleArmRight.setRotationPoint(-5.0F, 2.5F, 0.0F);
        this.femaleLeftArmwear = new ModelRenderer(this, 48, 48);
        this.femaleLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
        this.femaleLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
        this.femaleRightArmwear = new ModelRenderer(this, 40, 32);
        this.femaleRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
        this.femaleRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);

        this.maleArmLeft = new ModelRenderer(this, 32, 48);
        this.maleArmLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.maleArmLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
        this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
        this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entitySim, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entitySim, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (this.isChild) {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.femaleArmLeft.render(scale);
            this.femaleArmRight.render(scale);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.femaleLeftArmwear.render(scale);
            this.femaleRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        } else {
            if (entitySim.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.femaleArmLeft.render(scale);
            this.femaleArmRight.render(scale);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.femaleLeftArmwear.render(scale);
            this.femaleRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedLeftArm, this.femaleLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.femaleRightArmwear);
        copyModelAngles(this.bipedLeftArm, this.femaleArmLeft);
        copyModelAngles(this.bipedRightArm, this.femaleArmRight);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }

    public void setVisible(boolean visible, boolean female) {
        setVisible(visible);
        if (female) {
            this.bipedLeftArm.showModel = !visible;
            this.bipedRightArm.showModel = !visible;
            this.maleArmLeft.showModel = !visible;
            this.bipedLeftArmwear.showModel = !visible;
            this.bipedRightArmwear.showModel = !visible;
            this.femaleArmLeft.showModel = visible;
            this.femaleArmRight.showModel = visible;
            this.femaleLeftArmwear.showModel = visible;
            this.femaleRightArmwear.showModel = visible;
            this.smallArms = true;
        } else {
            this.femaleArmLeft.showModel = !visible;
            this.femaleArmRight.showModel = !visible;
            this.femaleLeftArmwear.showModel = !visible;
            this.femaleRightArmwear.showModel = !visible;
            this.bipedLeftArmwear.showModel = visible;
            this.bipedRightArmwear.showModel = visible;
            this.smallArms = false;
        }
        this.bipedLeftLegwear.showModel = visible;
        this.bipedRightLegwear.showModel = visible;
        this.bipedBodyWear.showModel = visible;
    }

    public void postRenderArm(float scale, EnumHandSide side) {
        ModelRenderer modelrenderer = this.getArmForSide(side);
        if (this.smallArms) {
            float f = 0.5F * (float)(side == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;
        } else {
            modelrenderer.postRender(scale);
        }
    }
}

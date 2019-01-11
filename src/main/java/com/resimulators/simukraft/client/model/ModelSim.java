package com.resimulators.simukraft.client.model;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
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
        this.maleArmLeft = new ModelRenderer(this, 32, 48);
        this.maleArmLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
        this.maleArmLeft.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
    }

    public void render(Entity entitySim, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entitySim, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (this.isChild) {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.femaleArmLeft.render(scale);
            this.femaleArmRight.render(scale);
        } else {
            if (entitySim.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            this.femaleArmLeft.render(scale);
            this.femaleArmRight.render(scale);
        }

        GlStateManager.popMatrix();
    }

    public void setVisible(boolean visible, boolean female) {
        setVisible(visible);
        if (female) {
            this.bipedLeftArm.showModel = !visible;
            this.bipedRightArm.showModel = !visible;
            this.femaleArmLeft.showModel = visible;
            this.femaleArmRight.showModel = visible;
            this.smallArms = true;
        }
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

package com.resimulators.simukraft.client.render;

import com.resimulators.simukraft.client.model.ModelSim;
import com.resimulators.simukraft.common.entity.EntityParticleSpawner;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderParticleEntity extends Render<EntityParticleSpawner> {

    public RenderParticleEntity(RenderManager manager){
        super(manager);
    }
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityParticleSpawner entityParticleSpawner) {
        return null;
    }

}

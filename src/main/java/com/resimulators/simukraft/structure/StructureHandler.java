package com.resimulators.simukraft.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

/**
 * Created by fabbe on 2019-02-12 - 9:32 AM.
 */
public class StructureHandler {
    private TemplateManagerPlus templateManager;

    private static StructureHandler instance;

    public StructureHandler(World world) {
        templateManager = new TemplateManagerPlus(world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "/structures/", Minecraft.getMinecraft().getDataFixer());
        instance = this;
    }

    public TemplateManagerPlus getTemplateManager() {
        return templateManager;
    }

    public static StructureHandler getInstance() {
        return instance;
    }
}

package com.resimulators.simukraft.structure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

/**
 * Created by fabbe on 22/10/2018 - 4:56 PM.
 */
public class StructureUtils {
    public static void saveStructure(MinecraftServer server, EntityPlayer player, World world, BlockPos pos1, BlockPos pos2, Rotation rotation, String name, String author) {
        if (pos1 != null && pos2 != null && !StringUtils.isNullOrEmpty(name)) {
            StructureBoundingBox bounds = new StructureBoundingBox(pos1, pos2);
            BlockPos size = new BlockPos(bounds.maxX - bounds.minX + 1, bounds.maxY - bounds.minY + 1, bounds.maxZ - bounds.minZ + 1);
            BlockPos pos = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
            WorldServer worldServer = (WorldServer) world;
            TemplateManager templateManager = worldServer.getStructureTemplateManager();
            Template template = templateManager.getTemplate(server, new ResourceLocation(name));
            template.takeBlocksFromWorld(world, pos, size, false, Blocks.STRUCTURE_VOID);
            template.setAuthor(player.getName());
            templateManager.writeTemplate(server, new ResourceLocation(name));
            player.sendMessage(new TextComponentString("Saved " + name));
        }
    }

    public static Template loadStructure(MinecraftServer server, World world, String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            WorldServer worldServer = (WorldServer)world;
            TemplateManager templateManager = worldServer.getStructureTemplateManager();
            Template template = templateManager.get(server, new ResourceLocation(name));
            if (template == null)
                return null;
            else {
                return template;
            }
        }
        else
            return null;
    }

    public static void placeStructure(World world, BlockPos pos, Template template, Mirror mirror, Rotation rotation, boolean ignoreEntities) {
        PlacementSettings placementSettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setIgnoreEntities(ignoreEntities);
        template.addBlocksToWorldChunk(world, pos, placementSettings);
    }
}

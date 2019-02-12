package com.resimulators.simukraft.structure;

import com.resimulators.simukraft.ConfigHandler;
import com.resimulators.simukraft.Utilities;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by fabbe on 22/10/2018 - 4:56 PM.
 */
public class StructureUtils {
    /**
     *  Saves the structure to NBT
     *  @param server - The Minecraft server.
     *  @param player - The player executing the save.
     *  @param world - The Minecraft world.
     *  @param pos1 - Corner A of the structure.
     *  @param pos2 - Corner B of the structure.
     *  @param rotation - The rotation of the players facing. (Unused.)
     *  @param name - Name of the Structure.
     *  @param author - The author of the structure.
     */
    public static void saveStructure(MinecraftServer server, EntityPlayer player, World world, BlockPos pos1, BlockPos pos2, Rotation rotation, String name, String author, String category) {
        if (pos1 != null && pos2 != null && !StringUtils.isNullOrEmpty(name)) {
            StructureBoundingBox bounds = new StructureBoundingBox(pos1, pos2);
            BlockPos size = new BlockPos(bounds.maxX - bounds.minX + 1, bounds.maxY - bounds.minY + 1, bounds.maxZ - bounds.minZ + 1);
            BlockPos pos = new BlockPos(bounds.minX, bounds.minY, bounds.minZ);
            StructureHandler handler = StructureHandler.getInstance();
            TemplateManagerPlus templateManager = handler.getTemplateManager();
            TemplatePlus template = templateManager.getTemplate(server, new ResourceLocation(name));
            template.takeBlocksFromWorld(world, pos, size, false, Blocks.STRUCTURE_VOID);
            template.setAuthor(author);
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("category", category);
            compound.setDouble("price", calculatePrice(template.getBlocks(), template.getSize()));
            templateManager.writeTemplate(server, new ResourceLocation(name), compound);
            player.sendMessage(new TextComponentString("Saved " + name));
        }
    }

    /**
     * Reads the structure from storage.
     * @param server - The Minecraft Server.
     * @param world - The Minecraft World.
     * @param name - Name of the Structure.
     * @return Building template that can then be either placed immediately in the world or iterated through.
     */
    public static TemplatePlus loadStructure(MinecraftServer server, World world, String name) {
        if (!StringUtils.isNullOrEmpty(name)) {
            StructureHandler handler = StructureHandler.getInstance();
            TemplateManagerPlus templateManager = handler.getTemplateManager();
            TemplatePlus template = templateManager.get(server, new ResourceLocation(name));
            if (template == null)
                return null;
            else {
                return template;
            }
        }
        else
            return null;
    }

    /**
     * Places the structure instantly in the world.
     * @param world - The Minecraft World
     * @param pos - Position of placement in the world.
     * @param template - The template of the structure.
     * @param mirror - Mirror settings.
     * @param rotation - Rotation settings.
     * @param ignoreEntities - Ignore saved entities in the structure.
     */
    public static void placeStructure(World world, BlockPos pos, Template template, Mirror mirror, Rotation rotation, boolean ignoreEntities) {
        PlacementSettings placementSettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setIgnoreEntities(ignoreEntities);
        template.addBlocksToWorldChunk(world, pos, placementSettings);
    }


    public static double calculatePrice(List<TemplatePlus.BlockInfo> blocks, BlockPos size) {
        double price = 0;
        for (Template.BlockInfo info : blocks) {
            if (!(info.blockState.getBlock() instanceof BlockAir)) {
                try {
                    Field field = info.blockState.getBlock().getClass().getDeclaredField("blockHardness");
                    field.setAccessible(true);
                    price += field.getFloat(info.blockState.getBlock()) * (info.blockState.getBlock().getHarvestLevel(info.blockState) + 1);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        price += size.getX() * 1.5d;
        price += size.getY() * 2.2d;
        price += size.getZ() * 1.5d;
        return price * ConfigHandler.build_price;
    }
}

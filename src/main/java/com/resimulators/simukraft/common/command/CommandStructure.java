package com.resimulators.simukraft.common.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.item.ItemBlueprint;
import com.resimulators.simukraft.init.ModItems;
import com.resimulators.simukraft.structure.StructureUtils;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by Astavie on 27/01/2018 - 6:28 PM.
 */
public class CommandStructure extends CommandTreeBase {
    private static List<String> types = Stream.of("residential", "commercial", "industrial", "special").collect(Collectors.toList());

    public CommandStructure() {
        addSubcommand(new CommandStructureSave());
        addSubcommand(new CommandStructureLoad());
        addSubcommand(new CommandStructureList());
        addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "structure";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.simukraft:structure.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    public static class CommandStructureSave extends CommandBase {

        @Override
        public String getName() {
            return "save";
        }

        @Override
        public String getUsage(ICommandSender iCommandSender) {
            return "commands.simukraft:structure.save.usage";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (!server.getEntityWorld().isRemote && !StringUtils.isNullOrEmpty(args[0])) {
                if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
                    if (player.getHeldItemMainhand().getItem() == ModItems.PLANNING_SHEET) {
                        ItemStack planning_sheet = player.getHeldItemMainhand();
                        NBTTagCompound compound = planning_sheet.getTagCompound();
                        SimUKraft.getLogger().info(compound);
                        int[] coords1;
                        BlockPos pos1 = null;
                        if (compound != null) {
                            coords1 = compound.getIntArray("pos1");
                            if (coords1.length == 3)
                                pos1 = new BlockPos(coords1[0], coords1[1], coords1[2]);
                        }
                        int[] coords2;
                        BlockPos pos2 = null;
                        if (compound != null) {
                            coords2 = compound.getIntArray("pos2");
                            if (coords2.length == 3)
                                pos2 = new BlockPos(coords2[0], coords2[1], coords2[2]);
                        }
                        StructureUtils.saveStructure(server, player, player.world, pos1, pos2, Utilities.convertFromFacing(player.getHorizontalFacing()), args[0], player.getName());
                    }
                }
            }
        }
    }

    public static class CommandStructureLoad extends CommandBase {
        @Override
        public String getName() {
            return "load";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.simukraft:structure.load.usage";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (!server.getEntityWorld().isRemote && !StringUtils.isNullOrEmpty(args[0])) {
                if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) sender;
                    ItemStack itemStack = player.getHeldItemMainhand();
                    if (player.getHeldItemMainhand().getItem() == ModItems.BLUEPRINT) {
                        Template template = StructureUtils.loadStructure(server, player.world, args[0]);
                        if (template != null) {
                            ((ItemBlueprint) itemStack.getItem()).setStructure(itemStack, args[0]);
                            ((ItemBlueprint) itemStack.getItem()).setAuthor(itemStack, template.getAuthor());
                            ((ItemBlueprint) itemStack.getItem()).refreshStructure(server, player.world, itemStack);
                        }
                    }
                }
            }
        }
    }

    public static class CommandStructureList extends CommandBase {
        @Override
        public String getName() {
            return "list";
        }

        @Override
        public String getUsage(ICommandSender sender) {
            return "commands.simukraft:structure.list.usage";
        }

        @Override
        public int getRequiredPermissionLevel() {
            return 2;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
            if (new File(server.getDataDirectory() + "/saves/" + server.getWorldName() + "/structures/").exists()) {
                int page = 1;
                if (args.length == 1)
                    if (Utilities.isInteger(args[0]))
                        page = Integer.parseInt(args[0]);

                File[] files = new File(server.getDataDirectory() + "/saves/" + server.getWorldName() + "/structures/").listFiles();
                if (files != null) {
                    sender.sendMessage(new TextComponentString(ChatFormatting.GREEN + "Structures on the server! (Page " + page + "/" + ((int)Math.ceil(files.length / 10.0)) + ")"));
                    for (int i = ((page * 10) - 10); i < (page * 10); i++) {
                        if (i < files.length)
                            sender.sendMessage(new TextComponentString(Utilities.upperCaseFirstLetterInEveryWord(files[i].getName().split("_")).replace(".nbt", "") +
                                    ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + files[i].getName().replace(".nbt", "") + ChatFormatting.GRAY + "]"));
                        else break;
                    }
                }
            }
        }
    }
}
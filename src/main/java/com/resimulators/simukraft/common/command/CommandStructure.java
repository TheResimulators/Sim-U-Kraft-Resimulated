package com.resimulators.simukraft.common.command;

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
            List<String> structurelist = new ArrayList<>();
            String folder = "";
            if (args.length == 1) folder = args[0];
            else if (args.length == 0) folder = "all";
            if (args.length == 0 || args.length == 1 && types.contains(folder)) {
                Entity entity = sender.getCommandSenderEntity();
                if (entity instanceof EntityPlayer) {
                    if (!folder.equals("all")) {
                        if (new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\" + folder + "\\").exists()) {
                            File file = new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\" + folder + "\\");
                            structurelist.addAll(java.util.Arrays.asList(file.list()));
                        }
                    } else {
                        if (new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\").exists()) {
                            for (String string : types) {
                                if (new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\" + string + "\\").exists()) {
                                    File category = new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\" + string + "\\");
                                    structurelist.addAll(java.util.Arrays.asList(category.list()));
                                }

                            }
                        }
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append("Structure files: ");
                    assert structurelist != null;
                    for (String s : structurelist) {
                        builder.append(s).append(", ");
                    }
                    String str = builder.toString().replace(".struct", "");
                    sender.sendMessage(new TextComponentString(str.substring(0, str.length() - 2)));
                } else {
                    throw new WrongUsageException(getUsage(sender));
                }
            } else {
                throw new WrongUsageException(getUsage(sender));
            }

        }
    }
}
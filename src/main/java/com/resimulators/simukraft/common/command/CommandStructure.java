package com.resimulators.simukraft.common.command;

import com.resimulators.simukraft.common.item.ItemBlueprint;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;
import scala.Array;
import scala.actors.threadpool.Arrays;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by Astavie on 27/01/2018 - 6:28 PM.
 */
public class CommandStructure extends CommandTreeBase {
    private static List<String> types = Stream.of("residential", "commercial","industrial","special").collect(Collectors.toList());
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
		    System.out.println("args " + args[args.length-1]);

		    String type;
		    if (args.length == 1 || args.length == 7){
		        System.out.println("this has been called " + args.length);
		        type = "special";
            }else type = args[args.length-1];
            int i = 0;
            StructureBoundingBox bounds;
			if (args.length == 8 || type.equals("special") && args.length == 7 ) {
                i = 7  ;
                BlockPos corner0 = parseBlockPos(sender, args, 0, false);
                BlockPos corner1 = parseBlockPos(sender, args, 3, false);
                bounds = new StructureBoundingBox(corner0, corner1);
            } else if (args.length == 2 || type.equals("special")) {
			    Entity entity = sender.getCommandSenderEntity();
			    if (entity instanceof EntityPlayer) {
                    ItemStack stack = ((EntityPlayer) entity).getHeldItem(EnumHand.MAIN_HAND);
			        if (stack.getItem() == ModItems.PLANNING_SHEET) {
                        NBTTagCompound compound = stack.getTagCompound();
                        if (compound != null) {
                            int[] aPos = compound.getIntArray("pos1");
                            BlockPos corner0 = new BlockPos(aPos[0], aPos[1], aPos[2]);
                            aPos = compound.getIntArray("pos2");
                            BlockPos corner1 = new BlockPos(aPos[0], aPos[1], aPos[2]);
                            bounds = new StructureBoundingBox(corner0, corner1);
                        } else {
                            throw new WrongUsageException(getUsage(sender));
                        }
                    } else {
                        throw new WrongUsageException(getUsage(sender));
                    }
                } else {
                    throw new WrongUsageException(getUsage(sender));
                }
            } else {
                throw new WrongUsageException(getUsage(sender));
            }

			int width = bounds.getXSize();
			int height = bounds.getYSize();
			int depth = bounds.getZSize();

			int blocks = width * height * depth;
			if (blocks > 32768) throw new CommandException("commands.clone.tooManyBlocks", blocks, 32768);
			if (bounds.minY < 0 || bounds.maxY >= 256 || !sender.getEntityWorld().isAreaLoaded(bounds)) throw new CommandException("commands.clone.outOfWorld");

			IBlockState[][][] data = new IBlockState[width][height][depth];
			for (int z = 0; z < depth; z++)
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
						data[x][y][z] = sender.getEntityWorld().getBlockState(new BlockPos(x + bounds.minX, y + bounds.minY, z + bounds.minZ));

            if (!types.contains(type)){
                System.out.println("type " + types);
                throw new WrongUsageException(getUsage(sender));
            }

			if (!new File(Loader.instance().getConfigDir() + "\\simukraft\\structures" + type + "\\").exists())
                new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\"+ type + "\\").mkdir();


			if (!new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\"+ type + "\\", args[i] + ".struct").exists()) {
                try {
                    new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\"+ type + "\\", args[i] + ".struct").createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File file = new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\"+ type + "\\", args[i] + ".struct");
                new Structure(data).save(file,type);
                sender.sendMessage(new TextComponentString("Structure Saved!"));
            } else {
			    throw new CommandException("command.simukraft:structure.save.fileexists");
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
            String folder = "";
            if (args.length == 1) {folder = "special";}else folder = args[1];
            if (args.length == 2 || !folder.equals("") && args.length == 1 ) {
                Entity entity = sender.getCommandSenderEntity();
                if (entity instanceof EntityPlayer) {
                    ItemStack stack = ((EntityPlayer) entity).getHeldItem(EnumHand.MAIN_HAND);
                    if (stack.getItem() == ModItems.BLUEPRINT) {
                        if (new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\" +folder+ "\\"+  args[args.length-1] + ".struct").exists()) {
                            ((ItemBlueprint) stack.getItem()).setStructure(stack, new File(Loader.instance().getConfigDir() + "\\simukraft\\structures\\"+ folder + "\\" + args[0] + ".struct"));
                            sender.sendMessage(new TextComponentString("Structure Loaded!"));
                        } else
                            throw new WrongUsageException(getUsage(sender));
                    } else
                        throw new WrongUsageException(getUsage(sender));
                } else
                    throw new WrongUsageException(getUsage(sender));
            } else
                throw new WrongUsageException(getUsage(sender));
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
                }else{
                    throw new WrongUsageException(getUsage(sender));
                }
            }else {
                throw new WrongUsageException(getUsage(sender));
            }

        }
    }
}

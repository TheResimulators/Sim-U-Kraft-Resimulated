package com.resimulators.simukraft.common.command;

import com.resimulators.simukraft.common.block.BlockConstructorBox;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;


/**
 * Created by Astavie on 27/01/2018 - 6:28 PM.
 */
public class CommandStructure extends CommandTreeBase {

	public CommandStructure() {
		addSubcommand(new CommandStructureSave());
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
			if (args.length != 6) throw new WrongUsageException(getUsage(sender));

			BlockPos corner0 = parseBlockPos(sender, args, 0, false);
			BlockPos corner1 = parseBlockPos(sender, args, 3, false);
			StructureBoundingBox bounds = new StructureBoundingBox(corner0, corner1);

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
			new Structure(data).save(BlockConstructorBox.FILE); // TODO: Save to specified file
		}

	}

}

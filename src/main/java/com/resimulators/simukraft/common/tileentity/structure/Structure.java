package com.resimulators.simukraft.common.tileentity.structure;

import com.google.common.base.Optional;
import com.resimulators.simukraft.SimUKraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Astavie on 19/01/2018 - 7:45 PM.
 */
public class Structure {

	private final IBlockState[][][] data;
	private final int width, height, depth;
	static public String name;

	public Structure(IBlockState[][][] data) {
		this.data = data;
		this.width = data.length;
		this.height = data[0].length;
		this.depth = data[0][0].length;
	}

	public static Structure load(File file) throws StructureParseException {
		name = file.getName().replace(".struct","");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String[] dimensions = reader.readLine().split("x");
			if (dimensions.length == 3) {
				try {
					int width = Integer.parseInt(dimensions[0]);
					int height = Integer.parseInt(dimensions[1]);
					int depth = Integer.parseInt(dimensions[2]);

					String[] blockStates = reader.readLine().split(";");
					char[] blocks = reader.readLine().toCharArray();
					if (blocks.length != width * height * depth)
						throw new StructureParseException("Error parsing structure " + file.getName() + " at line 3: amount of blocks does not equal dimensions.");

					Map<Character, IBlockState> key = new HashMap<>();
					for (String string : blockStates) {
						String[] data = string.split(",");
						String[] blockData = data[0].split("=");
						if (blockData.length == 2) {
							if (blockData[0].length() == 1) {
								char id = blockData[0].charAt(0);
								Block block = Block.REGISTRY.getObject(new ResourceLocation(blockData[1]));
								if (block != Blocks.AIR || blockData[1].equals(Blocks.AIR.getRegistryName().toString())) {
									BlockStateContainer container = block.getBlockState();
									IBlockState state = block.getDefaultState();
									for (String propertyString : Arrays.copyOfRange(data, 1, data.length)) {
										String[] propertyData = propertyString.split("=");
										if (propertyData.length == 2) {
											IProperty property = container.getProperty(propertyData[0]);
											if (property != null) {
												Optional value = property.parseValue(propertyData[1]);
												if (value.isPresent())
													state = withProperty(state, property, value.get());
												else
													throw new StructureParseException("Error parsing structure " + file.getName() + " at line 2: invalid value \"" + propertyData[1] + "\" for block property \"" + propertyData[0] + "\" for block " + blockData[1] + ".");
											} else
												throw new StructureParseException("Error parsing structure " + file.getName() + " at line 2: unknown block property \"" + propertyData[0] + "\" for block " + blockData[1] + ".");
										} else
											throw new StructureParseException("Error parsing structure " + file.getName() + " at line 2: malformed block property \"" + propertyString + "\" for block " + blockData[1] + ".");
									}
									key.put(id, state);
								} else
									throw new StructureParseException("Error parsing structure " + file.getName() + " at line 2: unknown block \"" + blockData[1] + "\".");
							} else
								throw new StructureParseException("Error parsing structure " + file.getName() + " at line 2: blockstate \"" + blockData[0] + "\" must be one character long.");
						} else
							throw new StructureParseException("Error parsing structure " + file.getName() + " at line 2: malformed blockstate \"" + data[0] + "\".");
					}

					IBlockState[][][] data = new IBlockState[width][height][depth];
					int i = 0;
					for (int x = 0; x < width; x++) {
						for (int y = 0; y < height; y++) {
							for (int z = 0; z < depth; z++) {
								IBlockState state = key.get(blocks[i]);
								if (state != null) {
									data[x][y][z] = state;
									i += 1;
								} else
									throw new StructureParseException("Error parsing structure " + file.getName() + " at line 3: no blockstate was assigned to key '" + blocks[i] + "'.");
							}
						}
					}
					return new Structure(data);
				} catch (NumberFormatException e) {
					throw new StructureParseException("Error parsing structure " + file.getName() + " at line 1: malformed structure dimensions.", e);
				}
			} else
				throw new StructureParseException("Error parsing structure " + file.getName() + " at line 1: malformed structure dimensions.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>, V extends T> IBlockState withProperty(IBlockState state, IProperty property, Object value) {
		return state.withProperty((IProperty<T>) property, (V) value);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>, V extends T> String getValue(IProperty property, Object value) {
		return ((IProperty<T>) property).getName((V) value);
	}

	public boolean save(File file,String type) {
		Map<IBlockState, Character> key = new HashMap<>();
		char cur = ' ';
		StringBuilder states = new StringBuilder();
		StringBuilder blocks = new StringBuilder();
		for (IBlockState[][] a0 : data) {
			for (IBlockState[] a1 : a0) {
				for (IBlockState a2 : a1) {
					if (!key.containsKey(a2)) {
						if (cur > '~')
							return false;
						if (states.length() > 0)
							states.append(";");
						states.append(cur).append("=").append(a2.getBlock().getRegistryName());
						for (Map.Entry<IProperty<?>, Comparable<?>> entry : a2.getProperties().entrySet())
							states.append(",").append(entry.getKey().getName()).append("=").append(getValue(entry.getKey(), entry.getValue()));
						key.put(a2, cur);
						do
							cur += 1;
						while (cur == '=' || cur == ',' || cur == ';');
					}
					blocks.append(key.get(a2));
				}
			}
		}
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.println(width + "x" + height + "x" + depth);
			writer.println(states);
			writer.println(blocks);
			writer.println(type);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}

	public IBlockState getBlock(int x, int y, int z) {
		return data[x][y][z];
	}

	public ItemStack getItemStack(int x, int y, int z) {
		IBlockState temp = data[x][y][z];
		return temp.getBlock().getItem(null, null, temp);
	}

	public String getName() {
		return name;
	}
}

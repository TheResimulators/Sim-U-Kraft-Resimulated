package com.resimulators.simukraft.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fabbe on 04/02/2018 - 4:56 PM.
 */
public class ItemPlanningSheet extends ItemBase {
    public ItemPlanningSheet(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                if (playerIn.rayTrace(5, 200) != null) {
                    ItemStack stack = playerIn.getHeldItem(handIn);
                    NBTTagCompound compound = stack.getTagCompound();
                    if (compound != null) {
                        compound.removeTag("pos1");
                        compound.removeTag("pos2");
                        compound.removeTag("volume");
                        compound.removeTag("sizes");
                        stack.setTagCompound(compound);
                        pos1 = null;
                        playerIn.sendStatusMessage(new TextComponentString("Positions reset"), true);
                    }
                }
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    private BlockPos pos1;
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound compound = stack.getTagCompound();
            if (compound == null)
                compound = new NBTTagCompound();

            if (!compound.hasKey("pos1")) {
                pos1 = pos;
                compound.setIntArray("pos1", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.sendStatusMessage(new TextComponentString("Position 1 set: " + Arrays.toString(compound.getIntArray("pos1"))), true);
                stack.setTagCompound(compound);
            } else if (!compound.hasKey("pos2")) {
                StructureBoundingBox bounds = new StructureBoundingBox(pos1, pos);
                int volume = calcVolume(bounds);
                compound.setIntArray("sizes", new int[]{bounds.getXSize(), bounds.getYSize(), bounds.getZSize()});
                compound.setInteger("volume", volume);
                if (volume <= 2097152) {
                    compound.setIntArray("pos2", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                    player.sendStatusMessage(new TextComponentString("Position 2 set: " + Arrays.toString(compound.getIntArray("pos2"))), true);
                    stack.setTagCompound(compound);
                } else {
                    player.sendStatusMessage(new TextComponentString("Too many blocks in the specified area (" + calcVolume(new StructureBoundingBox(pos1, pos)) + " > 2097152)"), true);
                }
            } else {
                player.sendStatusMessage(new TextComponentString("Positions already set!"), true);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound compound = stack.getTagCompound();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add("To use this, right click the item on the " + ChatFormatting.DARK_PURPLE + "first");
            tooltip.add("corner of the structure, then right click the");
            tooltip.add(ChatFormatting.DARK_PURPLE + "second" + ChatFormatting.GRAY + " corner of the structure.");
        } else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            tooltip.add(ChatFormatting.DARK_AQUA + "Information:");
            if (compound != null) {
                if (compound.hasKey("volume"))
                    tooltip.add("Volume: " + ChatFormatting.DARK_PURPLE + compound.getInteger("volume"));
                if (compound.hasKey("sizes")) {
                    int[] sizes = compound.getIntArray("sizes");
                    if (sizes.length == 3) {
                        tooltip.add("Size: " + Utilities.formatBlockPos(new BlockPos(sizes[0], sizes[1], sizes[2])));
                    }
                }
            } else {
                tooltip.add("No positions set.");
            }
        } else {
            tooltip.add(ChatFormatting.DARK_AQUA + "Used to save structures!");
            if (compound != null) {
                if (compound.hasKey("pos1"))
                    tooltip.add("Position 1: " + Utilities.formatBlockPos(getBlockPos1(stack)));
                else
                    tooltip.add("Position 1: " + ChatFormatting.DARK_RED + "Not set");
                if (compound.hasKey("pos2"))
                    tooltip.add("Position 2: " + Utilities.formatBlockPos(getBlockPos2(stack)));
                else
                    tooltip.add("Position 2: " + ChatFormatting.DARK_RED + "Not set");
            } else {
                tooltip.add("Position 1: " + ChatFormatting.DARK_RED + "Not set");
                tooltip.add("Position 2: " + ChatFormatting.DARK_RED + "Not set");
            }
            tooltip.add(ChatFormatting.DARK_AQUA + "Hold '" + ChatFormatting.GOLD + "left shift" + ChatFormatting.DARK_AQUA + "' for instructions.");
            tooltip.add(ChatFormatting.DARK_AQUA + "Hold '" + ChatFormatting.GOLD + "left control" + ChatFormatting.DARK_AQUA + "' for information.");
        }
    }

    public BlockPos getBlockPos1(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            int[] aPos = compound.getIntArray("pos1");
            if (aPos.length != 3)
                return null;
            return new BlockPos(aPos[0], aPos[1], aPos[2]);
        } else
            return null;
    }

    public BlockPos getBlockPos2(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            int[] aPos = compound.getIntArray("pos2");
            if (aPos.length != 3)
                return null;
            return new BlockPos(aPos[0], aPos[1], aPos[2]);
        } else
            return null;
    }

    public StructureBoundingBox getStructureBounds(ItemStack stack) {
        BlockPos pos1 = getBlockPos1(stack);
        BlockPos pos2 = getBlockPos2(stack);
        return new StructureBoundingBox(pos1, pos2);
    }

    private int calcVolume(StructureBoundingBox bounds) {
        return bounds.getXSize() * bounds.getYSize() * bounds.getZSize();
    }
}

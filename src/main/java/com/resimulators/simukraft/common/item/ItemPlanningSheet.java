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
                        stack.setTagCompound(compound);
                    }
                }
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound compound = stack.getTagCompound();
            if (compound == null)
                compound = new NBTTagCompound();

            if (!compound.hasKey("pos1")) {
                compound.setIntArray("pos1", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.sendMessage(new TextComponentString("Position 1 set: " + Arrays.toString(compound.getIntArray("pos1"))));
                stack.setTagCompound(compound);
            } else if (!compound.hasKey("pos2")) {
                compound.setIntArray("pos2", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.sendMessage(new TextComponentString("Position 2 set: " + Arrays.toString(compound.getIntArray("pos2"))));
                stack.setTagCompound(compound);
            } else {
                player.sendMessage(new TextComponentString("Positions already set!"));
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            if (compound.hasKey("pos1"))
                tooltip.add("Position 1: " + ChatFormatting.DARK_PURPLE + Utilities.formatBlockPos(getBlockPos1(stack)));
            else
                tooltip.add("Position 1: " + ChatFormatting.DARK_PURPLE + "Not set");
            if (compound.hasKey("pos2"))
                tooltip.add("Position 2: " + ChatFormatting.DARK_PURPLE + Utilities.formatBlockPos(getBlockPos2(stack)));
            else
                tooltip.add("Position 2: " + ChatFormatting.DARK_PURPLE + "Not set");
        } else {
            tooltip.add("No positions added!");
        }
    }

    public BlockPos getBlockPos1(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            int[] aPos = compound.getIntArray("pos1");
            return new BlockPos(aPos[0], aPos[1], aPos[2]);
        } else
            return null;
    }

    public BlockPos getBlockPos2(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound != null) {
            int[] aPos = compound.getIntArray("pos2");
            return new BlockPos(aPos[0], aPos[1], aPos[2]);
        } else
            return null;
    }

    public StructureBoundingBox getStructureBounds(ItemStack stack) {
        BlockPos pos1 = getBlockPos1(stack);
        BlockPos pos2 = getBlockPos2(stack);
        return new StructureBoundingBox(pos1, pos2);
    }
}

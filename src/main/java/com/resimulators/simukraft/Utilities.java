package com.resimulators.simukraft;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by fabbe on 04/02/2018 - 5:32 PM.
 */
public class Utilities {
    public static String formatBlockPos(BlockPos pos) {
        return ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GOLD + "X" + ChatFormatting.GRAY + ": " + ChatFormatting.DARK_PURPLE + pos.getX() + ChatFormatting.DARK_GRAY + "] ["
                + ChatFormatting.GOLD + "Y" + ChatFormatting.GRAY + ": " + ChatFormatting.DARK_PURPLE + pos.getY() + ChatFormatting.DARK_GRAY + "] [" +
                ChatFormatting.GOLD + "Z" + ChatFormatting.GRAY + ": " + ChatFormatting.DARK_PURPLE + pos.getZ() + ChatFormatting.DARK_GRAY + "]";
    }

    public static String upperCaseFirstLetter(String str) {
        String s1 = str.substring(0, 1).toUpperCase();
        return s1 + str.substring(1);
    }

    public static String upperCaseFirstLetterInEveryWord(String[] str) {
        StringBuilder builder = new StringBuilder();
        for (String aStr : str) {
            builder.append(aStr.substring(0, 1).toUpperCase()).append(aStr.substring(1)).append(" ");
        }
        return builder.toString();
    }

    public static boolean destroyBlock(World world, BlockPos pos, boolean dropBlock, boolean dropTrueBlock, boolean soundParticles) {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block.isAir(iblockstate, world, pos))
            return false;
        else {
            if (soundParticles)
                world.playEvent(2001, pos, Block.getStateId(iblockstate));

            if (dropBlock && !dropTrueBlock)
                block.dropBlockAsItem(world, pos, iblockstate, 0);
            else if (dropTrueBlock)
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Item.getItemFromBlock(block))));

            return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public static Rotation convertFromFacing(EnumFacing facing) {
        if (facing == null) {
            return Rotation.NONE;
        }
        switch(facing) {
            case NORTH:
                return Rotation.NONE;
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
        }
        return Rotation.NONE;
    }

    public static EnumFacing convertToFacing(Rotation rotation) {
        switch(rotation) {
            case NONE:
                return EnumFacing.NORTH;
            case CLOCKWISE_90:
                return EnumFacing.EAST;
            case CLOCKWISE_180:
                return EnumFacing.SOUTH;
            case COUNTERCLOCKWISE_90:
                return EnumFacing.WEST;
        }
        return EnumFacing.NORTH;
    }

    public static int convertToInt(Rotation rotation) {
        switch(rotation) {
            case NONE:
                return 0;
            case CLOCKWISE_90:
                return 90;
            case CLOCKWISE_180:
                return 180;
            case COUNTERCLOCKWISE_90:
                return -90;
        }
        return 0;
    }

    public static Rotation convertFromInt(int rotation){

        switch(rotation){
            case 0:
                return Rotation.NONE;
            case 90:
                return Rotation.CLOCKWISE_90;
            case 180:
                return Rotation.CLOCKWISE_180;
            case -90:
                return Rotation.COUNTERCLOCKWISE_90;


        }
        return Rotation.NONE;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}

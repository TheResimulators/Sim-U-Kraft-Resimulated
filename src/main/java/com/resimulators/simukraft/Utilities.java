package com.resimulators.simukraft;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.math.BlockPos;

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
}

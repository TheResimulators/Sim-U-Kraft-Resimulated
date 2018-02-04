package com.resimulators.simukraft.debug;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by fabbe on 28/01/2018 - 12:24 AM.
 */
public class ItemDebug extends ItemBase {
    public ItemDebug(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!playerIn.world.isRemote) {
            if (target instanceof EntitySim) {
                playerIn.sendMessage(new TextComponentString("Profession: " + ((EntitySim) target).getProfession()));
                playerIn.sendMessage(new TextComponentString("Gender: " + ((EntitySim) target).getGender()));
                playerIn.sendMessage(new TextComponentString("Variation: " + ((EntitySim) target).getVariation()));
            }
        }

        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    }
}

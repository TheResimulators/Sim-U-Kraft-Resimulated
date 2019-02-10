package com.resimulators.simukraft.debug;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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
                if (!playerIn.isSneaking() && !playerIn.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty()) {
                    ((EntitySim) target).EquipItemStack(EnumHand.MAIN_HAND, playerIn.getHeldItem(EnumHand.OFF_HAND));
                }

                if(playerIn.isSneaking()) {
                    playerIn.sendMessage(new TextComponentString("Item in Slot 10: " + playerIn.inventory.getStackInSlot(10)));
                    playerIn.sendMessage(new TextComponentString("Profession: " + ((EntitySim) target).getProfession()));
                    playerIn.sendMessage(new TextComponentString("Gender: " + ((EntitySim) target).getGender()));
                    playerIn.sendMessage(new TextComponentString("Primary Hand: " + (target.getPrimaryHand() == EnumHandSide.RIGHT ? "Right" : "Left")));
                    playerIn.sendMessage(new TextComponentString("Variation: " + ((EntitySim) target).getVariation()));
                }
                else ((EntitySim) target).unEquipItemStack(EnumHand.MAIN_HAND);
            }
        }

        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Right clicking a Sim with this will put the");
        tooltip.add("item in the players off hand into the Sim's main hand.");
        tooltip.add("");
        tooltip.add("Shift + Right Click will print some info about the Sim in chat.");
    }
}

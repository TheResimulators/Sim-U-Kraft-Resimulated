package com.resimulators.simukraft.debug;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
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
                playerIn.sendMessage(new TextComponentString("Profession: " + ((EntitySim) target).getProfession()));
                playerIn.sendMessage(new TextComponentString("Gender: " + ((EntitySim) target).getGender()));
                playerIn.sendMessage(new TextComponentString("Variation: " + ((EntitySim) target).getVariation()));

                if(playerIn.isSneaking()) {
                    playerIn.sendMessage(new TextComponentString("Item in Slot 10: " + playerIn.inventory.getStackInSlot(10)));
                    ((EntitySim) target).EquipItemStack(EnumHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD, 1));
                }
                else ((EntitySim) target).unEquipItemStack(EnumHand.MAIN_HAND);
            }
        }

        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    }
}

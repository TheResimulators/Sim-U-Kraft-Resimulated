package com.resimulators.simukraft.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

/**
 * Created by fabbe on 03/02/2018 - 5:14 PM.
 */
public class ItemBlueprint extends ItemBase {
    public ItemBlueprint(String name, CreativeTabs tab) {
        super(name, tab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote && playerIn.isSneaking() && playerIn.rayTrace(6, 20) == null) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            NBTTagCompound compound = stack.getTagCompound();
            if (compound != null) {
                compound.removeTag("structurename");
                compound.removeTag("structurefile");
                compound.removeTag("posx");
                compound.removeTag("posy");
                compound.removeTag("posz");
                stack.setTagCompound(compound);
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && player.isSneaking()) {
            this.setStartPos(player.getHeldItem(hand), pos.offset(facing));
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!(target instanceof EntitySim))
            return false;
        if (((EntitySim) target).getProfession() != 1) {
            return false;
        }

        //Interaction logic begin

        SimUKraft.getLogger().info("Building");
        ((EntitySim) target).setStructure(Structure.load(new File(getStructure(stack))));
        ((EntitySim) target).setAllowedToBuild(true);
        ((EntitySim) target).setStartPos(getStartPos(stack));

        //Interaction logic end

        if (!playerIn.isCreative())
            stack.shrink(1);

        return true;
    }

    public void setStartPos(ItemStack stack, BlockPos pos) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setInteger("posx", pos.getX());
        compound.setInteger("posy", pos.getY());
        compound.setInteger("posz", pos.getZ());

        stack.setTagCompound(compound);
    }

    public BlockPos getStartPos(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        int posX = compound.getInteger("posx");
        int posY = compound.getInteger("posy");
        int posZ = compound.getInteger("posz");

        return new BlockPos(posX, posY, posZ);
    }

    public void setStructure(ItemStack stack, File structure) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        String structureFileName = structure.getAbsolutePath();
        String structureName = structure.getName().replace(".struct", "");

        compound.setString("structurename", structureName);
        compound.setString("structurefile", structureFileName);

        stack.setTagCompound(compound);
    }

    public String getStructure(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return "";

        return compound.getString("structurefile");
    }

    public String getStructureName(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return "";

        return compound.getString("structurename");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add("While holding the item in your hand, ");
            tooltip.add("run the '" + ChatFormatting.GOLD + "/structure load [name]" + ChatFormatting.GRAY + "' command.");
            tooltip.add("Right click the ground where you want");
            tooltip.add("the sim to build it. Then right click a");
            tooltip.add("builder sim with the blueprint.");
        } else {
            tooltip.add(ChatFormatting.DARK_AQUA + "Used to make sims build structures!");
            if (getStructureName(stack).length() > 0)
                tooltip.add("Structure: " + ChatFormatting.DARK_PURPLE + Utilities.upperCaseFirstLetterInEveryWord(getStructureName(stack).split("_")));
            else
                tooltip.add("Structure: " + ChatFormatting.DARK_RED + "No structure set");
            BlockPos pos = getStartPos(stack);
            tooltip.add("Build Position: " + Utilities.formatBlockPos(pos));
            tooltip.add(ChatFormatting.DARK_AQUA + "Hold '" + ChatFormatting.GOLD + "left shift" + ChatFormatting.DARK_AQUA + "' for more information.");
        }
    }
}

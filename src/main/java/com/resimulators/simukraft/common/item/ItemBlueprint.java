package com.resimulators.simukraft.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.structure.StructureUtils;
import net.minecraft.block.BlockChest;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
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
                compound.removeTag("name");
                compound.removeTag("author");
                compound.removeTag("posx");
                compound.removeTag("posy");
                compound.removeTag("posz");
                compound.removeTag("cposx");
                compound.removeTag("cposy");
                compound.removeTag("cposz");
                compound.removeTag("facing");
                compound.removeTag("facing2");
                stack.setTagCompound(compound);
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && player.isSneaking()) {
            if (worldIn.getBlockState(pos).getBlock() instanceof BlockChest) {
                this.setChestPos(player.getHeldItem(hand), pos);
            } else {
                this.setStartPos(player.getHeldItem(hand), pos.offset(facing));
                this.setRotation(player.getHeldItem(hand), player.getAdjustedHorizontalFacing());
            }
        } else if (!worldIn.isRemote && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && false) {
            this.setStartPos(player.getHeldItem(hand), pos.offset(facing));
            this.setRotation(player.getHeldItem(hand), player.getAdjustedHorizontalFacing());
            StructureUtils.placeStructure(worldIn, getStartPos(player.getHeldItem(hand)), StructureUtils.loadStructure(player.getServer(), player.world, getStructure(player.getHeldItem(hand))), Mirror.NONE, Utilities.convertFromFacing(getRotation(player.getHeldItem(hand))), true);
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (!playerIn.world.isRemote) {
            if (!(target instanceof EntitySim))
                return false;
            if (((EntitySim) target).getProfession() != 1) {
                return false;
            }

            //Interaction logic begin

            SimUKraft.getLogger().info("Building");
            ((EntitySim) target).setStructure(StructureUtils.loadStructure(playerIn.getServer(), playerIn.world, getStructure(stack)));
            ((EntitySim) target).setAllowedToBuild(true);
            ((EntitySim) target).setStartPos(getStartPos(stack));
            ((EntitySim) target).setFacing(getRotation(stack));

            //Interaction logic end

            if (!playerIn.isCreative())
                stack.shrink(1);

            return true;
        }
        return false;
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

    public void setChestPos(ItemStack stack, BlockPos pos) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setInteger("cposx", pos.getX());
        compound.setInteger("cposy", pos.getY());
        compound.setInteger("cposz", pos.getZ());

        stack.setTagCompound(compound);
    }

    public BlockPos getChestPos(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        int posX = compound.getInteger("cposx");
        int posY = compound.getInteger("cposy");
        int posZ = compound.getInteger("cposz");

        return new BlockPos(posX, posY, posZ);
    }

    public void setRotation(ItemStack stack, EnumFacing facing) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setString("facing", facing.getName());

        stack.setTagCompound(compound);
    }

    public EnumFacing getRotation(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        String facing = compound.getString("facing");

        return EnumFacing.byName(facing);
    }

    public void setAddRotation(ItemStack stack, EnumFacing facing) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setString("facing2", facing.getName());

        stack.setTagCompound(compound);
    }

    public EnumFacing getAddRotation(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        String facing = compound.getString("facing2");

        return EnumFacing.byName(facing);
    }

    public void setStructure(ItemStack stack, String name) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setString("name", name);

        stack.setTagCompound(compound);
    }

    public String getStructure(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return "";

        return compound.getString("name");
    }

    public void setAuthor(ItemStack stack, String author) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setString("author", author);
    }

    public String getAuthor(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return "";
        return compound.getString("author");
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
            if (getStructure(stack).length() > 0) {
                tooltip.add("Structure: " + ChatFormatting.DARK_PURPLE + Utilities.upperCaseFirstLetterInEveryWord(getStructure(stack).split("_")));
                tooltip.add("Author: " + ChatFormatting.DARK_PURPLE + getAuthor(stack));
            }
            else
                tooltip.add("Structure: " + ChatFormatting.DARK_RED + "No structure set");
            BlockPos pos = getStartPos(stack);
            tooltip.add("Build Position: " + Utilities.formatBlockPos(pos));
            EnumFacing facing = getRotation(stack);
            if (facing == null)
                facing = EnumFacing.NORTH;
            tooltip.add("Facing: " + ChatFormatting.DARK_PURPLE + Utilities.upperCaseFirstLetter(facing.getName()));
            tooltip.add(ChatFormatting.DARK_AQUA + "Hold '" + ChatFormatting.GOLD + "left shift" + ChatFormatting.DARK_AQUA + "' for more information.");
        }
    }
}

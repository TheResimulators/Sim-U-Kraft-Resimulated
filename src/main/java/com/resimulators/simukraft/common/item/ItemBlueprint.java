package com.resimulators.simukraft.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import com.resimulators.simukraft.event.RenderHandEvent;
import com.resimulators.simukraft.structure.StructureUtils;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.block.BlockChest;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by fabbe on 03/02/2018 - 5:14 PM.
 */
public class ItemBlueprint extends ItemBase {
    private TemplatePlus template;
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
    public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entity, itemSlot, isSelected);
        if ((this.template == null || getStructure(stack).equals("")) && !worldIn.isRemote)
            this.template = StructureUtils.loadStructure(entity.getServer(), entity.world, getStructure(stack));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && player.isSneaking()) {
            if (worldIn.getBlockState(pos).getBlock() instanceof BlockChest) {
                this.setChestPos(player.getHeldItem(hand), pos);
            } else {
                this.setStartPos(player.getHeldItem(hand), pos.offset(facing));
                //this.setRotation(player.getHeldItem(hand), player.getAdjustedHorizontalFacing());
                this.setCurrentRotation(player.getHeldItem(hand), player.getAdjustedHorizontalFacing());

            }
        } else if (!worldIn.isRemote && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && false) {
            this.setStartPos(player.getHeldItem(hand), pos.offset(facing));
            this.setCurrentRotation(player.getHeldItem(hand), player.getAdjustedHorizontalFacing());
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

    public void setCurrentRotation(ItemStack stack, EnumFacing facing) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        compound.setString("currentfacing", facing.getName());

        stack.setTagCompound(compound);
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
        stack.setTagCompound(compound);
    }

    public String getAuthor(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            return "";
        return compound.getString("author");
    }

    public void setCategory(ItemStack stack, String category) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        compound.setString("category", category);
        stack.setTagCompound(compound);
    }

    public String getCategory(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        return compound.getString("category");
    }

    public void setPrice(ItemStack stack, double price) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        compound.setDouble("price", price);
        stack.setTagCompound(compound);
    }

    public double getPrice(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        return compound.getDouble("price");
    }

    public TemplatePlus getTemplate() {
        return template;
    }

    public void refreshStructure(MinecraftServer server, World world, ItemStack stack) {
        this.template = StructureUtils.loadStructure(server, world, getStructure(stack));
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

            if (getCategory(stack).equals(""))
                tooltip.add("Category: " + ChatFormatting.DARK_RED + "Category not set.");
            else
                tooltip.add("Category: " + ChatFormatting.DARK_PURPLE + Utilities.upperCaseFirstLetterInEveryWord(getCategory(stack).split("_")));

            if (getPrice(stack) == 0)
                tooltip.add("Price: " + ChatFormatting.DARK_RED + "FREE");
            else
                tooltip.add("Price: " + ChatFormatting.DARK_PURPLE + "$" + getPrice(stack));
            TemplatePlus template = getTemplate();
            if (template != null)
                tooltip.add("Size: " + Utilities.formatBlockPos(template.getSize()));
            BlockPos pos = getStartPos(stack);
            tooltip.add("Build Position: " + Utilities.formatBlockPos(pos));
            BlockPos chestPos = getChestPos(stack);
            tooltip.add("Chest Position: " + Utilities.formatBlockPos(chestPos));
            EnumFacing facing = getRotation(stack);
            if (facing == null)
                facing = EnumFacing.NORTH;
            tooltip.add("Facing: " + ChatFormatting.DARK_PURPLE + Utilities.upperCaseFirstLetter(facing.getName()));
            tooltip.add(ChatFormatting.DARK_AQUA + "Hold '" + ChatFormatting.GOLD + "left shift" + ChatFormatting.DARK_AQUA + "' for more information.");
        }
    }

    public EnumFacing getCurrentRotation(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        String facing = compound.getString("currentfacing");

        return EnumFacing.byName(facing);
    }

}

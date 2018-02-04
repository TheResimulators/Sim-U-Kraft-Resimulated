package com.resimulators.simukraft.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.SimUTab;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.init.ModBlocks;
import com.resimulators.simukraft.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
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
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            if (player.getHeldItem(hand).getItem() == ModItems.BLUEPRINT) {
                if (worldIn.getBlockState(pos).getBlock() == ModBlocks.CONSTRUCTOR_BOX && player.isSneaking()) {
                    this.setStructure(player.getHeldItem(hand), new File(Loader.instance().getConfigDir() + "\\" + "simukraft" + ".struct"), "Test Structure");
                    return EnumActionResult.SUCCESS;
                }
            }
        }

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

    public void setStructure(ItemStack stack, File structure, String structureName) {
        NBTTagCompound compound = stack.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();

        String structureFileName = structure.getAbsolutePath();

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
        tooltip.add("Structure: " + ChatFormatting.DARK_PURPLE + getStructureName(stack));
        BlockPos pos = getStartPos(stack);
        tooltip.add("Build Position: " + ChatFormatting.DARK_GRAY + "[" + ChatFormatting.GOLD + "X" + ChatFormatting.GRAY + ": " + ChatFormatting.DARK_PURPLE + pos.getX() + ChatFormatting.DARK_GRAY + "] ["
                + ChatFormatting.GOLD + "Y" + ChatFormatting.GRAY + ": " + ChatFormatting.DARK_PURPLE + pos.getY() + ChatFormatting.DARK_GRAY + "] [" +
                ChatFormatting.GOLD + "Z" + ChatFormatting.GRAY + ": " + ChatFormatting.DARK_PURPLE + pos.getZ() + ChatFormatting.DARK_GRAY + "]");
    }
}

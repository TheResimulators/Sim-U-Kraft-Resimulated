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
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            EntitySim sim = new EntitySim(worldIn);
            sim.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
            sim.setProfession(1);
            sim.setStructure(new Structure(StructureStore.debugStructure1));
            sim.setAllowedToBuild(true);
            sim.setFemale(randomizeGender());
            worldIn.spawnEntity(sim);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntitySim) {
            ((EntitySim) target).setProfession(1);
            ((EntitySim) target).setStructure(Structure.load(new File(Loader.instance().getConfigDir(), Reference.MOD_ID + ".struct")));
            ((EntitySim) target).setAllowedToBuild(true);
        }

        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(ChatFormatting.LIGHT_PURPLE + "Right click on ground to spawn a random Sim ready to build.");
        tooltip.add(ChatFormatting.LIGHT_PURPLE + "Right click on Sim to initialize the building AI ones more.");
    }

    private boolean randomizeGender() {
        Random rand = new Random();
        int dice = rand.nextInt(2);
        return dice != 0 && dice == 1;
    }
}

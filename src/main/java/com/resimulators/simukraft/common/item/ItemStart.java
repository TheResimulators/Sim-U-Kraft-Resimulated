package com.resimulators.simukraft.common.item;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.item.base.ItemBase;
import com.resimulators.simukraft.network.ItemRightClickedPacket;
import com.resimulators.simukraft.network.PacketHandler;
import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemStart extends ItemBase {
    public ItemStart(String name, CreativeTabs tab) {
        super(name, tab);
    }
    private boolean isdedicated = false;
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack =  playerIn.getHeldItem(handIn);
        if (worldIn.isRemote){
        System.out.println("Item right clicked");
        PacketHandler.INSTANCE.sendToServer(new ItemRightClickedPacket(handIn,stack));}
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);}
}

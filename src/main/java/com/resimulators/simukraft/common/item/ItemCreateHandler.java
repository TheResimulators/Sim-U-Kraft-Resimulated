package com.resimulators.simukraft.common.item;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.gui.GuiStart;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.init.ModItems;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.StartingGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.swing.text.html.MinimalHTMLWriter;
import java.time.chrono.MinguoEra;


@Mod.EventBusSubscriber
public class ItemCreateHandler {
    private static World world;
    private static EntityPlayer player;


    @SubscribeEvent
    public static void ItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getItem().getRegistryName().getResourceDomain().equals(Reference.MOD_ID)) {
            player = event.player;
            world = event.player.world;
            player.addItemStackToInventory(new ItemStack(ModItems.STARTING_ITEM));
        }
    }}

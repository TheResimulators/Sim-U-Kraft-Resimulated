package com.resimulators.simukraft.common.item;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;



@Mod.EventBusSubscriber
public class ItemCreateHandler {
    private static World world;
    private static EntityPlayer player;


    @SubscribeEvent
    public static void ItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getItem().getRegistryName().getNamespace().equals(Reference.MOD_ID)) {
            player = event.player;
            world = event.player.world;
            player.addItemStackToInventory(new ItemStack(ModItems.VILLAGE_FOUNDER));
        }
    }}

package com.resimulators.simukraft.common.item;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.gui.GuiStart;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.StartingGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.swing.text.html.MinimalHTMLWriter;
import java.time.chrono.MinguoEra;


@Mod.EventBusSubscriber
public class ItemCreateHandler {
    private static GuiButton sim;
    private static boolean itemCrafted;
    private static World world;
    private static EntityPlayer player;

    public static void setItemCrafted(boolean crafted) {
        itemCrafted = crafted;
    }

    @SubscribeEvent
    public static void ItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        System.out.println(event.crafting.getItem().getRegistryName().getResourceDomain());
        if (event.crafting.getItem().getRegistryName().getResourceDomain().equals(Reference.MOD_ID)) {
            setItemCrafted(true);
            player = event.player;
            world = event.player.world;
                Minecraft.getMinecraft().currentScreen.initGui();
        }
    }


    @SubscribeEvent
    public static void OpenGui(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof GuiCrafting) {
            if (itemCrafted && !SaveSimData.get(world).isEnabled()) {
                if (!event.getButtonList().contains(sim)) {
                    int xpos = ((GuiCrafting) event.getGui()).getGuiLeft() + ((GuiCrafting) event.getGui()).getXSize() / 2 - 30;
                    int ypos = ((GuiCrafting) event.getGui()).getGuiTop() - 20;
                    event.getButtonList().add(sim = new GuiButton(event.getButtonList().size(), xpos, ypos, 60, 20, "Sim_U_Kraft"));
    }}}}

    @SubscribeEvent
    public static void Buttonpressed(GuiScreenEvent.ActionPerformedEvent event) {
        if (event.getButton() == sim)
            if (itemCrafted && !SaveSimData.get(world).isEnabled())
            {
                PacketHandler.INSTANCE.sendToServer(new StartingGuiPacket());
                SaveSimData.get(world).setEnabled(true);
            }
        }
    }

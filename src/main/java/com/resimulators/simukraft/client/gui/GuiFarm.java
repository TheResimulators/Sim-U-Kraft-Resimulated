package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.network.Hiring_packet;
import com.resimulators.simukraft.network.PacketHandler;
import com.sun.javafx.scene.control.skin.PaginationSkin;
import net.minecraft.client.gui.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import scala.Int;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class GuiFarm extends GuiScreen {
    boolean hiredSim = false;
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    private int buttonWidth = 200;
    private int xOffset = 105;
    private int yOffset = 10;
    private int x = 0;
    private String status = "";
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton buttoni;
    private int mouseX;
    private int mouseY;
    private HashMap<EntitySim, GuiButton> buttons = new HashMap<>();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        if (!status.equals("hiring")) {
            drawString(mc.fontRenderer, "Farmer", (width / 2) - (buttonWidth / 3), height / 4 - 10, Color.WHITE.getRGB());
            button1.visible = true;
            button1.enabled = true;
        }
        for (GuiButton value : buttons.values()) {
            if (status.equals("hiring"))
            {
            value.enabled = true;
            value.visible = true;
            } else {
                value.enabled = false;
                value.visible = false;
            }
            if (value.y > height- 80)
            {
                value.enabled = false;
                value.visible = false;
            }
    }


        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
}


    @Override
    public void initGui() {
        buttonList.add(button1 = new GuiButton(0, width / 2 - buttonWidth / 2, height - 90, "Hire"));
        buttonList.add(button2 = new GuiButton(1, width / 2 - buttonWidth / 2, height - 50, "Cancel"));
        System.out.println("button 1 " + buttonList.get(0));
        System.out.println("button 2 " + buttonList.get(1));
        x = 90;
        int y = 15;
        int increment = 0;
        int i = 2;
        int button;
        Set<UUID> unemployed_sims = SimEventHandler.getUnemployedSims();
        for (UUID sims : unemployed_sims)
        {
            EntitySim sim = (EntitySim) server.getEntityFromUuid(sims);
            String name = sim.getCustomNameTag();
            int xpos = increment*x+10;
            if (xpos > width-30){
                y += 20;
                increment = 0;

            }
            xpos = increment*x+10;
            buttonList.add(new GuiButton(i,xpos,y,100,20,name));
            System.out.println(buttonList.get(i));
            buttons.put(sim,buttonList.get(i));
            i++;
            increment++;


        }
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                System.out.println(button.id);
                status = "hiring";
                System.out.println("status updated");
                updateButtons();
                break;
            case 1:
                this.mc.displayGuiScreen(null);

            default:
                System.out.println(button.id);
                remove_HireButtons(button.id);

        }
        super.actionPerformed(button);
    }

    private void updateButtons(){
        if (status.equals("hiring")){
            button1.visible = false;
            button1.enabled = false;

        } }

    private void remove_HireButtons(int id){
        GuiButton button = buttonList.get(id);
        button.visible = false;
        button.enabled = false;
        if (buttons.containsValue(button)){
        EntitySim sim = (EntitySim) getKeyFromValue(buttons,button);
        UUID sim_id = sim.getPersistentID();
        PacketHandler.INSTANCE.sendToServer(new Hiring_packet(sim_id,2));
        }

    }
    private static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }}


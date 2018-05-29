package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.tileentity.TileFarm;
import com.resimulators.simukraft.network.HiringPacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GuiHire extends GuiScreen {
    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    TileFarm tileEntity;
    private int mouseX;
    private int mouseY;
    private int buttonWidth = 140;
    private String status = "";
    private SimButton button;
    private GuiButton hire_button;
    private GuiButton cancel_button;
    private Set<UUID> sims = new HashSet<>();
    private String profession = "";
    public GuiHire(TileFarm tileEntity){
        this.tileEntity = tileEntity;
        this.profession = tileEntity.profession;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        if (!status.equals("hiring")) {
            drawString(mc.fontRenderer, profession, (width / 2)-fontRenderer.getStringWidth(profession)/2, height / 4 - 10, Color.WHITE.getRGB());
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);

    }
    @Override
    public void updateScreen()
    {
        for (GuiButton button: buttonList)
        {
            if (button instanceof SimButton )
            {
                if (!status.equals("hiring"))
                {
                    button.visible = false;
                    button.enabled = false;
                }else
                    {
                        button.visible = true;
                        button.enabled = true;
                if (((SimButton) button).clicked)
                {
                    button.visible = false;
                    button.enabled = false;
                }else
                {
                    button.visible = true;
                    button.enabled = true;
                }
            }
        }}}

    @Override
    public void initGui() {
        buttonList.clear();
        buttonList.add(hire_button = new GuiButton(0,width/2 - buttonWidth/2,height-60,buttonWidth,20,"Hire"));
        buttonList.add(cancel_button = new GuiButton(1,width/2 - buttonWidth/2,height-30,buttonWidth,20,"Cancel"));
        if(status.equals("hiring"))
        {
            hire_button.enabled = false;
            hire_button.visible = false;
            cancel_button.enabled = false;
            cancel_button.visible = false;
        }
        int ypos = 30;
        int i = 0;
        int pos = 0;
        sims.clear();
        for (UUID id: SimEventHandler.getWorldSimData().getUnemployed_sims())
        {
            int xpos = pos*100+20+pos*5;

            if (xpos > width - 60)
            {
                pos = 0;
                xpos = pos*100+20;
                ypos += 25;

            }

            EntitySim sim =(EntitySim) server.getEntityFromUuid(id);
            sims.add(id);
            String name = sim.getCustomNameTag();
            buttonList.add(button = new SimButton(i,xpos,ypos,name,id));
            if (!status.equals("hiring"))
            {
                button.visible = false;
                button.enabled = false;
            }
           if(ypos > height-40)
            {
             button.enabled = false;
             button.visible = false;
             button.clicked = true;
            }
            i++;
            pos++;
        }

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id){
            case 0:
                status = "hiring";
                button.visible = false;
                button.enabled = false;
            case 1:
                mc.displayGuiScreen(null);

        }
        if (button instanceof SimButton)
        {
            ((SimButton) button).clicked = true;
            tileEntity.setHired(true);
            PacketHandler.INSTANCE.sendToServer(new HiringPacket(((SimButton) button).simid,2));
            mc.displayGuiScreen(null);
        }


        super.actionPerformed(button);


    }


    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private class SimButton extends GuiButtonExt {
        boolean clicked = false;
        UUID simid;
        private SimButton(int id, int x, int y, String string, UUID sim_id ){
            super(id, x, y, 100, 20, string);
            simid = sim_id;
        }
    }
}
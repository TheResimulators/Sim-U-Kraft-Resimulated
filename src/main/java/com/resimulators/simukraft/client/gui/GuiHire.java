package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.interfaces.ISim;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import com.resimulators.simukraft.network.HiringPacket;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.UpdateJobIdPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GuiHire extends GuiScreen {
    private String name;
    private Set<Integer> sim_id = new HashSet<>();
    private World world;
    private TileEntity tileEntity;
    private int mouseX;
    private int mouseY;
    private int buttonWidth = 140;
    private String status = "";
    private SimButton button;
    private GuiButton hire_button;
    private GuiButton cancel_button;
    private Set<UUID> sims = new HashSet<>();
    private String profession = "";

    public GuiHire(TileEntity tileEntity){
        this.tileEntity = tileEntity;
        this.profession = ((ISim)tileEntity).getProfession();
    }

    public void add_sim(int id) {
        sim_id.add(id);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (mc != null) {
            drawDefaultBackground();
            if (!status.equals("hiring")) {
                drawString(mc.fontRenderer, profession, (width / 2) - fontRenderer.getStringWidth(profession) / 2, height / 4 - 10, Color.WHITE.getRGB());
            }
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void updateScreen() {
        List<GuiButton> buttons = buttonList;
        for (GuiButton button : buttons) {
            if (button instanceof SimButton) {
                if (!status.equals("hiring")) {
                    button.visible = false;
                    button.enabled = false;
                } else {
                    button.visible = true;
                    button.enabled = true;
                    if (((SimButton) button).clicked) {
                        button.visible = false;
                        button.enabled = false;
                    } else {
                        button.visible = true;
                        button.enabled = true;
                    }
                }
            }
        }
    }

    @Override
    public void initGui() {
        buttonList.add(hire_button = new GuiButton(0, width / 2 - buttonWidth / 2, height - 60, buttonWidth, 20, "Hire"));
        buttonList.add(cancel_button = new GuiButton(1, width / 2 - buttonWidth / 2, height - 30, buttonWidth, 20, "Cancel"));
        if (status.equals("hiring")) {
            hire_button.enabled = false;
            hire_button.visible = false;
            cancel_button.enabled = false;
            cancel_button.visible = false;
        }
        int ypos = 30;
        int i = 0;
        int pos = 0;
        sims.clear();
        world = Minecraft.getMinecraft().world;
        int num = 0;
        List<String> names = ((ISim)tileEntity).getnames();
        for (int id: ((ISim)tileEntity).getSims())
        {
            int xpos = pos*100+20+pos*5;

            if (xpos > width - 60) {
                pos = 0;
                xpos = pos * 100 + 20;
                ypos += 25;

            }
            EntitySim sim = (EntitySim) world.getEntityByID(id);
            sim_id.add(id);
            name = names.get(num);
            if (name == null) {
                name = "Error";
            }
            buttonList.add(button = new SimButton(i, xpos, ypos, name, id));
            if (!status.equals("hiring")) {
                button.visible = false;
                button.enabled = false;
            }
            if (ypos > height - 40) {
                button.enabled = false;
                button.visible = false;
                button.clicked = true;
            }
            i++;
            pos++;
            num++;
        }

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            status = "hiring";
            button.visible = false;
            button.enabled = false;
        }
        if (button instanceof SimButton) {
            ((SimButton) button).clicked = true;
            ((ISim)tileEntity).setHired(true);
            PacketHandler.INSTANCE.sendToServer(new HiringPacket(((SimButton) button).simid,((ISim)tileEntity).getProfessionint(),tileEntity.getPos().getX(),tileEntity.getPos().getY(),tileEntity.getPos().getZ()));
            PacketHandler.INSTANCE.sendToServer(new UpdateJobIdPacket(((SimButton) button).simid, tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()));
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
        int simid;
        EntitySim sim;

        private SimButton(int id, int x, int y, String string, int sim_id) {
            super(id, x, y, 100, 20, string);
            simid = sim_id;
            sim = (EntitySim) world.getEntityByID(sim_id);
        }
    }
}
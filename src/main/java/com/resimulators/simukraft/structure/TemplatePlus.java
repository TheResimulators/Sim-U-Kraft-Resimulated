package com.resimulators.simukraft.structure;

import com.resimulators.simukraft.SimUKraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.template.Template;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabbe on 2019-02-12 - 10:18 AM.
 */
public class TemplatePlus extends Template {
    private String category = "";
    private String profession = "";
    private double price = 0;

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        if (compound.hasKey("category"))
            category = compound.getString("category");
        if (compound.hasKey("price"))
            price = compound.getDouble("price");
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public List<BlockInfo> getBlocks() {
        List<BlockInfo> blocks = new ArrayList<>();
        try {
            Field field = this.getClass().getSuperclass().getDeclaredField("blocks");
            field.setAccessible(true);
            blocks = (List<Template.BlockInfo>) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {
            SimUKraft.getLogger().warn(e);
        }
        return blocks;
    }


    public String getProfession(){
        return profession;
    }
}

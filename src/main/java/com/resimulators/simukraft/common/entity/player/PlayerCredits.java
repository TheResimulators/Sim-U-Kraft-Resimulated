package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import java.util.List;

public class PlayerCredits implements INBTSerializable<NBTTagCompound>{
    public static float credits = 10;
    List<String> total_sim =  SimToHire.totalsims;
    NBTTagList taglist = new NBTTagList();
    public NBTTagCompound nbt = new NBTTagCompound();
    public NBTTagCompound sims = new NBTTagCompound();
    @Override
    public NBTTagCompound serializeNBT() {


        for(int i = 0; 1 < total_sim.size(); i++){
            String total_sims = String.valueOf(total_sim.get(i));
            if (total_sims != null){

            sims.setString("sim " + i, total_sims);
            taglist.appendTag(sims);

        }
        }
        nbt.setTag("population",taglist);
        nbt.setFloat("credits",credits);

        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.credits = nbt.getInteger("population");
        NBTTagList tagList = nbt.getTagList("sims", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < taglist.tagCount(); i++)
        {
            NBTTagCompound sims = taglist.getCompoundTagAt(i);
            String total_sims = nbt.getString("sim " + i);
            total_sim.add(i,total_sims);
        }

    }
}
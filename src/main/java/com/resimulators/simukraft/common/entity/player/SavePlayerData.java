package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.update_sim_packet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;


import java.util.List;
import java.util.UUID;

public class SavePlayerData extends WorldSavedData {
    private MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    private List<UUID>unemployed_sims = SimToHire.unemployedsims;
    private List<UUID>total_sims = SimToHire.totalsims;
    private NBTTagList list_unemployed_sims = new NBTTagList();
    private NBTTagList list_total_sims = new NBTTagList();
    private static final String DATA_NAME ="PlayerData";

    public SavePlayerData() {
        super(DATA_NAME);
    }
    public SavePlayerData(String s) {
        super(s);
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        for(int i = 0;i<list_unemployed_sims.tagCount();i++){
            String uuid = list_unemployed_sims.getStringTagAt(i);
            UUID sim_id = UUID.fromString(uuid);
            SimToHire.unemployedsims.add(sim_id);
            PacketHandler.INSTANCE.sendToAll(new update_sim_packet(sim_id,"unemployed"));

        }
        for(int i = 0;i<list_total_sims.tagCount();i++) {
            String uuid = list_total_sims.getStringTagAt(i);
            UUID sim_id = UUID.fromString(uuid);
            SimToHire.totalsims.add(sim_id);
            PacketHandler.INSTANCE.sendToAll(new update_sim_packet(sim_id,"total"));
        }
        }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        System.out.println("writing to nbt");
        for (int i = 0; i < unemployed_sims.size(); i++) {
            UUID id = unemployed_sims.get(i);
            list_unemployed_sims.appendTag(new NBTTagString(id.toString()));
            markDirty();
            }
        for (int i = 0; i < total_sims.size(); i++) {
            UUID id = total_sims.get(i);
            list_total_sims.appendTag( new NBTTagString(id.toString()));
            markDirty();
        }
        markDirty();
        return compound;
    }

}
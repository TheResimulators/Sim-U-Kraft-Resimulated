package com.resimulators.simukraft.common.interfaces;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ISim {
    void setHired(boolean hired);

    void setId(UUID id);

    UUID getId();

    boolean getHired();

    String getProfession();

    int getProfessionID();

    void addSim(int sim);

    Set<Integer> getSims();

    void removeSim(int sim);

    void removeSimName(String name);

    void addSimName(String name);

    List<String> getnames();

    void openGui(World world, BlockPos pos, EntityPlayer player);
}
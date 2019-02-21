package com.resimulators.simukraft.common.interfaces;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ISimIndustrial extends ISim {
    void setHired(boolean hired);

    void addId(UUID id);

    UUID getId();

    boolean getHired();

    String getProfession();

    int getProfessionID();

    void addSim(int sim);

    Set<Integer> getSims();

    void removeSim(int sim);

    void addSimName(String name);

    List<String> getnames();

    void setSimname(int id);
}


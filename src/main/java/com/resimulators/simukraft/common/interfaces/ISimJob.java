package com.resimulators.simukraft.common.interfaces;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ISimJob extends ISim {
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
}



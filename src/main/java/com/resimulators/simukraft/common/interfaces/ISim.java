package com.resimulators.simukraft.common.interfaces;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ISim {
    void setHired(boolean hired);

    void setId(UUID id);

    boolean getHired();

    String getProfession();

    int getProfessionint();

    void addSim(int sim);

    Set<Integer> getSims();

    void removeSim(int sim);

    void removeSimName(String name);

    void addSimName(String name);

    List<String> getnames();
}
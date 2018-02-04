package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.common.tileentity.base.TileFarmBase;
import net.minecraft.util.ITickable;

public class TileFarm extends TileFarmBase implements ITickable {
    public boolean building;

    public void update() {
    if (!building) {
        CreateFarm();
        building = true;
        }

    }
}

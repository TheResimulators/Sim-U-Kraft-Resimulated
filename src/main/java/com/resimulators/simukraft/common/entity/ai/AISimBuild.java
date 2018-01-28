package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

/**
 * Created by fabbe on 20/01/2018 - 11:04 AM.
 */
public class AISimBuild extends EntityAIBase {
    private EntitySim entitySim;
    private Structure structure;
    private boolean building;
    private int progress;
    private BlockPos startPos;

    public AISimBuild(EntitySim entitySim) {
        this.entitySim = entitySim;
    }

    @Override
    public boolean shouldExecute() {
        if (entitySim != null) {
            if (entitySim.getProfession() == 1) {
                if (entitySim.getStructure() != null) {
                    if (entitySim.isAllowedToBuild()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

    @Override
    public void startExecuting() {
        this.structure = entitySim.getStructure();
        this.startPos = entitySim.getPosition();
        this.building = true;
        SimUKraft.getLogger().info("Executing AISimBuild");
    }

    @Override
    public void resetTask() {

    }

    @Override
    public void updateTask() {
        //Code snippet from Astavie
        if (building) {
            if (structure == null) {
                building = false;
                progress = 0;
            } else {
                int x = (progress % structure.getWidth());
                int z = (progress / structure.getWidth()) % structure.getDepth();
                int y = (progress / structure.getWidth()) / structure.getDepth();
                if (y >= structure.getHeight()) {
                    building = false;
                    progress = 0;
                    entitySim.setStructure(null);
                    entitySim.setProfession(0);
                    entitySim.setAllowedToBuild(false);
                } else {
                    // TODO: make it require items from inventory
                    entitySim.world.setBlockState(startPos.add(x + 1, y, z), structure.getBlock(x, y, z));
                    progress++;
                }
            }
        }
    }
}
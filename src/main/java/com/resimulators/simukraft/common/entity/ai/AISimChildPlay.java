package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * Created by fabbe on 19/01/2018 - 8:36 PM.
 */
public class AISimChildPlay extends EntityAIBase {
    private final EntitySim sim;
    private EntityLivingBase targetVillager;
    private final double speed;
    private int playTime;

    public AISimChildPlay(EntitySim simIn, double speedIn) {
        this.sim = simIn;
        this.speed = speedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.sim.getGrowingAge() >= 0) {
            return false;
        } else if (this.sim.getRNG().nextInt(400) != 0) {
            return false;
        } else {
            List<EntitySim> list = this.sim.world.getEntitiesWithinAABB(EntitySim.class, this.sim.getEntityBoundingBox().grow(6.0D, 3.0D, 6.0D));
            double d0 = Double.MAX_VALUE;

            for (EntitySim entitysim : list) {
                if (entitysim != this.sim && !entitysim.isPlaying() && entitysim.getGrowingAge() < 0) {
                    double d1 = entitysim.getDistanceSq(this.sim);

                    if (d1 <= d0) {
                        d0 = d1;
                        this.targetVillager = entitysim;
                    }
                }
            }

            if (this.targetVillager == null) {
                Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.sim, 16, 3);

                if (vec3d == null) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        return this.playTime > 0;
    }

    public void startExecuting() {
        if (this.targetVillager != null) {
            this.sim.setPlaying(true);
        }

        this.playTime = 1000;
    }

    public void resetTask() {
        this.sim.setPlaying(false);
        this.targetVillager = null;
    }

    public void updateTask() {
        --this.playTime;

        if (this.targetVillager != null) {
            if (this.sim.getDistanceSq(this.targetVillager) > 4.0D) {
                this.sim.getNavigator().tryMoveToEntityLiving(this.targetVillager, this.speed);
            }
        } else if (this.sim.getNavigator().noPath()) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.sim, 16, 3);

            if (vec3d == null) {
                return;
            }

            this.sim.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, this.speed);
        }
    }
}

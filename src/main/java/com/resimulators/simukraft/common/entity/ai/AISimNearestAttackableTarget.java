package com.resimulators.simukraft.common.entity.ai;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.enums.cattleFarmMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import com.resimulators.simukraft.common.entity.ai.AISimNearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AISimNearestAttackableTarget <T extends EntityLivingBase> extends EntityAITarget
{
    protected final Class<T> targetClass;
    private final int targetChance;
    private EntitySim sim;
    /** Instance of AISimNearestAttackableTargetSorter. */
    protected final AISimNearestAttackableTarget.Sorter sorter;
    protected final Predicate<? super T > targetEntitySelector;
    protected T targetEntity;

    public AISimNearestAttackableTarget(EntitySim creature, Class<T> classTarget, boolean checkSight)
    {
        this(creature, classTarget, checkSight, false);
    }

    public AISimNearestAttackableTarget(EntitySim creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby)
    {
        this(creature, classTarget, 1, checkSight, onlyNearby,null);
    }

    public AISimNearestAttackableTarget(EntitySim creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate <? super T > targetSelector)
    {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.sim = creature;
        this.targetChance = chance;
        this.sorter = new AISimNearestAttackableTarget.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = (Predicate<T>) p_apply_1_ -> {
            if (p_apply_1_ == null)
            {
                return false;
            }
            else if (targetSelector != null && !targetSelector.apply(p_apply_1_))
            {
                return false;
            }
            else
            {
                return EntitySelectors.NOT_SPECTATING.apply(p_apply_1_) && AISimNearestAttackableTarget.this.isSuitableTarget(p_apply_1_, false);
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!sim.getWorking()){
            return false;}
        if (this.targetClass != EntityPlayer.class && this.targetClass != EntityPlayerMP.class)
        {
            List<T> list = this.taskOwner.world.<T>getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
            if (list.isEmpty())
            {
                return false;
            }
            else {
                Collections.sort(list, this.sorter);
                this.targetEntity = list.get(0);
            }
                return true;
            }
        return false;
    }



    protected AxisAlignedBB getTargetableArea(double targetDistance)
    {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        if (sim.getCowmode() == cattleFarmMode.FarmMode.KILL) {
            this.taskOwner.setAttackTarget(this.targetEntity);
        } else{
            this.sim.setTargetCow((EntityCow) this.targetEntity);
        }
        super.startExecuting();
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.entity.getDistanceSq(p_compare_1_);
            double d1 = this.entity.getDistanceSq(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }

    @Override
    protected double getTargetDistance(){
        return 4.0d;
    }
}

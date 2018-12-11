package com.resimulators.simukraft.common.entity.ai;

import com.google.common.base.Predicate;
import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.enums.FarmModes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.IShearable;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
            List<T> list = this.taskOwner.world.getEntitiesWithinAABB(this.targetClass, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
            if (list.isEmpty())
            {
                return false;
            }
            else {
                list.sort(this.sorter);
                if (sim.getCowmode() == FarmModes.CowMode.MILK && sim.getLabeledProfession().equals("Cattle Farmer") && targetEntity instanceof EntityCow && sim.getDistanceSq(sim.getJobBlockPos()) <= 4){
                    for (T aList : list) {
                        if (aList.hasCapability(ModCapabilities.CAP, null)) {
                            if (Objects.requireNonNull(aList.getCapability(ModCapabilities.CAP, null)).ismilkable()) {
                                this.targetEntity = aList;
                                return true;
                            }
                        }
                    }
                }else if (sim.getSheepMode() == FarmModes.SheepMode.SHEAR && sim.getLabeledProfession().equals("Sheep Farmer") && targetEntity instanceof EntitySheep && sim.getDistanceSq(sim.getJobBlockPos()) <= 4){

                    for (T alist : list){
                        EntitySheep sheep = (EntitySheep) alist;
                        if (!sheep.getSheared()){
                            sim.setSheeptarget(sheep);
                            break;
                        }
                    }
                    if (sim.getSheeptarget() == null){
                        sim.setEndWork(true);
                    }
                }

                else{this.targetEntity = list.get(0);}
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
        if (sim.getLabeledProfession().equals("Cattle Farmer")){
        if (sim.getCowmode() == FarmModes.CowMode.KILL) {
            this.taskOwner.setAttackTarget(this.targetEntity);
        } else{
            this.sim.setTargetCow((EntityCow) this.targetEntity);
        }
        super.startExecuting();
    }
    else{
            this.taskOwner.setAttackTarget(this.targetEntity);}
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

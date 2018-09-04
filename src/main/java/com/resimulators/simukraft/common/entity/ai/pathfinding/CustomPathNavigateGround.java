package com.resimulators.simukraft.common.entity.ai.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.world.World;

public class CustomPathNavigateGround extends PathNavigateGround {
    public CustomPathNavigateGround(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }
    @Override
    protected PathFinder getPathFinder()
    {
        this.nodeProcessor = new CustomWalkNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(nodeProcessor);

    }
}

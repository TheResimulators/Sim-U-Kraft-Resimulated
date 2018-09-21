package com.resimulators.simukraft.common.entity.ai.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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

    @Override
    public Path getPath(){
        System.out.println("current path" + currentPath);
        if (currentPath == null){
            System.out.println("this is null");
        }
        return this.currentPath;
    }


    @Override
    public boolean setPath(@Nullable Path pathentityIn, double speedIn){
        System.out.println("this is called. path is " + pathentityIn + "Entity" + entity);
        if (pathentityIn == null){
            System.out.println("who is setting it null");
        }
        return super.setPath(pathentityIn,speedIn);
    }


    @Override
    public boolean canNavigate(){
        return true;
    }
}

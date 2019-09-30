package com.resimulators.simukraft.util;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3i;

public class RotationHelper {

    public static BlockPos rotateAroundPoint(BlockPos pos,BlockPos startPoint, Rotation rotation){
        BlockPos difference = pos.add(-startPoint.getX(),-startPoint.getY(),-startPoint.getZ());
        pos = pos.rotate(rotation);
        pos = pos.add(difference.getX(),difference.getY(),difference.getZ());
        return pos;
    }
}

package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * Created by fabbe on 20/01/2018 - 11:04 AM.
 */
public class AISimBuild extends EntityAIBase {
    private EntitySim entitySim;
    private Structure structure;
    private int progress;
    private BlockPos startPos;
    private BlockPos currentPos;
    private int cooldown = 0;
    private int tries = 0;
    private Random rand = new Random();

    public AISimBuild(EntitySim entitySim) {
        this.entitySim = entitySim;
    }

    @Override
    public boolean shouldExecute() {
        if (this.entitySim != null) {
            if (this.entitySim.getProfession() == 1) {
                if (this.entitySim.getStructure() != null) {
                    return this.entitySim.isAllowedToBuild();
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
        this.structure = this.entitySim.getStructure();
        this.startPos = this.entitySim.getStartPos();
        this.currentPos = this.entitySim.getStartPos();
        SimUKraft.getLogger().info("Executing AISimBuild");
    }

    @Override
    public void resetTask() {

    }

    @Override
    public void updateTask() {
        //Code by Astavie, modified by fabbe50
        if (structure == null) {
            progress = 0;
        } else {
            int x = (progress % structure.getWidth());
            int z = (progress / structure.getWidth()) % structure.getDepth();
            int y = (progress / structure.getWidth()) / structure.getDepth();
            if (y >= structure.getHeight()) {
                progress = 0;
                this.entitySim.attemptTeleport(entitySim.getStartPos().getX(), entitySim.getStartPos().getY(), entitySim.getStartPos().getZ());
                this.entitySim.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                this.entitySim.setStructure(null);
                this.entitySim.setAllowedToBuild(false);
                this.entitySim.setStartPos(null);
            } else {
                // TODO: make it require items from inventory
                if (structure.getBlock(x, y, z).getBlock() == this.entitySim.world.getBlockState(currentPos.add(x + 1, y, z)).getBlock()) {
                    currentPos = startPos.add(x + 1, y, z);
                    progress++;
                } else if (currentPos.getDistance((int) this.entitySim.posX, (int) this.entitySim.posY, (int) this.entitySim.posZ) < 4) {
                    if (cooldown == 0) {
                        currentPos = startPos.add(x + 1, y, z);
                        if (structure.getBlock(x, y, z).getBlock() instanceof BlockDoor && structure.getBlock(x, y, z) == structure.getBlock(x, y, z).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER)) {
                            this.entityPlaceBlock(this.structure.getBlock(x, y, z), currentPos);
                            this.entitySim.world.setBlockState(currentPos.offset(EnumFacing.UP), structure.getBlock(x, y + 1, z));
                        } else
                            this.entityPlaceBlock(this.structure.getBlock(x, y, z), currentPos);
                        progress++;
                        cooldown = rand.nextInt(10) + 5;
                    } else {
                        cooldown--;
                    }
                } else {
                    this.moveEntityCloser(this.currentPos);
                }
            }
        }
    }

    private void moveEntityCloser(BlockPos thePos) {
        if (this.entitySim.getDistanceSq(thePos) > 256.0D) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entitySim, 14, 7, new Vec3d((double) thePos.getX() + 0.5D, (double) thePos.getY(), (double) thePos.getZ() + 0.5D));
            if (vec3d != null) {
                boolean success = this.entitySim.getNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.7D);
                if (!success) {
                    if (tries == 0) {
                        this.entitySim.attemptTeleport(vec3d.x + (rand.nextInt(20) - 10) + 0.5, vec3d.y + (rand.nextInt(20) - 10), vec3d.z + (rand.nextInt(20) - 10) + 0.5);
                        tries = 5;
                    } else {
                        tries--;
                    }
                }
            }
        } else {
            boolean success = this.entitySim.getNavigator().tryMoveToXYZ(thePos.getX(), thePos.getY(), thePos.getZ(), 0.7D);
            if (!success) {
                if (tries == 0) {
                    this.entitySim.attemptTeleport(thePos.getX() + (rand.nextInt(6) - 3) + 0.5, thePos.getY() + (rand.nextInt(6) - 3), thePos.getZ() + (rand.nextInt(6) - 3) + 0.5);
                    tries = 20;
                } else {
                    tries--;
                }
            }
        }
    }

    private void entityPlaceBlock(IBlockState blockState, BlockPos pos) {
        this.entitySim.getLookHelper().setLookPosition(currentPos.getX(), currentPos.getY(), currentPos.getZ(), 360, 360);
        this.entitySim.setHeldItem(EnumHand.MAIN_HAND, blockState.getBlock().getItem(null, null, blockState));
        this.entitySim.world.setBlockState(pos, blockState);
        this.entitySim.swingArm(EnumHand.MAIN_HAND);
    }
}
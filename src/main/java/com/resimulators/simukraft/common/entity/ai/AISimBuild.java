package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

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
    Random rand = new Random();

    public AISimBuild(EntitySim entitySim) {
        this.entitySim = entitySim;
    }

    @Override
    public boolean shouldExecute() {
        if (this.entitySim != null) {
            if (this.entitySim.getProfession() == 1) {
                if (this.entitySim.getStructure() != null) {
                    if (this.entitySim.isAllowedToBuild()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        boolean flag = this.shouldExecute();
        return flag;
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
                boolean success = this.entitySim.getNavigator().tryMoveToXYZ(entitySim.getStartPos().getX(), entitySim.getStartPos().getY(), entitySim.getStartPos().getZ(), 0.7);
                if (!success) {
                    for (int i = 0; i < 20; i++) {
                        boolean tpSuccess = this.entitySim.attemptTeleport(entitySim.getStartPos().getX() + (rand.nextInt(2) - 1) + 0.5, entitySim.getStartPos().getY() + (rand.nextInt(2)), entitySim.getStartPos().getZ() + (rand.nextInt(2) - 1) + 0.5);
                        if (tpSuccess)
                            break;
                    }
                }
                this.entitySim.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                this.entitySim.setStructure(null);
                this.entitySim.setAllowedToBuild(false);
                this.entitySim.setStartPos(null);
            } else {
                // TODO: make it require items from inventory
                if (structure.getBlock(x, y, z).getBlock() == Blocks.AIR && this.entitySim.world.getBlockState(currentPos.add(x + 1, y, z)).getBlock() == Blocks.AIR) {
                    currentPos = startPos.add(x + 1, y, z);
                    progress++;
                } else if (currentPos.getDistance((int) this.entitySim.posX, (int) this.entitySim.posY, (int) this.entitySim.posZ) < 4) {
                    if (cooldown == 0) {
                        currentPos = startPos.add(x + 1, y, z);
                        this.entitySim.setHeldItem(EnumHand.MAIN_HAND, structure.getItemStack(x, y, z));
                        this.entitySim.getLookHelper().setLookPosition(currentPos.getX(), currentPos.getY(), currentPos.getZ(), 360, 360);
                        this.entitySim.world.setBlockState(currentPos, structure.getBlock(x, y, z));
                        this.entitySim.swingArm(EnumHand.MAIN_HAND);
                        progress++;
                        cooldown = rand.nextInt(10) + 5;
                    } else {
                        cooldown--;
                    }
                } else {
                    if (this.entitySim.getDistanceSq(currentPos) > 256.0D) {
                        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entitySim, 14, 7, new Vec3d((double) this.currentPos.getX() + 0.5D, (double) this.currentPos.getY(), (double) this.currentPos.getZ() + 0.5D));
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
                        boolean success = this.entitySim.getNavigator().tryMoveToXYZ(this.currentPos.getX(), this.currentPos.getY(), this.currentPos.getZ(), 0.7D);
                        if (!success) {
                            if (tries == 0) {
                                this.entitySim.attemptTeleport(this.currentPos.getX() + (rand.nextInt(2) - 1) + 0.5, this.currentPos.getY() + (rand.nextInt(2) - 1), this.currentPos.getZ() + (rand.nextInt(2) - 1) + 0.5);
                                tries = 20;
                            } else {
                                tries--;
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockPos getPositionToMoveTo(BlockPos target) {
        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entitySim, 16, 7, new Vec3d((double) target.getX(), (double) target.getY(), (double) target.getZ()));
        if (vec3d == null) {
            return null;
        } else {
            return new BlockPos(vec3d.x, vec3d.y, vec3d.z);
        }
    }
}
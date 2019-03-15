package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.BlockRotationProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.util.*;

/**
 * Created by fabbe on 20/01/2018 - 11:04 AM.
 * !!! PLEASE NOTE !!!
 * THIS IS STILL IN EARLY DEVELOPMENT AND THAT THE CODE ISN'T IN IT'S FINAL STATE.
 * PLEASE DO:
 * * SUGGEST CHANGES ON HOW THIS CAN BE DONE BETTER.
 * * COMMIT ADJUSTMENTS THAT COULD IMPROVE THIS.
 * PLEASE DON'T:
 * * SAY "THIS IS CRAP, KYS".
 * * COMPLAIN ABOUT BUGS, WITHOUT GIVING AN ACTUAL REPORT ABOUT WHAT'S NOT WORKING.
 */
public class AISimBuild extends EntityAIBase {
    private World world;
    private EntitySim entitySim;
    private TemplatePlus structure;
    private int progress;
    private BlockPos startPos;
    private BlockPos currentPos;
    private EnumFacing facing;
    private PlacementSettings settings;
    private boolean rotated = false;
    private int cooldown = 0;
    private int tries = 0;
    private Random rand = new Random();
    private Map<BlockPos, Template.BlockInfo> blockLocations = new HashMap<>();

    //Special types
    private int specialIndex;
    private List<IBlockState> specialStates = new ArrayList<>();
    private List<BlockPos> specialPositions = new ArrayList<>();

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
        this.world = this.entitySim.world;
        this.structure = this.entitySim.getStructure();
        this.startPos = this.entitySim.getStartPos();
        this.currentPos = this.entitySim.getStartPos();
        this.facing = this.entitySim.getFacing();
        this.specialPositions.clear();
        this.specialStates.clear();
        this.specialIndex = 0;
        settings = (new PlacementSettings().setIgnoreEntities(false).setRotation(Utilities.convertFromFacing(facing)).setMirror(Mirror.NONE));
        List<Template.BlockInfo> blocks = structure.getBlocks();
        for (Template.BlockInfo info : blocks) {
            blockLocations.put(startPos.add(info.pos), info);
        }
    }

    @Override
    public void resetTask() {

    }

    Template.BlockInfo info;
    BlockPos lastPos;
    @Override
    public void updateTask() {
        //TODO: Needs a lot of improvement. Works with the new building system for now, but still needs inventory support and better looking building.
        //TODO: For instance, line of sight to the place where the block is supposed to go as well as being within reach.
        if (entitySim.isAllowedToBuild() && cooldown <= 0) {
            cooldown = 10;
            if (!rotated) {
                if (Utilities.convertFromFacing(facing).equals(Rotation.CLOCKWISE_90)) {
                    startPos = startPos.add(structure.getSize().getX() - 1, 0, 0);
                } else if (Utilities.convertFromFacing(facing).equals(Rotation.CLOCKWISE_180)) {
                    startPos = startPos.add(structure.getSize().getX() - 1, 0, structure.getSize().getZ() - 1);
                } else if (Utilities.convertFromFacing(facing).equals(Rotation.COUNTERCLOCKWISE_90)) {
                    startPos = startPos.add(0, 0, structure.getSize().getZ() - 1);
                }
                currentPos = startPos;
                rotated = true;
            }
            if ((info == null) || lastPos != currentPos) {
                lastPos = currentPos;
                info = blockLocations.get(currentPos);
            }
            if (info != null) {
                if (entitySim != null && entitySim.getPosition().distanceSq(currentPos.getX(), currentPos.getY(), currentPos.getZ()) <= 25) {
                    IBlockState state = info.blockState.withRotation(settings.getRotation());
                    if ((world.getBlockState(startPos.add(info.pos)).getBlock().equals(info.blockState.getBlock()))) {
                        if (info.pos.getX() < structure.getSize().getX())
                            currentPos = currentPos.add(1, 0, 0);
                        else {
                            currentPos = currentPos.add(-structure.getSize().getX(), 0, 0);
                            if (info.pos.getZ() < structure.getSize().getZ()) {
                                currentPos = currentPos.add(0, 0, 1);
                            } else {
                                currentPos = currentPos.add(0, 1, -structure.getSize().getZ());
                            }
                        }
                    } else {
                        if (entityPlaceBlock(state, startPos.add(transformedBlockPos(info.pos, settings.getMirror(), settings.getRotation())))) {
                            if (info.tileentityData != null) {
                                TileEntity tileentity = world.getTileEntity(info.pos);
                                if (tileentity != null) {
                                    if (tileentity instanceof IInventory)
                                        ((IInventory) tileentity).clear();
                                    info.tileentityData.setInteger("x", info.pos.getX());
                                    info.tileentityData.setInteger("y", info.pos.getY());
                                    info.tileentityData.setInteger("z", info.pos.getZ());
                                    tileentity.readFromNBT(info.tileentityData);
                                    tileentity.mirror(settings.getMirror());
                                    tileentity.rotate(settings.getRotation());
                                    tileentity.markDirty();
                                }
                            }

                            if (info.pos.getX() < structure.getSize().getX())
                                currentPos = currentPos.add(1, 0, 0);
                            else {
                                currentPos = currentPos.add(-structure.getSize().getX(), 0, 0);
                                if (info.pos.getZ() < structure.getSize().getZ()) {
                                    currentPos = currentPos.add(0, 0, 1);
                                } else {
                                    currentPos = currentPos.add(0, 1, -structure.getSize().getZ());
                                }
                            }

                            SimUKraft.getLogger().info("Placing block " + info.blockState.getBlock().getLocalizedName() + " at " + currentPos.add(info.pos));
                            if (info.pos.getY() >= structure.getSize().getY()) {
                                SimUKraft.getLogger().info("Done building.");
                                entitySim.setAllowedToBuild(false);
                                entitySim.setStructure(null);
                                entitySim.setStartPos(null);
                            }

                            info = null;
                        }
                    }
                } else {
                    SimUKraft.getLogger().info("Polling position: " + startPos.add(info.pos) + ", Current position: " + currentPos + ", Distance from entity: " + entitySim.getPosition().distanceSq(currentPos.getX(), currentPos.getY(), currentPos.getZ()) + ", Block Position: " + info.pos);
                    moveEntityCloser(currentPos);
                }
            }
        } else
            cooldown--;
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
            boolean success = this.entitySim.getNavigator().tryMoveToXYZ(thePos.getX() + (rand.nextInt(6) - 3), thePos.getY() + (rand.nextInt(6) - 3), thePos.getZ() + (rand.nextInt(6) - 3), 0.7D);
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

    private boolean entityPlaceBlock(IBlockState blockState, BlockPos pos) {
        this.entitySim.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), 360, 360);
        this.entitySim.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(blockState.getBlock())));
        this.entitySim.world.setBlockState(pos, blockState, 2);
        this.entitySim.world.playSound(null, pos, blockState.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0f, (rand.nextFloat() - 0.5f) / 5);
        this.entitySim.swingArm(EnumHand.MAIN_HAND);
        return true;
    }

    private static BlockPos transformedBlockPos(BlockPos pos, Mirror mirrorIn, Rotation rotationIn)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;

        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                k = -k;
                break;
            case FRONT_BACK:
                i = -i;
                break;
            default:
                flag = false;
        }

        switch (rotationIn)
        {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return flag ? new BlockPos(i, j, k) : pos;
        }
    }
}
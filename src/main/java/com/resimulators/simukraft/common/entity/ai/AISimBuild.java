package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.Utilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.BlockRotationProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import java.lang.reflect.Field;
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
    private EntitySim entitySim;
    private TemplatePlus structure;
    private int progress;
    private BlockPos startPos;
    private BlockPos currentPos;
    private EnumFacing facing;
    private PlacementSettings settings;
    private int cooldown = 0;
    private int tries = 0;
    private Random rand = new Random();

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
        this.structure = this.entitySim.getStructure();
        this.startPos = this.entitySim.getStartPos();
        this.currentPos = this.entitySim.getStartPos();
        this.facing = this.entitySim.getFacing();
        this.specialPositions.clear();
        this.specialStates.clear();
        this.specialIndex = 0;
        settings = (new PlacementSettings().setIgnoreEntities(false).setRotation(Utilities.convertFromFacing(facing)).setMirror(Mirror.NONE));
        SimUKraft.getLogger().info("Executing AISimBuild");
    }

    @Override
    public void resetTask() {

    }

    @Override
    public void updateTask() {
        //TODO: Needs a lot of improvement. Works with the new building system for now, but still needs inventory support and better looking building.
        //TODO: For instance, line of sight to the place where the block is supposed to go as well as being within reach.
        if (entitySim.isAllowedToBuild()) {
            if (Utilities.convertFromFacing(facing).equals(Rotation.CLOCKWISE_90)) {
                startPos = startPos.add(structure.getSize().getX() - 1, 0, 0);
            } else if (Utilities.convertFromFacing(facing).equals(Rotation.CLOCKWISE_180)) {
                startPos = startPos.add(structure.getSize().getX() - 1, 0, structure.getSize().getZ() - 1);
            } else if (Utilities.convertFromFacing(facing).equals(Rotation.COUNTERCLOCKWISE_90)) {
                startPos = startPos.add(0, 0, structure.getSize().getZ() - 1);
            }
            try {
                World world = entitySim.world;
                List<Template.BlockInfo> blocks = structure.getBlocks();
                for (Template.BlockInfo info : blocks) {
                    BlockPos blockpos = info.pos;
                    BlockRotationProcessor processor = new BlockRotationProcessor(blockpos, settings);
                    Template.BlockInfo info1 = processor.processBlock(world, blockpos, info);
                    if (info1 != null) {
                        IBlockState state = info1.blockState.withRotation(settings.getRotation());
                        if (entityPlaceBlock(state, startPos.add(transformedBlockPos(blockpos, settings.getMirror(), settings.getRotation()))) && info1.tileentityData != null) {
                            TileEntity tileentity = world.getTileEntity(blockpos);
                            if (tileentity != null) {
                                if (tileentity instanceof IInventory)
                                    ((IInventory) tileentity).clear();
                                info1.tileentityData.setInteger("x", blockpos.getX());
                                info1.tileentityData.setInteger("y", blockpos.getY());
                                info1.tileentityData.setInteger("z", blockpos.getZ());
                                tileentity.readFromNBT(info1.tileentityData);
                                tileentity.mirror(settings.getMirror());
                                tileentity.rotate(settings.getRotation());
                                tileentity.markDirty();
                            }
                        }
                    }
                }
                entitySim.setAllowedToBuild(false);
                entitySim.setStructure(null);
                entitySim.setStartPos(null);
            } catch (ClassCastException e) {
                //Do Nothing
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

    private boolean entityPlaceBlock(IBlockState blockState, BlockPos pos) {
        this.entitySim.getLookHelper().setLookPosition(currentPos.getX(), currentPos.getY(), currentPos.getZ(), 360, 360);
        this.entitySim.setHeldItem(EnumHand.MAIN_HAND, blockState.getBlock().getItem(null, null, blockState));
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
package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.tileentity.TileMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AISimQuarryMine extends EntityAIBase {
    private World world;
    private EntitySim sim;
    private int miningDelay = 2;
    private BlockPos targetPos;
    private EnumFacing forward = null;
    private boolean pathfound = false;
    private int stairPosX = 0;
    private int stairPosY = 0;
    private int stairPosZ = 0;
    // direction, 0 = right, 1 = forward, 2 = left, 3 = backwards
    private int direction = 0;
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int width = 0;
    private int depth = 0;
    private TileMiner miner;

    public AISimQuarryMine(EntitySim sim, World world) {
        this.sim = sim;
        this.world = world;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return sim.getProfession() == 7 && sim.isWorking();
    }

    @Override
    public void startExecuting() {
        x = 0;
        y = 0;
        z = 0;
        direction = 0;
        stairPosX = 0;
        stairPosY = 0;
        stairPosZ = 0;
        miner = (TileMiner) world.getTileEntity(sim.getJobBlockPos());
        if (miner == null)
            return;

        targetPos = sim.getJobBlockPos();
        width = miner.getWidth();
        depth = miner.getDepth();
    }


    @Override
    public void updateTask() {
        /** if (targetPos != null && sim.getDistanceSq(targetPos) > 3 && !pathfound && sim.isWorking()) {
         Vec3d position = RandomPositionGenerator.findRandomTargetBlockTowards(sim, 3, 0, new Vec3d(targetPos.getX() + 0.5f, targetPos.getY(), targetPos.getZ() + 0.5f));
         System.out.println("position " + position);
         if (position != null) {
         pathfound = sim.getNavigator().tryMoveToXYZ(position.x, position.y, position.z, 0.7d);
         }
         }**/

        if (miningDelay <= 0) {
            miningDelay = 1;
            if (targetPos != null) {
                targetPos = sim.getJobBlockPos().add(0,-1,0);
                targetPos = targetPos.offset(miner.getFacing());
                targetPos = targetPos.offset(miner.getFacing().rotateY());
                if (x > width){
                    x = 0;
                    z++;
                }
                if (z > depth){
                    x = 0;
                    z = 0;
                    y++;
                }
                targetPos = targetPos.offset(miner.getFacing().rotateY(), x);
                targetPos = targetPos.offset(EnumFacing.DOWN, y);
                targetPos = targetPos.offset(miner.getFacing(), z);
                if (targetPos.getY() < 1 || world.getBlockState(targetPos).getBlock().equals(Blocks.BEDROCK)) {
                    targetPos = null;
                    sim.setNotWorking();
                    Objects.requireNonNull(SaveSimData.get(world)).getFaction(sim.getFactionId()).addUnemployedSim(sim.getUniqueID());
                    ((TileMiner) Objects.requireNonNull(world.getTileEntity(sim.getJobBlockPos()))).setHired(false);
                    ((TileMiner) Objects.requireNonNull(world.getTileEntity(sim.getJobBlockPos()))).setId(null);
                    List<UUID> players = Objects.requireNonNull(SaveSimData.get(world)).getFaction(sim.getFactionId()).getPlayers();
                    for (UUID id : players) {
                        EntityPlayer player = world.getPlayerEntityByUUID(id);
                        if (player != null) {
                            player.sendMessage(new TextComponentString("Miner " + sim.getName() + " has finished mining at position " + sim.getJobBlockPos() + "\n and has been fired"));
                        }
                    }
                    sim.setJobBlockPos(null);
                } else {
                    world.setBlockState(targetPos, Blocks.AIR.getDefaultState());
                    if (y == stairPosY) {
                        //to the right of the miner block
                        if (z == 0 && direction == 0 && x == stairPosX) {
                            if (x == width) {
                                direction = 1;
                                stairPosZ += 1;
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing().getOpposite());
                            } else {
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing().rotateYCCW());
                                stairPosX += 1;
                            }
                            stairPosY += 1;
                        }

                        //direction same facing as the miner blocks
                        if (x == width && direction == 1 && z == stairPosZ) {
                            if (z == depth) {
                                direction = 2;
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing().rotateY());
                                if (stairPosX > 0)
                                    stairPosX -= 1;
                            } else {
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing().getOpposite());
                                stairPosZ += 1;
                            }
                            stairPosY += 1;
                        }

                        //from right to left
                        if (z == depth && direction == 2 && x == stairPosX) {
                            if (x == 0) {
                                direction = 3;
                                stairPosZ -= 1;
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing());
                            } else {
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing().rotateY());
                                stairPosX -= 1;
                            }
                            stairPosY += 1;
                        }

                        //coming back towards the miner block
                        if (x == 0 && direction == 3 && z == stairPosZ) {
                            if (z == 0) {
                                direction = 0;
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing().rotateYCCW());
                                stairPosX += 1;
                            } else {
                                placeBlock(targetPos, Blocks.OAK_STAIRS, miner.getFacing());
                                stairPosZ -= 1;
                            }
                            stairPosY += 1;
                        }
                    }
                    x++;
                }
            }
        } else miningDelay--;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return shouldExecute();
    }

    private void placeBlock(BlockPos pos, Block block, EnumFacing facing) {
        if (block instanceof BlockStairs && pos != null) {
            EnumFacing blockfacing = block.getDefaultState().getValue(BlockStairs.FACING);
            int timesToRotate = 0;
            while (blockfacing != facing) {
                blockfacing = blockfacing.rotateY();
                timesToRotate++;
            }
            switch (timesToRotate) {
                case 0:
                default:
                    world.setBlockState(pos, block.getDefaultState());
                    break;
                case 1:
                    world.setBlockState(pos, block.getDefaultState().withRotation(Rotation.CLOCKWISE_90));
                    break;
                case 2:
                    world.setBlockState(pos, block.getDefaultState().withRotation(Rotation.CLOCKWISE_180));
                    break;
                case 3:
                    world.setBlockState(pos, block.getDefaultState().withRotation(Rotation.COUNTERCLOCKWISE_90));
                    break;
            }
        }

        sim.swingArm(EnumHand.MAIN_HAND);
    }


    private boolean checkinvforitem(@Nullable Item item, boolean adding) {
        IItemHandler inv = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);

        for (int i = 0; i < inv.getSlots(); i++) {
            if (adding) {
                if (inv.getStackInSlot(i).getItem().equals(item)) {
                    if (inv.getStackInSlot(i).getMaxStackSize() > inv.getStackInSlot(i).getCount()) {
                        return true;
                    }
                }
            } else {
                if (inv.getStackInSlot(i).equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addItemtoInv(Item item) {
        IItemHandler inv = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.SOUTH);

        for (int i = 0; i < inv.getSlots(); i++) {
            if (inv.getStackInSlot(i).getItem().equals(item) && inv.getStackInSlot(i).getCount() < inv.getStackInSlot(i).getMaxStackSize()) {
                ItemStack stack = inv.getStackInSlot(i);
                stack.grow(1);
                inv.insertItem(i, stack, false);
            }
        }

    }
}

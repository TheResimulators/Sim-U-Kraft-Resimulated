package com.resimulators.simukraft.common.tileentity.base;

import com.resimulators.simukraft.common.block.BlockControlBox;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Astavie on 19/01/2018 - 7:24 PM.
 */
public class TileBuilderBase extends TileEntity {

	protected Structure structure;
	protected int progress;
    private BlockPos rotatedpos;
    private int xindex;
    private int zindex;
    private int y;
    private int xoffset = 1;
    private int zoffest = 1;
    private int xdir = 1;
    private int zdir = 1;
    private Rotation rotation;
	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	protected void build() {
		if (structure == null) { // Just in case this ever happens
			progress = 0;
		} else {
             xindex = (progress % structure.getWidth());
             zindex = (progress / structure.getWidth()) % structure.getDepth();
			 y = (progress / structure.getWidth()) / structure.getDepth();
			if (!isFinished())
				// TODO: Check for items in adjacent inventories
			    getBuildDirection();
			    IBlockState block = structure.getBlock(xindex,y,zindex);
                world.setBlockState(getPos().add(rotatedpos.getX()*xdir + xoffset,rotatedpos.getY() + 1,rotatedpos.getZ() *xdir + zoffest), block.withRotation(rotation));
                System.out.println("zdir " + zdir);
                System.out.println("block " + block);
                System.out.println((block.getBlock() instanceof BlockControlBox));
			    if (block.getBlock() instanceof BlockControlBox){
			        System.out.println("this is true");
                    ((BlockControlBox)block.getBlock()).name = structure.getName();
                    System.out.println("structure name " + structure.getName());
					((BlockControlBox)block.getBlock()).isresidential = structure.isResidential();
                    ((BlockControlBox)block.getBlock()).createNewTileEntity(world,0);
                }
				progress++;
			}
		}

	protected boolean isFinished() {
		if (structure == null){
			return false;
		}
		return progress >= structure.getWidth() * structure.getHeight() * structure.getDepth();
	}




	private void getBuildDirection(){
	    EnumFacing angle = structure.getFacing();
	        if (angle == EnumFacing.SOUTH) {
	            rotatedpos = new BlockPos(xindex,y,zindex).rotate(Rotation.COUNTERCLOCKWISE_90);
	            rotation = Rotation.COUNTERCLOCKWISE_90;
	            xoffset = 0;
	            zoffest = 1;
	            xdir = -1;
	            zdir = 1;
            }
            if (angle == EnumFacing.WEST){
                rotatedpos = new BlockPos(xindex,y,zindex);
                rotation = Rotation.NONE;
                xoffset = -1;
                zoffest = 0;
                xdir = -1;
                zdir = -1;
            }
            if (angle == EnumFacing.EAST){
                rotatedpos = new BlockPos(xindex,y,zindex);
                xoffset = 1;
                zoffest = 0;
                xdir = 1;
                zdir = 1;
            }
            if (angle == EnumFacing.NORTH){
	            System.out.println("called");
                rotatedpos = new BlockPos(xindex,y,zindex).rotate(Rotation.CLOCKWISE_90);
                xoffset = 0;
                zoffest = -1;
                xdir = -1;
                zdir = 1;
            }
    }
    }

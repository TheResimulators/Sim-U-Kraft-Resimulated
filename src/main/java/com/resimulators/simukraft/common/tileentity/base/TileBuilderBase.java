package com.resimulators.simukraft.common.tileentity.base;

import com.resimulators.simukraft.common.block.BlockConstructorBox;
import com.resimulators.simukraft.common.block.BlockControlBox;
import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.block.Block;
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
	private EnumFacing playerfacing;
	private EnumFacing otherfacing;
	private int xoffset = 1; //either 1 or -1 to make it start on different side of the block in the direction the play
                             // is facing, if 1 then not looking in that direction
    private int zoffset = 1; // when 1, looking in the positive z direction
    private BlockPos posoffset;
    private BlockPos newpos;
    private int zdir;
    private int xdir;
	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	protected void build() {
		if (structure == null) { // Just in case this ever happens
			progress = 0;
		} else {
            int xindex = (progress % structure.getWidth());
            int zindex = (progress / structure.getWidth()) % structure.getDepth();
			int y = (progress / structure.getWidth()) / structure.getDepth();
			if (!isFinished())
				// TODO: Check for items in adjacent inventories
			    getBuildDirection();
			    IBlockState block = structure.getBlock(xindex,y,zindex);
			    posoffset = getPos().add((xindex +xoffset) * xdir ,y,(zindex+zoffset)*zdir);
                newpos = posoffset;
			   // System.out.println("original pos offset " + posoffset);
			    rotateAroundOrigin();
			    rotateAroundOrigin();
			 //   System.out.println("new pos " + newpos);


                world.setBlockState(newpos, block);



			    if (block instanceof BlockControlBox){
                    ((BlockControlBox)block.getBlock()).name = structure.getName();
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
                xoffset = 0;
                zoffset = -1;
                zdir = -1;
                xdir = 1;
            }
            if (angle == EnumFacing.WEST){
	            xoffset = -1;
	            zoffset = 0;
	            zdir = -1;
	            xdir = -1;
            }
    }



    private BlockPos rotateAroundOrigin(){
		int newx;
		int newz;
		System.out.println();
        System.out.println();
        System.out.println();
		System.out.println("x " +getPos().getX());
        System.out.println("z " +getPos().getZ());
        System.out.println(posoffset);
        System.out.println("x pos - offset " +(getPos().getX()-posoffset.getX()));
        System.out.println("z pos - offset " +(getPos().getZ()-posoffset.getZ()));
		newx = getPos().getX() + (getPos().getX()-posoffset.getX());
		newz = getPos().getZ() + (getPos().getZ()-posoffset.getZ());
		System.out.println("newx " + newx);
		System.out.println("newz " + newz);
		System.out.println("this should be newx " + (getPos().getZ() + (getPos().getZ()-posoffset.getZ())));
        newpos = new BlockPos(newx,posoffset.getY(),newz);
		System.out.println("rotated " + newpos);

		return newpos;

    }
    }

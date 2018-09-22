package com.resimulators.simukraft.common.tileentity.base;

import com.resimulators.simukraft.common.block.BlockConstructorBox;
import com.resimulators.simukraft.common.block.BlockControlBox;
import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Astavie on 19/01/2018 - 7:24 PM.
 */
public class TileBuilderBase extends TileEntity {

	protected Structure structure;
	protected int progress;
	private int xdir;
	private int zdir;
	private int xoffset;
	private int zoffset;
	private int z;
	private int x;

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
			    if (xdir == -1) xindex = structure.getDepth()-zindex-1;
			    if (zdir == -1) zindex = structure.getDepth()-zindex-1;
			    System.out.println("xindex " + xindex);
			    System.out.println("zindes " + zindex);
			    System.out.println("width " + structure.getWidth());
			    System.out.println("depth " + structure.getWidth());
			    IBlockState block = structure.getBlock(xindex,y,zindex);
			System.out.println(" block from arrary " + xindex + " " + y + " " + zindex);
				world.setBlockState(getPos().add((x) * xdir + xoffset, y, (z) * zdir + zoffset), block);
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
	    float angle = structure.getFacing().getHorizontalAngle();
	    if (angle <= 45 && Math.max(angle,360) > 315){ //looking south
	        xdir = -1;
	        zdir = 1;
	        xoffset = 0;
	        zoffset = 1;
             x = (progress % structure.getWidth());
             z = (progress / structure.getWidth()) % structure.getDepth();
        }else if(angle <= 135 && angle > 45) { // looking west
	        xdir = -1;
	        zdir = -1;
	        xoffset = -1;
	        zoffset = 0;
            z = (progress % structure.getWidth());
            x = (progress / structure.getWidth()) % structure.getDepth();
        } else if (angle <= 225 && angle > 135 ){ // looking north
	        xdir = 1;
	        zdir = -1;
	        xoffset = 0;
	        zoffset = -1;
            x = (progress % structure.getWidth());
            z = (progress / structure.getWidth()) % structure.getDepth();
        } else if (angle <= 315 && angle > 225) {//looking east
            xdir = 1;
            zdir = 1;
            xoffset = 1;
            zoffset = 0;
            z = (progress % structure.getWidth());
            x = (progress / structure.getWidth()) % structure.getDepth();
        }
    }



             }

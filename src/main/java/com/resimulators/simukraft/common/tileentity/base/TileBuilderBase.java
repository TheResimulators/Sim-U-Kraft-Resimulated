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
    private BlockPos origin;
    private BlockPos newpos;
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
			    zoffset *= playerfacing.getZOffset();
                xoffset *= playerfacing.getXOffset();
			    IBlockState block = structure.getBlock(xindex,y,zindex);
			    BlockPos pos = getPos().add(xindex +1,y,zindex);
			    System.out.println("get pos " + getPos());

			    rotateAroundOrigin();
				world.setBlockState(pos, block);
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
			playerfacing = angle;
			otherfacing = EnumFacing.SOUTH.rotateY();
        }


    private void rotateAroundOrigin(){

    }
    }

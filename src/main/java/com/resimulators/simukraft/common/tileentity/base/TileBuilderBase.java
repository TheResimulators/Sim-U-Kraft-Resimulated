package com.resimulators.simukraft.common.tileentity.base;

import com.resimulators.simukraft.common.block.BlockConstructorBox;
import com.resimulators.simukraft.common.block.BlockControlBox;
import com.resimulators.simukraft.common.tileentity.TileConstructor;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Astavie on 19/01/2018 - 7:24 PM.
 */
public class TileBuilderBase extends TileEntity {

	protected Structure structure;
	protected int progress;
	private int xdir;
	private int zdir;


	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	protected void build() {
		System.out.println("building");
		if (structure == null) { // Just in case this ever happens
			progress = 0;
		} else {
			int x = (progress % structure.getWidth());
			int z = (progress / structure.getWidth()) % structure.getDepth();
			int y = (progress / structure.getWidth()) / structure.getDepth();
			if (!isFinished())
				// TODO: Check for items in adjacent inventories
			    getBuildDirection();
				world.setBlockState(getPos().add((x + 1) * xdir, y, (z) * zdir), structure.getBlock(x, y, z));
			    if (structure.getBlock(x,y,z).getBlock() instanceof BlockControlBox){
                    ((BlockControlBox)structure.getBlock(x,y,z).getBlock()).name = structure.getName();
					((BlockControlBox)structure.getBlock(x,y,z).getBlock()).isresidential = structure.isResidential();
                    ((BlockControlBox)structure.getBlock(x,y,z).getBlock()).createNewTileEntity(world,0);
                }
				progress++;
			}
		}

	protected boolean isFinished() {
		if (structure == null){
			return false;
		}
		System.out.println("progress " + progress + " strutuce thing " +structure.getWidth() * structure.getHeight() * structure.getDepth());
		return progress >= structure.getWidth() * structure.getHeight() * structure.getDepth();
	}




	private void getBuildDirection(){
	    float angle = structure.getFacing().getHorizontalAngle();
	    String direction = null;

	    if (angle <= 45 && angle > 315 ){ //looking south
	        xdir = -1;
	        zdir = 1;
        }else if(angle <= 135 && angle > 45) { // looking west
	        xdir = -1;
	        zdir = -1;
        } else if (angle <= 225 && angle > 135 ){ // looking north
	        xdir = 1;
	        zdir = -1;
        } else if (angle <= 315 && angle > 225) {//looking east
            xdir = 1;
            zdir = 1;
        }
        }



             }

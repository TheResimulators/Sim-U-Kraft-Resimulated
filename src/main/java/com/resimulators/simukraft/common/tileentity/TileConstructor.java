package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.common.tileentity.base.TileBuilderBase;
import net.minecraft.util.ITickable;

/**
 * Created by Astavie on 25/01/2018 - 5:05 PM.
 */
public class TileConstructor extends TileBuilderBase implements ITickable {
	public boolean building;

	@Override
	public void update() {
		if (building)
			if (isFinished())
				building = false;
			else
				build();
	}
}

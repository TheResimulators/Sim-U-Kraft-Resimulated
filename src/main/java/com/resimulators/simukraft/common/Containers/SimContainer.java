package com.resimulators.simukraft.common.Containers;


import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class SimContainer extends Container {
    private EntitySim sim;
    public SimContainer(IInventory playerInv, EntitySim sim)
    {
        this.sim = sim;
        IItemHandler handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null);



        int xPos = 8;
        int yPos = 84;

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x, xPos + x * 18, yPos + 58));
        }
    }
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isSpectator();
    }


}

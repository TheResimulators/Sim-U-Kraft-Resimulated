package com.resimulators.simukraft.common.containers;


import com.resimulators.simukraft.common.ToolFoodSlot;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemFoodBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SimContainer extends Container {
    private EntitySim sim;
    private IItemHandler toolinv;
    private IItemHandler pickups;
    public SimContainer(IInventory playerInv, EntitySim sim)
    {
        this.sim = sim;
        toolinv = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        pickups = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.SOUTH);
        for (int i = 0; i < 9;i++)  this.addSlotToContainer(new ToolFoodSlot(toolinv, i, 8 + 18 * i, 16));
        for (int i = 0; i < 18; i++)  this.addSlotToContainer(new SlotItemHandler(pickups, i, 8 + 18 * (i % 9), 45 + 18 * (i / 9)));
        int xPos = 8;
        int yPos = 91;

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

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        IItemHandler transferinv;
        if (this.inventorySlots.get(fromSlot).getStack().getItem() instanceof ItemFoodBase || this.inventorySlots.get(fromSlot).getStack().getItem() instanceof ItemSword){
            transferinv = toolinv;
        }else{
            transferinv = pickups;
        }
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < transferinv.getSlots()) {
                if (!this.mergeItemStack(current, transferinv.getSlots(), transferinv.getSlots() + 36, true))
                    return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(current, 0, transferinv.getSlots(), false))
                    return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) //Use func_190916_E() instead of stackSize 1.11 only 1.11.2 use getCount()
                slot.putStack(ItemStack.EMPTY); //Use ItemStack.field_190927_a instead of (ItemStack)null for a blank item stack. In 1.11.2 use ItemStack.EMPTY
            else
                slot.onSlotChanged();

            if (current.getCount() == previous.getCount())
                return null;
            slot.onTake(playerIn, current);
        }
        return previous;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
}

package com.resimulators.simukraft.common.containers;


import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SimContainer extends Container {
    private EntitySim sim;
    IItemHandler handler;
    public SimContainer(IInventory playerInv, EntitySim sim)
    {
        this.sim = sim;
        handler = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null);
        this.addSlotToContainer(new SlotItemHandler(handler,0,8,18));
        this.addSlotToContainer(new SlotItemHandler(handler,1,26,18));
        this.addSlotToContainer(new SlotItemHandler(handler,2,44,18));
        this.addSlotToContainer(new SlotItemHandler(handler,3,62,18));
        this.addSlotToContainer(new SlotItemHandler(handler,4,80,18));
        this.addSlotToContainer(new SlotItemHandler(handler,5,98,18));
        this.addSlotToContainer(new SlotItemHandler(handler,6,116,18));
        this.addSlotToContainer(new SlotItemHandler(handler,7,134,18));
        this.addSlotToContainer(new SlotItemHandler(handler,8,152,18));
        this.addSlotToContainer(new SlotItemHandler(handler,9,8,36));
        this.addSlotToContainer(new SlotItemHandler(handler,10,26,36));
        this.addSlotToContainer(new SlotItemHandler(handler,11,44,36));
        this.addSlotToContainer(new SlotItemHandler(handler,12,62,36));
        this.addSlotToContainer(new SlotItemHandler(handler,13,80,36));
        this.addSlotToContainer(new SlotItemHandler(handler,14,98,36));
        this.addSlotToContainer(new SlotItemHandler(handler,15,116,36));
        this.addSlotToContainer(new SlotItemHandler(handler,16,134,36));
        this.addSlotToContainer(new SlotItemHandler(handler,17,152,36));
        this.addSlotToContainer(new SlotItemHandler(handler,18,8,54));
        this.addSlotToContainer(new SlotItemHandler(handler,19,26,54));
        this.addSlotToContainer(new SlotItemHandler(handler,20,44,54));
        this.addSlotToContainer(new SlotItemHandler(handler,21,62,54));
        this.addSlotToContainer(new SlotItemHandler(handler,22,80,54));
        this.addSlotToContainer(new SlotItemHandler(handler,23,98,54));
        this.addSlotToContainer(new SlotItemHandler(handler,24,116,54));
        this.addSlotToContainer(new SlotItemHandler(handler,25,134,54));
        this.addSlotToContainer(new SlotItemHandler(handler,26,152,54));
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

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            if (fromSlot < this.handler.getSlots()) {
                if (!this.mergeItemStack(current, handler.getSlots(), handler.getSlots() + 36, true))
                    return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
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

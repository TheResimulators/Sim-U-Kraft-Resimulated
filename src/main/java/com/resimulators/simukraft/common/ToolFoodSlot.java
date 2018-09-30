package com.resimulators.simukraft.common;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ToolFoodSlot extends SlotItemHandler {
    public ToolFoodSlot(IItemHandler inventory, int id, int x, int y) {
        super(inventory, id,x,y);
    }



    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemBucket || stack.getItem() instanceof ItemShears || stack.getItem() instanceof ItemTool){
            super.isItemValid(stack);
            return true;
        }
        return false;
    }


}

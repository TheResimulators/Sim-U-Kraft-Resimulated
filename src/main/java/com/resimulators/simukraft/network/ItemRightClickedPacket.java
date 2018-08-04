package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class ItemRightClickedPacket implements IMessage {
    EnumHand handin;
    ItemStack stack;
    public ItemRightClickedPacket(){}
    public ItemRightClickedPacket(EnumHand handin,ItemStack stack){
        this.handin = handin;
        this.stack = stack;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        handin = EnumHand.valueOf(ByteBufUtils.readUTF8String(byteBuf));
        stack = ByteBufUtils.readItemStack(byteBuf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf,handin.toString());
        ByteBufUtils.writeItemStack(byteBuf,stack);
    }
}

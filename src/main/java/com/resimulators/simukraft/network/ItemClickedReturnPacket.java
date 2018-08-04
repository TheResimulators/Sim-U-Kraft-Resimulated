package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ItemClickedReturnPacket implements IMessage {
    boolean isdedicated;
    EnumHand handin;
    ItemStack stack;
    int mode;
    public ItemClickedReturnPacket(){}
    public ItemClickedReturnPacket(boolean isdedicated, EnumHand handin, ItemStack stack, int mode){
        this.isdedicated = isdedicated;
        this.handin = handin;
        this.stack = stack;
        this.mode = mode;
    }
    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.isdedicated = byteBuf.readBoolean();
        handin = EnumHand.valueOf(ByteBufUtils.readUTF8String(byteBuf));
        stack = ByteBufUtils.readItemStack(byteBuf);
        mode = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
    byteBuf.writeBoolean(isdedicated);
    ByteBufUtils.writeUTF8String(byteBuf,handin.toString());
    ByteBufUtils.writeItemStack(byteBuf,stack);
    byteBuf.writeInt(mode);
    }
}

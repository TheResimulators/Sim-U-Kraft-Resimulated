package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.enums.EnumDay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateDayHandler implements IMessageHandler<UpdateDayPacket,IMessage> {
    @Override
    public IMessage onMessage(UpdateDayPacket message, MessageContext ctx){
        IThreadListener mainthread = Minecraft.getMinecraft();

        mainthread.addScheduledTask(()->{
            EnumDay.DayStorage.setDayInt(message.daynum);


        });


        return null;
    }
}

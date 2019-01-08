package com.resimulators.simukraft.common.enums;

import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.UpdateDayPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public enum EnumDay {
    MONDAY ("Monday",0),
    TUESDAY("Tuesday",1),
    WEDNESDAY("Wednesday",2),
    THRUSDAY("Thursday",3),
    FRIDAY("Friday",4),
    SATURDAY("Saturday",5),
    SUNDAY("Sunday",6);
    String string;
    int num;


    public static EnumDay getDay(int num){
        for (EnumDay day: EnumDay.values()){
            if (day.num == num){
                return day;
            }
        }
        return null;
    }
    EnumDay(String string,int num){
        this.num = num;
        this.string = string;

    }
    @Mod.EventBusSubscriber
    public static class DayStorage implements INBTSerializable<NBTTagCompound>{
        private static int dayint = 0;
        private static boolean added = false;
        private static int totaldays = 0;
        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("dayint",dayint);
            compound.setBoolean("added",added);
            compound.setInteger("totalday",totaldays);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            dayint = nbt.getInteger("dayint");
            added = nbt.getBoolean("added");
            totaldays =nbt.getInteger("totalday");

        }


        @SubscribeEvent
        public static void DayChange(TickEvent.WorldTickEvent event){
            if (!event.world.isRemote){

                if (event.world.getWorldTime()%24000 == 0 && !added) {
                    added = true;
                    dayint++;
                    dayint = dayint%7;
                    addtotalday();
                    updateAll();
                }else{
                    if (event.world.getWorldTime() != 0) {
                        added = false;
                    }
                }
        }}

        public static int getDayInt(){
            return dayint;
    }
        public static void setDayInt(int newdayint){
            dayint = newdayint;
        }
        public static void addtotalday(){
            totaldays++;
        }
        public static int getTotaldays(){
            return totaldays;
        }
    public static void updateAll(){
        PacketHandler.INSTANCE.sendToAll(new UpdateDayPacket(dayint));

    }
    }
}


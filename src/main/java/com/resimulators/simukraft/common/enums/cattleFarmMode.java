package com.resimulators.simukraft.common.enums;

public class cattleFarmMode {


    public enum FarmMode{
        KILL("kill"),
        MILK("milk");

        String mode;


        FarmMode(String string){
        this.mode = string;
        }
    }


}

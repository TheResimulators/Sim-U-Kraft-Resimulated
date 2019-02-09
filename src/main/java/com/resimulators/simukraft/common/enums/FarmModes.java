package com.resimulators.simukraft.common.enums;

public class FarmModes {

    public enum CowMode {
        KILL("kill"),
        MILK("milk");
        String mode;
        CowMode(String string){
        this.mode = string;
        }
    }

    public enum SheepMode{
        KILL("kill"),
        SHEAR("shear");
        String mode;
        SheepMode(String string){this.mode = string;}
    }
}

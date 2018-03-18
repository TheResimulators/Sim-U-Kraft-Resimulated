package com.resimulators.simukraft.common.entity.entitysim;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabbe on 11/03/2018 - 5:40 PM.
 */
public class SpecialNameStorage {
    public static List<String> specialNames = new ArrayList<>();
    public static List<String> femaleIndex = new ArrayList<>();

    public static void init() {
        specialNames.add("General5001");
        specialNames.add("Div4Wom4n");
        specialNames.add("fabbe50");
        specialNames.add("zakando");
        specialNames.add("Ellisenator");
        specialNames.add("jakegalen");
        specialNames.add("brodydavid1126");
        specialNames.add("Maiyr_Cordeth");
        specialNames.add("Korath");

        registerGenders();
    }

    private static void registerGenders() {
        femaleIndex.add("Div4Wom4n");
        femaleIndex.add("fabbe50");
    }
}

package com.resimulators.simukraft.temp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabbe on 21/01/2018 - 1:03 AM.
 */
public class NameStorage {
    //TODO: switch over to JSON storage

    public static List<String> malenames = new ArrayList<>();
    public static List<String> femalenames = new ArrayList<>();

    public static void init() {
        femalenames.add("Anna");
        femalenames.add("Annabelle");
        femalenames.add("Charlie");

        malenames.add("Andreas");
        malenames.add("Andrew");
        malenames.add("Charles");
    }
}

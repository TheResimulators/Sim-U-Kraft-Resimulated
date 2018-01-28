package com.resimulators.simukraft.common.entity.entitysim;

import com.resimulators.simukraft.Reference;
import net.minecraftforge.fml.common.Loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fabbe on 21/01/2018 - 1:03 AM.
 */
public class NameStorage {
    //TODO: switch over to JSON storage

    public static List<String> malenames = new ArrayList<>();
    public static List<String> femalenames = new ArrayList<>();

    public static void init() {
        try {
            femalenames = loadFemaleNames();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            malenames = loadMaleNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Work in progress
    private static List<String> loadFemaleNames() throws IOException {
        if (!new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "femalenames.txt").exists())
            new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID).mkdir();
        File femalenames = new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "femalenames.txt");
        BufferedReader reader = new BufferedReader(new FileReader(femalenames));
        return reader.lines().collect(Collectors.toList());
    }

    //Work in progress
    private static List<String> loadMaleNames() throws IOException {
        if (!new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "malenames.txt").exists())
            new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID).mkdir();
        File malenames = new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "malenames.txt");
        BufferedReader reader = new BufferedReader(new FileReader(malenames));
        return reader.lines().collect(Collectors.toList());
    }
}

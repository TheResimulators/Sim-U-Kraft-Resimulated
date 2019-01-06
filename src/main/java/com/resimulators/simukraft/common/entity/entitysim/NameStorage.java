package com.resimulators.simukraft.common.entity.entitysim;

import com.resimulators.simukraft.Reference;
import net.minecraftforge.fml.common.Loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (!new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "femalenames.txt").exists()) {
            new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID).mkdir();
            new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID + "/femalenames.txt").createNewFile();
            Files.write(new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID + "/femalenames.txt").toPath(), generateFemaleNameList(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        File femalenames = new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "femalenames.txt");
        BufferedReader reader = new BufferedReader(new FileReader(femalenames));
        return reader.lines().collect(Collectors.toList());
    }

    //Work in progress
    private static List<String> loadMaleNames() throws IOException {
        if (!new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "malenames.txt").exists()) {
            new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID).mkdir();
            new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID + "/malenames.txt").createNewFile();
            Files.write(new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID + "/malenames.txt").toPath(), generateMaleNameList(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        File malenames = new File(Loader.instance().getConfigDir() + "/" + Reference.MOD_ID, "malenames.txt");
        BufferedReader reader = new BufferedReader(new FileReader(malenames));
        return reader.lines().collect(Collectors.toList());
    }

    private static List<String> generateFemaleNameList() {
        String names = "Arminda\n" +
                "Yen\n" +
                "Willette\n" +
                "Mindi\n" +
                "Krystle\n" +
                "Natalie\n" +
                "Amanda\n" +
                "Colene\n" +
                "Santana\n" +
                "Darlene\n" +
                "Nana\n" +
                "Yuri\n" +
                "Chia\n" +
                "Lady\n" +
                "Penney\n" +
                "Zita\n" +
                "Goldie\n" +
                "Diedra\n" +
                "Dawne\n" +
                "Emma\n" +
                "Elena\n" +
                "Phylis\n" +
                "Celestina\n" +
                "Mira\n" +
                "Sadye\n" +
                "Meda\n" +
                "Rachael\n" +
                "Donnette\n" +
                "Katrice\n" +
                "Denae\n" +
                "Afton\n" +
                "Charlotte\n" +
                "Bethany\n" +
                "Junko\n" +
                "Bonita\n" +
                "Sandra\n" +
                "Barbar\n" +
                "Enid\n" +
                "Emelina\n" +
                "Nanette\n" +
                "Sarai\n" +
                "Thi\n" +
                "Shannan\n" +
                "Wilma\n" +
                "Raguel\n" +
                "Ludie\n" +
                "Louisa\n" +
                "Lourdes\n" +
                "Cristen\n" +
                "Bess";
        String[] tempA = names.split("\\n");
        List<String> temp = new ArrayList<>(Arrays.asList(tempA));
        return temp;
    }

    private static List<String> generateMaleNameList() {
        String names = "Julian\n" +
                "Derick\n" +
                "Ronnie\n" +
                "Jeremy\n" +
                "Carson\n" +
                "Kim\n" +
                "Hank\n" +
                "Jospeh\n" +
                "Lavern\n" +
                "Taylor\n" +
                "Marshall\n" +
                "Cordell\n" +
                "Andreas\n" +
                "Chase\n" +
                "Timothy\n" +
                "Lanny\n" +
                "Craig\n" +
                "William\n" +
                "Karl\n" +
                "Manual\n" +
                "Cletus\n" +
                "Agustin\n" +
                "Chad\n" +
                "Mauro\n" +
                "Thurman\n" +
                "Jeromy\n" +
                "Kareem\n" +
                "Jerome\n" +
                "Rudolph\n" +
                "Theodore\n" +
                "Jamel\n" +
                "Porter\n" +
                "Domingo\n" +
                "Thad\n" +
                "Elvin\n" +
                "Napoleon\n" +
                "Oswaldo\n" +
                "Randolph\n" +
                "Johnnie\n" +
                "Jeff\n" +
                "Grover\n" +
                "Noe\n" +
                "Aubrey\n" +
                "Corey\n" +
                "Romeo\n" +
                "Garland\n" +
                "Silas\n" +
                "Pedro\n" +
                "Mario\n" +
                "Isreal";
        String[] tempA = names.split("\\n");
        List<String> temp = new ArrayList<>(Arrays.asList(tempA));
        return temp;
    }
}

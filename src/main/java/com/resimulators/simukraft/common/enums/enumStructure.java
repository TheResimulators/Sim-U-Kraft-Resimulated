package com.resimulators.simukraft.common.enums;

import com.resimulators.simukraft.common.tileentity.*;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;

public class enumStructure {

    public enum FarmStructure {
        CATTLE ("cattle_farm", TileCattle::new),
        CHICKEN ("chicken_farm", TileChicken::new),
        SHEEP("sheep_farm", TileSheep::new),
        PIG("pig_farm", TilePig::new),
        GLASS("glass_factory",TileCattle::new),
        GROCERIES("groceries_store", TileCattle::new),
        RESIDENTIAL ("residential", TileResidential::new);
        public String structure;
        public Supplier<TileEntity> teSupplier;


        public static TileEntity byName(String name) {
            for (FarmStructure value: FarmStructure.values()) {
                if (name.equals(value.structure)) return value.teSupplier.get();
            }
            return null;
        }
        FarmStructure(String structureFile, Supplier<TileEntity> teSupplier) {
            this.structure = structureFile;
            this.teSupplier = teSupplier;
        }
    }



    }

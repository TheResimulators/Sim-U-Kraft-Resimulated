package com.resimulators.simukraft.structure;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

/**
 * Created by fabbe on 06/01/2019 - 2:40 PM.
 */
public class StructureBuilding {
    /* Building Variables */
    private String name;
    private EnumCategory category;
    private StructureBoundingBox bounds;
    private String profession;

    /**
     * @param name - Name of building.
     * @param category - Category of building.
     * @param pos1 - Corner A of building.
     * @param pos2 - Corner B of building.
     */
    public StructureBuilding(String name, EnumCategory category, BlockPos pos1, BlockPos pos2) {
        this(name, category, new StructureBoundingBox(pos1, pos2));
    }

    /**
     * @param name - Name of building.
     * @param category - Category of building.
     * @param bounds - Bounds of the building.
     */
    public StructureBuilding(String name, EnumCategory category, StructureBoundingBox bounds) {
        this.name = name;
        this.category = category;
        this.bounds = bounds;
    }

    /**
     * @return name of building.
     */
    public String getName() {
        return name;
    }

    /**
     * @return category of building.
     */
    public EnumCategory getCategory() {
        return category;
    }

    /**
     * @return bounds of the building.
     */
    public StructureBoundingBox getBounds() {
        return bounds;
    }

    /**
     * @return the building as NBT.
     */
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", getName());
        nbt.setInteger("category", getCategory().id);
        nbt.setIntArray("bounds", new int[]{getBounds().minX, getBounds().minY, getBounds().minZ, getBounds().maxX, getBounds().maxY, getBounds().maxZ});
        return nbt;
    }


    /**
     * @param nbt - Building NBT
     * @return the building extracted from NBT.
     */
    public StructureBuilding deserializeNBT(NBTTagCompound nbt) {
        String name = "null";
        EnumCategory category = EnumCategory.UNKNOWN;
        StructureBoundingBox bounds = null;
        if (nbt.hasKey("name"))
            name = nbt.getString("name");
        if (nbt.hasKey("category"))
            category = EnumCategory.getCategoryFromID(nbt.getInteger("category"));
        if (nbt.hasKey("bounds"))
            bounds = new StructureBoundingBox(nbt.getIntArray("bounds"));
        return new StructureBuilding(name, category, bounds);
    }


    public String getProfession() {
        return profession;
    }

    public enum EnumCategory {
        UNKNOWN(0, "unknown"),
        RESIDENTIAL(1, "residential"),
        INDUSTRIAL(2, "industrial"),
        COMMERCIAL(3,"commercial");

        private int id;
        private String name;

        EnumCategory(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static EnumCategory getCategoryFromID(int id) {
            if (EnumCategory.values().length < id) {
                return EnumCategory.values()[id];
            }
            return UNKNOWN;
        }

        public static String getNameFromID(int id) {
            for (EnumCategory c : EnumCategory.values()) {
                if (c.id == id) {
                    return c.name;
                }
            }
            return "";
        }

        public static int getIDFromName(String name) {
            for (EnumCategory c : EnumCategory.values()) {
                if (c.name.equals(name))
                    return c.id;
            }
            return 0;
        }
    }
}

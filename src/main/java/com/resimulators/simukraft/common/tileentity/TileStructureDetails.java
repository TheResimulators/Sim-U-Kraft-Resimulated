package com.resimulators.simukraft.common.tileentity;

import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.structure.StructureBuilding;
import com.resimulators.simukraft.structure.TemplatePlus;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class TileStructureDetails extends TileEntity {
    private StructureBuilding.EnumCategory category;
    private short capacity = 0; // only used for residential structures
    private String profession = "";
    private int cost = 0;
    private int totalBlocks = 0;
    private ArrayList<BlockData> blocks = new ArrayList<>();
    private TemplatePlus structure;


    public BlockData createBlockData(Block block){
        return new BlockData(block);
    }

    private void addBlockData(BlockData data){
        for (BlockData blockData:blocks){
            if (data.block == blockData.block){
                return;
            }
        }
        blocks.add(data);
    }

    public void addDataToStructure(){
        //TODO: to be done when structure is changed to accommodate this data
        if (structure != null){
            //add data to structure to save it. data will be used when the structure is built to determined what tile entity
            //should be add to the block
        }
    }


    private NBTTagList arrayToNBT(){
        NBTTagList list = new NBTTagList();
        for (BlockData data: blocks){
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("blockid",Block.getIdFromBlock(data.block));
            compound.setInteger("amount",data.getAmount());
            list.appendTag(compound);
        }
        return list;
    }

    private void arrayFromNBT(NBTTagList list){
        for (int i = 0;i<list.tagCount();i++){
            NBTTagCompound compound = list.getCompoundTagAt(i);
            blocks.add(new BlockData(Block.getBlockById(compound.getInteger("blockid")),compound.getInteger("amount")));
        }
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        compound.setInteger("categoryID",category.getId());
        compound.setShort("capacity",capacity);
        compound.setString("profession",profession);
        compound.setInteger("cost",cost);
        compound.setInteger("totalblocks",totalBlocks);
        compound.setTag("blocks",arrayToNBT());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound){
        category = StructureBuilding.EnumCategory.getCategoryFromID(compound.getInteger("catergoryID"));
        capacity = compound.getShort("capacity");
        profession = compound.getString("profession");
        cost = compound.getInteger("cost");
        totalBlocks = compound.getInteger("totalblocks");
        arrayFromNBT(compound.getTagList("blocks", Constants.NBT.TAG_COMPOUND));
    }

    private void setStructure(TemplatePlus structure){
        this.structure = structure;
    }

    private class BlockData {
        private Block block;
        private int amount;
        BlockData(Block block){
            this(block,0);
        }
        BlockData(Block block, int amount){
            this.block = block;
            this.amount = amount;
        }

        boolean isSameBlock(Block block){
            return this.block == block;
        }

        void addblock(){
            amount++;
        }

        int getAmount(){
            return amount;
        }
        Block getBlock(){
            return block;
        }
    }
}

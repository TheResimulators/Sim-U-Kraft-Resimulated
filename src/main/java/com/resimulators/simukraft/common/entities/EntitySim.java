package com.resimulators.simukraft.common.entities;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.ai.AISimChildPlay;
import com.resimulators.simukraft.temp.NameStorage;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by fabbe on 19/01/2018 - 11:36 AM.
 */
public class EntitySim extends EntityAgeable implements INpc {
    private static final DataParameter<Integer> VARIATION = EntityDataManager.createKey(EntitySim.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntitySim.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FEMALE = EntityDataManager.createKey(EntitySim.class, DataSerializers.BOOLEAN);

    private boolean isPlaying;
    private EntityPlayer commander;

    private boolean areAdditionalTasksSet;
    private int wealth;

    private final InventoryBasic inventory;

    public EntitySim(World world) {
        this(world, 0, 0, false);
    }

    public EntitySim(World worldIn, int variationID, int professionID, boolean female) {
        super(worldIn);
        this.inventory = new InventoryBasic("Items", false, 8);
        this.setVariation(variationID);
        this.setProfession(professionID);
        this.setFemale(female);
        this.setSize(0.6f, 1.95f);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
        this.setCustomNameTag("Sim (WIP)");
        this.setAlwaysRenderNameTag(false);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0f, 0.6d, 0.6d));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.6d));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
    }

    private void setAdditionalAITasks() {
        if (!this.areAdditionalTasksSet) {
            this.areAdditionalTasksSet = true;
            if (this.isChild())
                this.tasks.addTask(8, new AISimChildPlay(this, 0.32d));
        }
    }

    @Override
    protected void onGrowingAdult() {
        //Add specific profession activities here.

        super.onGrowingAdult();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        boolean flag = itemStack.getItem() == Items.NAME_TAG;

        if (flag) {
            itemStack.interactWithEntity(player, this, hand);
            return true;
        } else if (!this.holdingSpawnEggOfClass(itemStack, this.getClass()) && this.isEntityAlive() && !isRecievingOrders() && !isChild() && !player.isSneaking()) {
            if (this.world.isRemote) {
                this.setCommander(player);
                NBTTagCompound tags = player.getEntityData();
                tags.setInteger("simInteract", this.getEntityId());
                player.writeToNBT(tags);
            }
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIATION, rand.nextInt(50));
        this.dataManager.register(PROFESSION, 0);
        this.dataManager.register(FEMALE, randomizeGender());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variation", this.getVariation());
        compound.setInteger("profession", this.getProfession());
        compound.setBoolean("female", this.getFemale());
        compound.setInteger("Riches", this.wealth);

        NBTTagList nbtTagList = new NBTTagList();

        for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
            ItemStack itemStack = this.inventory.getStackInSlot(i);

            if (!itemStack.isEmpty()) {
                nbtTagList.appendTag(itemStack.writeToNBT(new NBTTagCompound()));
            }
        }

        compound.setTag("Inventory", nbtTagList);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariation(compound.getInteger("Variation"));
        this.setProfession(compound.getInteger("Profession"));
        this.setFemale(compound.getBoolean("female"));
        this.wealth = compound.getInteger("Riches");

        NBTTagList nbtTagList = compound.getTagList("Inventory", 10);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            ItemStack itemStack = new ItemStack(nbtTagList.getCompoundTagAt(i));

            if (!itemStack.isEmpty()) {
                this.inventory.addItem(itemStack);
            }
        }

        this.setCanPickUpLoot(true);
        this.setAdditionalAITasks();
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
    }

    public void setVariation(int variationID) {
        this.dataManager.set(VARIATION, variationID);
    }

    public int getVariation() {
        return Math.max(this.dataManager.get(VARIATION), 0);
    }

    public void setProfession(int professionID) {
        this.dataManager.set(PROFESSION, professionID);
    }

    public int getProfession() {
        return Math.max(this.dataManager.get(PROFESSION), 0);
    }

    public void setFemale(boolean female) {
        this.dataManager.set(FEMALE, female);
    }

    public boolean getFemale() {
        return this.dataManager.get(FEMALE);
    }

    public String getGender() {
        if (this.dataManager.get(FEMALE))
            return "female";
        else
            return "male";
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        EntitySim entitySim = new EntitySim(this.world);
        entitySim.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitySim)), null);
        return entitySim;
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData livingData = super.onInitialSpawn(difficulty, livingdata);
        this.setFemale(randomizeGender());
        if (this.getFemale()) {
            this.setCustomNameTag(NameStorage.femalenames.get(new Random().nextInt(NameStorage.femalenames.size())));
            this.setVariation(rand.nextInt(10));
        } else {
            this.setCustomNameTag(NameStorage.malenames.get(new Random().nextInt(NameStorage.malenames.size())));
            this.setVariation(rand.nextInt(5));
        }

        this.setAdditionalAITasks();
        return livingData;
    }

    public void setCommander(@Nullable EntityPlayer player) {
        this.commander = player;
    }

    @Nullable
    public EntityPlayer getCommander() {
        return this.commander;
    }

    public boolean isRecievingOrders() {
        return this.commander != null;
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer player) {
        return false;
    }

    public InventoryBasic getInventory() {
        return this.inventory;
    }

    @Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
        //Used for picking up items
    }

    private boolean canSimPickupItem(Item item) {
        //Used for specifying items Sim can pick up
        return false;
    }

    //return false for male, return true for female
    private boolean randomizeGender() {
        int dice = rand.nextInt(2);
        return dice != 0 && dice == 1;
    }
}

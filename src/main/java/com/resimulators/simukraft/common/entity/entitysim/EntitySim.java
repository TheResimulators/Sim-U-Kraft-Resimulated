package com.resimulators.simukraft.common.entity.entitysim;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.ai.AISimBuild;
import com.resimulators.simukraft.common.entity.ai.AISimChildPlay;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import com.resimulators.simukraft.init.ModItems;
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
import net.minecraft.world.gen.structure.StructureBoundingBox;

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

    //Builder profession related
    private Structure structure;
    private boolean isAllowedToBuild;
    private BlockPos startPos;

    //Farmer profession related
    private BlockPos farmPos1;
    private BlockPos farmPos2;
    private StructureBoundingBox bounds;


    private boolean areAdditionalTasksSet;
    private int wealth;

    private final InventoryBasic inventory;

    public EntitySim(World worldIn) {
        super(worldIn);
        this.inventory = new InventoryBasic("Items", false, 8);
        this.setSize(0.6f, 1.95f);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
        this.setCustomNameTag("Sim (WIP)");
        this.setAlwaysRenderNameTag(false);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8.0f, 0.6d, 0.6d));
        this.setProfessionAIs();
        this.tasks.addTask(3, new EntityAIMoveIndoors(this));
        this.tasks.addTask(4, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(5, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6d));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
    }

    private void setProfessionAIs() {
        this.tasks.addTask(2, new AISimBuild(this));
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
        boolean flag2 = itemStack.getItem() == ModItems.DEBUG || itemStack.getItem() == ModItems.BLUEPRINT;
        if (flag) {
            itemStack.interactWithEntity(player, this, hand);
            return true;
        } else if (!this.holdingSpawnEggOfClass(itemStack, this.getClass()) && this.isEntityAlive() && !isReceivingOrders() && !isChild() && !player.isSneaking() && !flag2) {
            this.setCommander(player);
            player.addTag("ID" + this.getEntityId());
            player.openGui(SimUKraft.instance, GuiHandler.GUI_SIM, this.world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIATION, 0);
        this.dataManager.register(PROFESSION, 0);
        this.dataManager.register(FEMALE, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variation", this.getVariation());
        compound.setInteger("Profession", this.getProfession());
        compound.setBoolean("Female", this.getFemale());
        compound.setInteger("Riches", this.wealth);
        if (this.getFarmPos1() != null)
            compound.setIntArray("FarmPos1", new int[]{this.getFarmPos1().getX(), this.getFarmPos1().getY(), this.getFarmPos1().getZ()});
        if (this.getFarmPos2() != null)
            compound.setIntArray("FarmPos2", new int[]{this.getFarmPos2().getX(), this.getFarmPos2().getY(), this.getFarmPos2().getZ()});

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

        if (compound.hasKey("Variation"))
            this.setVariation(compound.getInteger("Variation"));
        if (compound.hasKey("Profession"))
            this.setProfession(compound.getInteger("Profession"));
        if (compound.hasKey("Female"))
            this.setFemale(compound.getBoolean("Female"));
        if (compound.hasKey("Riches"))
            this.wealth = compound.getInteger("Riches");
        if (compound.hasKey("FarmPos1"))
            this.setFarmPos1(new BlockPos(compound.getIntArray("FarmPos1")[0], compound.getIntArray("FarmPos1")[1], compound.getIntArray("FarmPos1")[2]));
        if (compound.hasKey("FarmPos2"))
            this.setFarmPos2(new BlockPos(compound.getIntArray("FarmPos2")[0], compound.getIntArray("FarmPos2")[1], compound.getIntArray("FarmPos2")[2]));
        if (compound.hasKey("FarmPos1") && compound.hasKey("FarmPos1"))
            this.setBounds(new StructureBoundingBox(this.getFarmPos1(), this.getFarmPos2()));

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

    public String getLabeledProfession() {
        switch (getProfession()) {
            case 0:
                return "Nitwit";
            case 1:
                return "Builder";
            case 2:
                return "Farmer";
            case 3:
                return "Fisher";
            case 4:
                return "Butcher";
        }

        return "Oh well, this is awkward.";
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setStartPos(BlockPos startPos) {
        this.startPos = startPos;
    }

    public BlockPos getStartPos() {
        return startPos;
    }

    public void setFarmPos1(BlockPos farmPos1) {
        this.farmPos1 = farmPos1;
    }

    public BlockPos getFarmPos1() {
        return farmPos1;
    }

    public void setFarmPos2(BlockPos farmPos2) {
        this.farmPos2 = farmPos2;
    }

    public BlockPos getFarmPos2() {
        return farmPos2;
    }

    public void setBounds(StructureBoundingBox bounds) {
        this.bounds = bounds;
    }

    public StructureBoundingBox getBounds() {
        return bounds;
    }

    public void setAllowedToBuild(boolean allowed) {
        this.isAllowedToBuild = allowed;
    }

    public boolean isAllowedToBuild() {
        return isAllowedToBuild;
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
        this.setFemale(randomizeBoolean());
        this.setProfession(rand.nextInt(2)); //TODO: add more professions.
        if (this.getFemale()) {
            if (NameStorage.femalenames.size() != 0) {
                this.setCustomNameTag(NameStorage.femalenames.get(new Random().nextInt(NameStorage.femalenames.size())));
            }
            this.setVariation(rand.nextInt(10));
        } else {
            if (NameStorage.malenames.size() != 0) {
                this.setCustomNameTag(NameStorage.malenames.get(new Random().nextInt(NameStorage.malenames.size())));
            }
            this.setVariation(rand.nextInt(5));
        }

        this.setAdditionalAITasks();
        this.writeEntityToNBT(this.getEntityData());
        return livingData;
    }

    public void setCommander(@Nullable EntityPlayer player) {
        this.commander = player;
    }

    @Nullable
    public EntityPlayer getCommander() {
        return this.commander;
    }

    public boolean isReceivingOrders() {
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

    private boolean randomizeBoolean() {
        int dice = rand.nextInt(2);
        return dice != 0 && dice == 1;
    }

    @Override
    public float getSwingProgress(float partialTickTime) {
        return super.getSwingProgress(partialTickTime);
    }
}

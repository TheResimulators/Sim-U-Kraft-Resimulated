package com.resimulators.simukraft.common.entity.entitysim;

import com.mojang.authlib.GameProfile;
import com.resimulators.simukraft.ConfigHandler;
import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;

import com.resimulators.simukraft.common.entity.ai.*;
import com.resimulators.simukraft.common.entity.ai.pathfinding.CustomPathNavigateGround;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.enums.FarmModes;
import com.resimulators.simukraft.init.ModItems;
import com.resimulators.simukraft.network.HungerPacket;
import com.resimulators.simukraft.structure.StructureBuilding;
import net.minecraft.block.BlockChest;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.sql.Time;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;
import java.util.Random;

/**
 * Created by fabbe on 19/01/2018 - 11:36 AM.
 */
public class EntitySim extends EntityAgeable implements INpc, ICapabilityProvider {
    private static final DataParameter<Integer> VARIATION = EntityDataManager.createKey(EntitySim.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntitySim.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FEMALE = EntityDataManager.createKey(EntitySim.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STAFF = EntityDataManager.createKey(EntitySim.class, DataSerializers.BOOLEAN);

    private boolean isPlaying;
    private EntityPlayer commander;
    private GameProfile playerProfile;
    private Time time;
    //Builder profession related
    private Template structure;
    private boolean isAllowedToBuild;
    private BlockPos startPos;
    private EnumFacing facing;
    //Inventory
    private ItemStackHandler toolinv;
    private ItemStackHandler pickups;
    //Food related
    private int hunger = 20;
    private int maxhunger = 20;
    private int counter;
    private int heal_counter = 0;
    //Farmer profession related
    private BlockPos farmPos1;
    private BlockPos farmPos2;
    private StructureBoundingBox bounds;
    //Faction related
    private long Factionid;
    private boolean areAdditionalTasksSet;
    private int wealth;
    //Job Common
    private BlockPos jobBlockPos;
    //workstatus: 0 = not working, 1 = going to work, 2 = working,3 = finish work
    private int workstatus = 0;
    private boolean returntoblock = false;
    //workcooldown: cooldown for when the sims worktime has expired. when is zero allows sims to work
    private int workcooldown;
    //worktime: increase every second the sim is working to only allow working a
    //certain amount per day. this allows them to do other needs e.g eat socialize
    private int worktime = 0;
    private final InventoryBasic inventory;
    //Housing
    private StructureBuilding homeLocation;


    //Inventory AI related
    private BlockChest emptychest;
    private BlockPos emptychestpos;
    //Milking related
    private EntityCow cowtarget;
    private FarmModes.CowMode cowmode = FarmModes.CowMode.KILL;

    //Sheep related
    private EntitySheep sheeptarget;
    private FarmModes.SheepMode sheepmode = FarmModes.SheepMode.SHEAR;

    //Teleportation related
    private boolean shouldteleport = false;
    private BlockPos teleporttarget; 
    private boolean teleport;
    private int teleportdelay = 140;
    private boolean particlspawning;



    public EntitySim(World worldIn) {
        super(worldIn);
        this.navigator = new CustomPathNavigateGround(this,this.world);
        this.inventory = new InventoryBasic("Items", false, 8);
        this.setSize(0.6f, 1.95f);
        ((CustomPathNavigateGround) this.getNavigator()).setBreakDoors(false);
        ((CustomPathNavigateGround) this.getNavigator()).setEnterDoors(true);
        this.setCanPickUpLoot(true);
        this.setCustomNameTag("Sim");
        this.setAlwaysRenderNameTag(false);
        this.pickups = new ItemStackHandler(18);
        this.toolinv = new ItemStackHandler(9);
        time = new Time(System.currentTimeMillis());
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new AISimEat(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new AISimOpenGate(this,true));
        this.tasks.addTask(2, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8.0f, 0.6d, 0.6d));
        this.setProfessionAIs();
        this.tasks.addTask(4, new EntityAIMoveIndoors(this));
        this.tasks.addTask(5, new EntityAIRestrictOpenDoor(this));
        //this.tasks.addTask(6,new AISimReturnToBlock(this));
        this.tasks.addTask(6, new AISimTeleport(this));
        this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(8, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6d));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
    }

    private void setProfessionAIs() {
        this.tasks.addTask(2, new AISimBuild(this));
        this.tasks.addTask(2, new AISimQuarryMine(this,this.world));
        this.tasks.addTask(3, new AISimGotoToWork(this));
        this.tasks.addTask(4, new AISimKillCow(this));
        this.tasks.addTask(5, new AISimGetInventory(this));
        this.tasks.addTask(6, new AISimEmptyInventory(this));
        this.tasks.addTask(7, new AISimMilkCow(this));
        this.tasks.addTask(6, new AiSimShearSheep(this));
        this.tasks.addTask(4, new AiSimAttackNearest(0.7,true,this));
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
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        boolean flag = itemStack.getItem() == Items.NAME_TAG;
        boolean flag3 = System.currentTimeMillis() - time.getTime() >= 3000;
        boolean flag2 = itemStack.getItem() == ModItems.DEBUG || itemStack.getItem() == ModItems.BLUEPRINT;
        if (flag) {
            if (flag3) {
                if (!world.isRemote) {
                    if (!itemStack.getDisplayName().equals(this.getName())) {
                        player.sendStatusMessage(new TextComponentString(getName() + ": " + new String[]{"I don't want that name.", "Don't touch me with that.", "Hey, back off!", "Ruuude.", "No."}[rand.nextInt(5)]), true);
                        time.setTime(System.currentTimeMillis());
                    } else {
                        player.sendStatusMessage(new TextComponentString(getName() + ": That's already my name."), true);
                        time.setTime(System.currentTimeMillis());
                    }
                }
            }
            return true;
        } else if (!this.holdingSpawnEggOfClass(itemStack, this.getClass()) && this.isEntityAlive() && !isReceivingOrders() && !isChild() && !player.isSneaking() && !flag2) {
            this.setCommander(player);
            player.addTag("ID" + this.getEntityId());
            player.openGui(SimUKraft.instance, GuiHandler.GUI.SIM.ordinal(), this.world, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
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
        this.dataManager.register(STAFF, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variation", this.getVariation());
        compound.setInteger("Profession", this.getProfession());
        compound.setBoolean("Female", this.getFemale());
        compound.setBoolean("Staff", this.getStaff());
        compound.setInteger("Riches", this.wealth);
        if (this.playerProfile != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
            compound.setTag("CustomSkinOwner", nbttagcompound);
        }
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
        if (toolinv != null) {
            compound.setTag("SimToolInv", toolinv.serializeNBT());
        }
        if (pickups != null)compound.setTag("PickUpInv",pickups.serializeNBT());
        compound.setTag("Inventory", nbtTagList);
        compound.setInteger("hunger", this.hunger);
        compound.setLong("Factionid",Factionid);
        compound.setInteger("workstatus",workstatus);
        if (this.getHomeLocation() != null) {
            compound.setTag("HomeLocation", getHomeLocation().serializeNBT());
        }
        compound.setBoolean("shouldteleport",shouldteleport);
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
        if (compound.hasKey("Staff"))
            this.setStaff(compound.getBoolean("Staff"));
        if (compound.hasKey("Riches"))
            this.wealth = compound.getInteger("Riches");
        if (compound.hasKey("CustomSkinOwner", 10))
            this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("CustomSkinOwner"));
        if (compound.hasKey("FarmPos1"))
            this.setFarmPos1(new BlockPos(compound.getIntArray("FarmPos1")[0], compound.getIntArray("FarmPos1")[1], compound.getIntArray("FarmPos1")[2]));
        if (compound.hasKey("FarmPos2"))
            this.setFarmPos2(new BlockPos(compound.getIntArray("FarmPos2")[0], compound.getIntArray("FarmPos2")[1], compound.getIntArray("FarmPos2")[2]));
        if (compound.hasKey("FarmPos1") && compound.hasKey("FarmPos1"))
            this.setBounds(new StructureBoundingBox(this.getFarmPos1(), this.getFarmPos2()));
        if (compound.hasKey("working"))
            this.workstatus = compound.getInteger("workstatus");
        NBTTagList nbtTagList = compound.getTagList("Inventory", 10);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            ItemStack itemStack = new ItemStack(nbtTagList.getCompoundTagAt(i));
            if (!itemStack.isEmpty()) {
                this.inventory.addItem(itemStack);
            }
        }
        if (compound.hasKey("SimToolInv"))this.toolinv.deserializeNBT(compound.getCompoundTag("SimToolInv"));

        if (compound.hasKey("PickUpInv"))this.pickups.deserializeNBT(compound.getCompoundTag("PickUpInv"));
        System.out.println(compound.hasKey("spawn pos"));
        System.out.println("this is called!!!!");
        this.hunger = compound.getInteger("hunger");
        this.Factionid = compound.getLong("Factionid");
        if (compound.hasKey("HomeLocation"))
            this.setHomeLocation(homeLocation.deserializeNBT(compound));
        this.shouldteleport = compound.getBoolean("shouldteleport");
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
            case 5:
                return "Cattle Farmer";
            case 6:
                return "Sheep Farmer";
            case 7:
                return "Miner";
        }

        return "Oh well, this is awkward.";
    }

    public void setStructure(Template structure) {
        this.structure = structure;
    }

    public Template getStructure() {
        return this.structure;
    }

    public void setStartPos(BlockPos startPos) {
        this.startPos = startPos;
    }

    public BlockPos getStartPos() {
        return startPos;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    public EnumFacing getFacing() {
        return facing;
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

    public void setStaff(boolean staff) {
        this.dataManager.set(STAFF, staff);
    }

    public boolean getStaff() {
        return this.dataManager.get(STAFF);
    }

    public void setPlayerProfile(GameProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    public GameProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setHomeLocation(StructureBuilding homeLocation) {
        this.homeLocation = homeLocation;
    }

    public StructureBuilding getHomeLocation() {
        return homeLocation;
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
        this.setStaff(randomizeBooleanWithChance(ConfigHandler.specialSpawn));
        this.setProfession(rand.nextInt(2)); //TODO: add more professions.

        this.setLeftHanded(randomizeBooleanWithChance(10));

        if (this.getStaff()) {
            String name = SpecialNameStorage.specialNames.get(rand.nextInt(SpecialNameStorage.specialNames.size()));
            this.setCustomNameTag(name);
            this.setFemale(SpecialNameStorage.femaleIndex.contains(name));
        } else {
            this.setFemale(randomizeBoolean());
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
        }

        this.setAdditionalAITasks();
        this.writeEntityToNBT(this.getEntityData());
        return livingData;
    }

    @Override
    public void setCustomNameTag(String name) {
        if (SpecialNameStorage.specialNames.contains(name)) {
            super.setCustomNameTag(name);
            this.setStaff(true);
            this.setFemale(SpecialNameStorage.femaleIndex.contains(name));
        } else {
            super.setCustomNameTag(name);
        }
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
        if (itemEntity.getItem().getItem() instanceof ItemArmor) {
            EntityEquipmentSlot slot = EntityLiving.getSlotForItemStack(itemEntity.getItem());
            ItemStack itemstack1 = this.getItemStackFromSlot(slot);

            if (itemstack1.isEmpty()) {
                this.setItemStackToSlot(slot, itemEntity.getItem().copy());
                itemEntity.setDead();
            } else {
                if (((ItemArmor)this.getItemStackFromSlot(slot).getItem()).damageReduceAmount < ((ItemArmor) itemEntity.getItem().getItem()).damageReduceAmount) {
                    this.setItemStackToSlot(slot, itemEntity.getItem().copy());
                    itemEntity.setDead();
                    this.entityDropItem(itemstack1, 1.7f);
                }
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return super.getArmorInventoryList();
    }

    public boolean hasArmorInSlot(EntityEquipmentSlot slot) {
        return this.getItemStackFromSlot(slot) != ItemStack.EMPTY;
    }

    private boolean canSimPickupItem(Item item) {
        return true;
    }

    private boolean randomizeBoolean() {
        int dice = rand.nextInt(2);
        return dice != 0 && dice == 1;
    }

    private boolean randomizeBooleanWithChance(int i) {
        int dice = rand.nextInt(i);
        return dice == 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getSwingProgress(float partialTickTime) {
        return super.getSwingProgress(partialTickTime);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.NORTH) {
            return true;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.SOUTH){
         return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.NORTH) {
            return (T) this.toolinv;
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.SOUTH){
            return  (T) this.pickups;
        }
        return super.getCapability(capability, facing);

    }

    public void checkFood() {
        if (!this.world.isRemote) {
            int heal = 0;
            int finalHeal = 0;
            ItemStack final_stack = null;
            for (int i = 0; i < toolinv.getSlots(); i++) {
                ItemStack stack = toolinv.getStackInSlot(i);
                if (stack.getItem() instanceof ItemFood) {
                    heal = (((ItemFood) stack.getItem()).getHealAmount(stack));
                    if (finalHeal <= 0) finalHeal = heal;
                    else if (this.getFoodLevel() + finalHeal > 20) {
                        if (heal < finalHeal) {
                            finalHeal = heal;
                            final_stack = toolinv.getStackInSlot(i);
                        }
                    } else {
                        finalHeal = heal;
                        final_stack = toolinv.getStackInSlot(i);
                    }
                }
            }

            if (finalHeal != 0 && final_stack != null) {
                if (this.hunger + finalHeal > maxhunger) {
                    hunger = 20;
                    final_stack.shrink(1);
                } else {
                    hunger += finalHeal;
                    final_stack.shrink(1);
                }

                if (hunger < 5) {
                    if (finalHeal + hunger > 20) {
                        hunger = 20;
                        final_stack.shrink(1);
                    } else {
                        hunger += finalHeal;
                    }
                } else if (finalHeal + hunger <= 20) {
                    hunger += finalHeal;
                    final_stack.shrink(1);
                }
                Objects.requireNonNull(SaveSimData.get(this.world)).getfaction(this.getFactionId()).sendFactionPacket(new HungerPacket(hunger,this.getEntityId()));
            }
        }
    }

    public int getFoodLevel() {
        return hunger;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!world.isRemote){
        //heal counter. checks to heal after
        updatenotworking();
        if (getEndWork()){
            de_EquipSword(getActiveItemStack());
        }
        if (heal_counter / 20 > 4) {
            if (hunger > 15 && getHealth() < 20) {
                 heal(1.0f);
                heal_counter = 0;
            }
        }
        if (counter/20 > 60) {
            if (hunger <= 0) {
                this.attackEntityFrom(DamageSource.STARVE, 1.0f);
                counter = 0;
                hunger = 0;
            } else {
                if (hunger > 0) {
                    hunger --;
                    Objects.requireNonNull(SaveSimData.get(this.world)).getfaction(this.getFactionId()).sendFactionPacket(new HungerPacket(hunger,this.getEntityId()));
                }
            }
        }
        heal_counter++;
        counter++;
    }

    if (!world.isRemote){

            if (teleport){
        if (teleportdelay <= 0 || teleporttarget == null) {
            setTeleport(false);
            setParticlspawning(false);
            teleportdelay = 140;
            teleporttarget = null;
            setAIMoveSpeed(0.7f);
            setWorking();


        }else{
            teleportdelay--;}
        if (teleporttarget ==  null){
            teleport = false;
            particlspawning = false;
            setAIMoveSpeed(0.7f);
        }
        if (teleportdelay <= 40 && teleporttarget != null){
            setWorking();
            this.setPosition(getTeleporttarget().getX()+0.5f,getTeleporttarget().getY()+2,getTeleporttarget().getZ()+0.5f);
            teleporttarget=null;
        }
            }

            if (this.getProfession() == 0){
                if (this.workstatus != 0)workstatus = 0;
            }
        }
    }


    private void updatenotworking() {
        if (getEndWork()) {
            if (counter / 20 > 10) {
                setEndWork();
                counter = 0;
            } else {
                counter++;
            }
        } else {
            counter = 0;
        }
    }

    private void de_EquipSword(ItemStack stack){
        getHeldItemMainhand();
            if (getHeldItemMainhand().getItem() instanceof ItemSword){
                this.setHeldItem(EnumHand.MAIN_HAND,ItemStack.EMPTY);
                for (int i = 0;i<toolinv.getSlots();i++){
                    if (toolinv.getStackInSlot(i).isEmpty()){
                        toolinv.insertItem(i,stack,false);
                    }
                }
            }
        }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setFactionid(Long id){
        Factionid = id;
    }

    public Long getFactionId(){
        return Factionid;
    }

    public void setJobBlockPos(BlockPos pos){
        jobBlockPos = pos;
    }

    public BlockPos getJobBlockPos() {
        return jobBlockPos;
    }

    public void setTeleporttarget(BlockPos teleporttarget){
        this.teleporttarget = teleporttarget;
    }

    public BlockPos getTeleporttarget(){
        return teleporttarget;
    }
  
    @Override
    public boolean attackEntityAsMob(Entity p_attackEntityAsMob_1_){
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;
        if (p_attackEntityAsMob_1_ instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)p_attackEntityAsMob_1_).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }
        boolean flag = p_attackEntityAsMob_1_.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (i > 0) {
                ((EntityLivingBase)p_attackEntityAsMob_1_).knockBack(this, (float)i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);
            if (j > 0) {
                p_attackEntityAsMob_1_.setFire(j * 4);
            }
            this.getHeldItemMainhand().setItemDamage(getHeldItemMainhand().getItemDamage()-1);
            if (p_attackEntityAsMob_1_ instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)p_attackEntityAsMob_1_;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;
                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
                    if (this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, p_attackEntityAsMob_1_);
        }

        return flag;
    }

    public void setWorking(){
        this.workstatus = 2;
    }

    public boolean isWorking(){
        return workstatus == 2;
    }

    public void setTargetCow(EntityCow cow){
        this.cowtarget = cow;
    }

    public EntityCow getCowtarget() {
        return cowtarget;
    }

    public void setCowmode(FarmModes.CowMode cowmode){
        this.cowmode = cowmode;
    }

    public FarmModes.CowMode getCowmode(){
        return cowmode;
    }

    public void setEndWork(){
        this.workstatus = 3;
    }

    public boolean getEndWork(){
        return workstatus == 3;
    }

    public void setEmptychest(BlockChest chest,BlockPos pos){
        this.emptychest = chest;
        this.emptychestpos = pos;
    }

    public BlockChest getEmptychest(){
        return emptychest;
    }


    public BlockPos getEmptychestpos(){return emptychestpos;}

    public EntitySheep getSheeptarget(){ return sheeptarget;}

    public void setSheeptarget(EntitySheep target){sheeptarget = target;}

    public void setSheepMode(FarmModes.SheepMode mode){
        this.sheepmode = mode;
    }

    public FarmModes.SheepMode getSheepMode(){
        return sheepmode;
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }

    public boolean getTeleport(){
        return teleport;
    }
    public void setParticlspawning(boolean spawning){
    this.particlspawning = spawning;
    }

    public boolean isParticlspawning(){
        return this.particlspawning;
    }


    public void setShouldteleport(boolean teleport){
        this.shouldteleport = teleport;
    }

    public boolean isShouldteleport(){
        return shouldteleport;
    }

    public void setReturntoblock(boolean returnto){
        this.returntoblock = returnto;
    }

    public boolean isReturntoblock(){
        return returntoblock;
    }

    public void setNotWorking(){
        workstatus = 0;
    }

    public boolean isNotWorking(){
        return workstatus == 0;
    }
    //same as going to work
    public void setStartingWork(){
        workstatus = 1;
    }
    //same as going to work
    public boolean isStartingWork(){
        return workstatus == 1;
    }


}



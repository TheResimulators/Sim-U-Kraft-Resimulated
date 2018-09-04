package com.resimulators.simukraft.client.render;

import com.google.common.collect.Iterables;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.client.model.ModelSim;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import java.io.File;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;

/**
 * Created by fabbe on 19/01/2018 - 9:04 PM.
 */
public class RenderSim extends RenderLiving<EntitySim> {
    public RenderSim(RenderManager renderManager) {
        super(renderManager, new ModelSim(0.0f), 0.5F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    @Override
    public ModelSim getMainModel() {
        return (ModelSim)super.getMainModel();
    }

    @Override
    public void doRender(EntitySim entitySim, double x, double y, double z, float entityYaw, float partialTicks) {
        double d0 = y;
        this.bindEntityTexture(entitySim);

        if (entitySim.isSneaking()) {
            d0 = y - 0.125D;
        }

        this.setModelVisibilities(entitySim);
        GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        super.doRender(entitySim, x, d0, z, entityYaw, partialTicks);
        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
    }

    private void setModelVisibilities(EntitySim entitySim) {
        ModelSim model = new ModelSim(0.0f);
        ItemStack itemstack = entitySim.getHeldItemMainhand();
        ItemStack itemstack1 = entitySim.getHeldItemOffhand();
        model.setVisible(true, entitySim.getFemale());
        model.isSneak = entitySim.isSneaking();
        ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
        ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

        if (!itemstack.isEmpty()) {
            modelbiped$armpose = ModelBiped.ArmPose.ITEM;

            if (entitySim.getItemInUseCount() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.BLOCK) {
                    modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction == EnumAction.BOW) {
                    modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (!itemstack1.isEmpty()) {
            modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

            if (entitySim.getItemInUseCount() > 0) {
                EnumAction enumaction1 = itemstack1.getItemUseAction();

                if (enumaction1 == EnumAction.BLOCK) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                } else if (enumaction1 == EnumAction.BOW) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
                }
            }
        }

        if (entitySim.getPrimaryHand() == EnumHandSide.RIGHT) {
            model.rightArmPose = modelbiped$armpose;
            model.leftArmPose = modelbiped$armpose1;
        } else {
            model.rightArmPose = modelbiped$armpose1;
            model.leftArmPose = modelbiped$armpose;
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntitySim entitySim) {
        if (entitySim.getStaff()) {
            return getPlayerSkin(entitySim);
        } else {
            //TODO: this system is all temporary
            if (entitySim.getFemale())
                return new ResourceLocation(Reference.MOD_ID, "textures/entities/sims/human/female/" + entitySim.getVariation() + ".png");
            else if (!entitySim.getFemale())
                return new ResourceLocation(Reference.MOD_ID, "textures/entities/sims/human/male/" + entitySim.getVariation() + ".png");
            else
                return new ResourceLocation("textures/entity/steve.png");
        }
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    protected void preRenderCallback(EntitySim entitySim, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    GameProfile profile = null;
    private ResourceLocation getPlayerSkin(EntitySim entitySim) {
        ResourceLocation playerSkin = DefaultPlayerSkin.getDefaultSkinLegacy();
        GameProfile profileDirty = new GameProfile((UUID)null, entitySim.getName());
        if (entitySim.getPlayerProfile() == null) {
            this.profile = updateGameprofile(profileDirty);
            entitySim.setPlayerProfile(this.profile);
        } else
            this.profile = entitySim.getPlayerProfile();
        if (this.profile != null) {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(this.profile);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
                playerSkin = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            else {
                UUID uuid = EntityPlayer.getUUID(this.profile);
                playerSkin = DefaultPlayerSkin.getDefaultSkin(uuid);
            }
        }
        return playerSkin;
    }

    public static void initSkinService() {
        Proxy proxy = Minecraft.getMinecraft().getProxy();
        AuthenticationService authenticationservice = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        MinecraftSessionService minecraftsessionservice = authenticationservice.createMinecraftSessionService();
        GameProfileRepository gameprofilerepository = authenticationservice.createProfileRepository();
        PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(Minecraft.getMinecraft().gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
        TileEntitySkull.setProfileCache(playerprofilecache);
        TileEntitySkull.setSessionService(minecraftsessionservice);
        PlayerProfileCache.setOnlineMode(false);
        RenderSim.setProfileCache(playerprofilecache);
        RenderSim.setSessionService(minecraftsessionservice);
    }

    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;

    public static void setProfileCache(PlayerProfileCache profileCacheIn) {
        profileCache = profileCacheIn;
    }

    public static void setSessionService(MinecraftSessionService sessionServiceIn) {
        sessionService = sessionServiceIn;
    }

    public static GameProfile updateGameprofile(GameProfile input) {
        if (input != null && !StringUtils.isNullOrEmpty(input.getName())) {
            if (input.isComplete() && input.getProperties().containsKey("textures")) {
                return input;
            } else if (profileCache != null && sessionService != null) {
                GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());

                if (gameprofile == null) {
                    return input;
                } else {
                    Property property = (Property) Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object) null);

                    if (property == null) {
                        gameprofile = sessionService.fillProfileProperties(gameprofile, true);
                    }

                    return gameprofile;
                }
            } else {
                return input;
            }
        } else {
            return input;
        }
    }
}

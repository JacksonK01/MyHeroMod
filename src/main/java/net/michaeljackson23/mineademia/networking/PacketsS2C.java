package net.michaeljackson23.mineademia.networking;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.abilitysystem.networking.AbilityUserPacketS2C;
import net.michaeljackson23.mineademia.animations.Animations;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.michaeljackson23.mineademia.client.gui.quirktablet.MockQuirkTabletGui;
import net.michaeljackson23.mineademia.client.gui.quirktablet.QuirkTabletGui;
import net.michaeljackson23.mineademia.client.gui.vestige.VestigeGUI;
import net.michaeljackson23.mineademia.mixin.accessors.EntityAccessors;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataAccessors;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket;
import net.michaeljackson23.mineademia.util.LivingEntityMixinAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PacketsS2C {
    public static void quirkDataSync(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buf, PacketSender ignore1) {
        if(!(client.player instanceof QuirkDataAccessors quirkPlayer)) {
            return;
        }

        quirkPlayer.myHeroMod$setQuirkData(QuirkDataPacket.decode(buf));
    }
    //To sync player's QuirkData with every player
    public static void quirkDataSyncProxy(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buf, PacketSender ignore1) {
        if(client.world != null) {
            QuirkData quirkData = QuirkDataPacket.decode(buf);
            PlayerEntity player = client.world.getPlayerByUuid(buf.readUuid());
            if(player instanceof QuirkDataAccessors quirkPlayer) {
                quirkPlayer.myHeroMod$setQuirkData(quirkData);
            }
        }
    }
    public static void quirkTablet(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        client.execute(() -> {
            QuirkTabletGui quirkTabletGui = new QuirkTabletGui(Text.literal("Quirk Tablet"));
            quirkTabletGui.init(client, 50, 50);
            quirkTabletGui.shouldPause();
            client.setScreenAndRender(quirkTabletGui);
        });
    }
    public static void mockQuirkTablet(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        client.execute(() -> {
            MockQuirkTabletGui quirkTabletGui = new MockQuirkTabletGui(Text.literal("Mock Quirk Tablet"));
            quirkTabletGui.init(client, 50, 50);
            quirkTabletGui.shouldPause();
            client.setScreenAndRender(quirkTabletGui);
        });
    }
    public static void animationProxy(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buf, PacketSender ignore1) {
        client.execute(() -> {
            UUID playerToAnimate = buf.readUuid();
            String animationName = buf.readString();
            Identifier id = new Identifier(Mineademia.MOD_ID, animationName);
            AbstractClientPlayerEntity player = null;
            if (client.world != null) {
                player = (AbstractClientPlayerEntity) client.world.getPlayerByUuid(playerToAnimate);
            }
            if (player != null) {
                AnimationStack animationStack = PlayerAnimationAccess.getPlayerAnimLayer(player);
                for(int i = 0; animationStack.isActive(); i++) {
                    animationStack.removeLayer(i);
                }
                if(!animationName.equals("reset")) {
                    KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(id);
                    if(keyframeAnimation != null) {
                        animationStack.addAnimLayer(0, new KeyframeAnimationPlayer(keyframeAnimation)
                                .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                                .setFirstPersonConfiguration(new FirstPersonConfiguration()
                                .setShowRightArm(true)
                                .setShowLeftArm(true)
                                .setShowRightItem(false)
                                .setShowLeftItem(false)));
                    }
                }
            }
        });
    }
    public static void animationProxyNoAPI(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buf, PacketSender ignore1) {
        client.execute(() -> {
            int id = buf.readInt();
            String animationName = buf.readString();

            assert client.world != null;
            Entity entity = client.world.getEntityById(id);

            if(!(entity instanceof LivingEntity livingEntity)) {
                return;
            }

            LivingEntityMixinAccessor animatedLivingEntity = (LivingEntityMixinAccessor) livingEntity;

            animatedLivingEntity.setAnimationState(new AnimationState());

            if(animationName.equals("reset")) {
                animatedLivingEntity.getAnimationState().setRunning(false, livingEntity.age);
                return;
            }

            animatedLivingEntity.setAnimationData(Animations.getAnimationRegistry().get(animationName));
            animatedLivingEntity.getAnimationState().setRunning(true, livingEntity.age);
        });
    }
    public static void setYaw(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buf, PacketSender ignore1) {
        if(client.player != null) {
            client.player.setYaw(buf.readFloat());
        }
    }
    public static void forceThirdPersonBack(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }
    public static void forceThirdPersonFront(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
    }
    public static void forceFirstPerson(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        client.options.setPerspective(Perspective.FIRST_PERSON);
    }

    public static void openVestigeGui(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        client.execute(() -> {
            VestigeGUI vestigeGUI = new VestigeGUI(Text.literal("Quirk Tablet"));
            vestigeGUI.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
            vestigeGUI.shouldPause();
            client.setScreenAndRender(vestigeGUI);
        });
    }
    public static void setEntitiesGlow(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buf, PacketSender ignore1) {
        ClientWorld world = client.world;

        if (world != null) {
            int[] ids = buf.readIntArray();
            boolean isGlowing = buf.readBoolean();

            if (ids != null) {
                for (int id : ids) {
                    Entity entity = world.getEntityById(id);

                    if (entity != null)
                        ((EntityAccessors) entity).invokeSetFlag(6, isGlowing);
                        //EntityReflection.trySetFlag(entity, 6, isGlowing);
                }
            }
        }
    }
    public static void windFlyDescentVelocity(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf ignore1, PacketSender ignore2) {
        ClientPlayerEntity player = client.player;
        if(player != null) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, velocity.y/2, velocity.z);
        }
    }
    public static void comboDamage(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, @NotNull PacketByteBuf buf, PacketSender ignore1) {
        UUID attackerUuid = buf.readUuid();
        float amount = buf.readFloat();
        ClientPlayerEntity target = client.player;
        if(target != null && target.getWorld() != null) {
            PlayerEntity attacker = target.getWorld().getPlayerByUuid(attackerUuid);
            target.damage(target.getDamageSources().playerAttack(attacker), amount);
        }
    }

    public static void playerAbilityUser(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, PacketByteBuf buffer, PacketSender ignore1) {
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        AbilityUserPacketS2C user = AbilityUserPacketS2C.decode(buffer);

        ClientCache.self = user;
        ClientCache.users.put(player.getUuid(), user);
    }

    public static void zoomPacket(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, @NotNull PacketByteBuf buffer, PacketSender ignore1) {
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        float zoomLevel = buffer.readFloat();
        ClientCache.setZoomLevel(player.getUuid(), zoomLevel);

        client.options.smoothCameraEnabled = zoomLevel != 1 && buffer.readBoolean();
    }

    public static void setVelocity(@NotNull MinecraftClient client, ClientPlayNetworkHandler ignore, @NotNull PacketByteBuf buffer, PacketSender ignore1) {
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        Vec3d playerVelocity = player.getVelocity();
        Vec3d velocity = buffer.readVec3d();

        Vec3d actualVelocity = new Vec3d(velocity.x == Double.MAX_VALUE ? playerVelocity.x : velocity.x, velocity.y == Double.MAX_VALUE ? playerVelocity.y : velocity.y, velocity.z == Double.MAX_VALUE ? playerVelocity.z : velocity.z);
        player.setVelocity(actualVelocity);
    }

}

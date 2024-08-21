package net.michaeljackson23.mineademia.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.impl.abilityyser.PlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IPlayerAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.AbilitySets;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.DodgeAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.PickVestigeAbility;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.michaeljackson23.mineademia.util.AnimationProxy;
import net.michaeljackson23.mineademia.util.PlayerDataAccessor;
import net.michaeljackson23.mineademia.util.QuirkAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class PacketsC2S {

    public static void blocking(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        AnimationProxy.sendAnimationToClients((LivingEntity) player, "blocking");
    }

    public static void abilityOne(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null) {
            boolean isKeyDown = buf.readBoolean();
            user.executeBoundAbility(Networking.C2S_ABILITY_ONE, isKeyDown);
        }
        // activateAbility(player, buf, 0);
    }

    public static void abilityTwo(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null) {
            boolean isKeyDown = buf.readBoolean();
            user.executeBoundAbility(Networking.C2S_ABILITY_TWO, isKeyDown);
        }
        // activateAbility(player, buf, 1);
    }

    public static void abilityThree(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null) {
            boolean isKeyDown = buf.readBoolean();
            user.executeBoundAbility(Networking.C2S_ABILITY_THREE, isKeyDown);
        }
        // activateAbility(player, buf, 2);
    }

    public static void abilityFour(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null) {
            boolean isKeyDown = buf.readBoolean();
            user.executeBoundAbility(Networking.C2S_ABILITY_FOUR, isKeyDown);
        }
        // activateAbility(player, buf, 3);
    }

    public static void abilityFive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null) {
            boolean isKeyDown = buf.readBoolean();
            user.executeBoundAbility(Networking.C2S_ABILITY_FIVE, isKeyDown);
        }
        // activateAbility(player, buf, 4);
    }

    public static void abilityDodge(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null)
            user.execute(DodgeAbility.class, true);
    }

    // TODO REMOVE!!!
    public static void abilityTest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        boolean isKeyDown = buf.readBoolean();

        PlayerAbilityUser user = (PlayerAbilityUser) AbilityManager.getPlayerUser(player);
        if (user != null) {
            Class<? extends IAbility> type = user.getCurrentAbility();
            if (user.getAbilities().get(type) instanceof IActiveAbility ability) {
                user.execute(ability.getClass(), isKeyDown);
            }
        }
    }

    // TODO REMOVE!!!
    public static void abilityTestSwap(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
         PlayerAbilityUser user = (PlayerAbilityUser) AbilityManager.getPlayerUser(player);
        if (user != null)
            user.incrementIndex();
    }

    public static void kickCombo(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(player.getServerWorld().getEntityById(buf.readInt()) instanceof LivingEntity livingEntity) {
            ((PlayerDataAccessor) player).myHeroMod$getPlayerData().getComboManager().notifyKick(player, livingEntity);
        }
    }

    public static void aerialCombo(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if(player.getServerWorld().getEntityById(buf.readInt()) instanceof LivingEntity livingEntity) {
            ((PlayerDataAccessor) player).myHeroMod$getPlayerData().getComboManager().notifyAerial(player, livingEntity);
        }
    }

    public static void openQuirkTabletGUI(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String quirk = buf.readString();
        player.sendMessage(Text.literal("Changed quirk to " + quirk));
        ((QuirkAccessor) player).myHeroMod$setQuirk(QuirkInitialize.setQuirkWithString(quirk));
    }

    @SuppressWarnings("unchecked")
    public static void mockQuirkTabletQuirkChange(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String abilityName = buf.readString();

        IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
        if (user != null)
            user.setAbilities(AbilitySets.GENERAL, AbilitySets.getAbilitySetOrEmpty(abilityName));
    }

//    private static void activateAbility(ServerPlayerEntity player, PacketByteBuf buf, int i) {
//        Quirk quirk = StateSaverAndLoader.getPlayerState(player).getQuirk();
//        boolean isHeld = buf.readBoolean();
//        if (isHeld) {
//            if(quirk.getActiveAbility() == null) {
//                quirk.setActiveAbility(quirk.getAbilities()[i]);
//            }
//            if(quirk.getActiveAbility() == quirk.getAbilities()[i]) {
//                AbilityBase activeAbility = quirk.getActiveAbility();
//                activeAbility.setAmountOfTimesActivated(activeAbility.getAmountOfTimesActivated() + 1);
//            }
//        }
//        if(quirk.getActiveAbility() != null) {
//            AbilityBase activeAbility = quirk.getActiveAbility();
//            activeAbility.setIsCurrentlyHeld(isHeld);
//        }
//    }

    public static void vestigeAbility(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, @NotNull PacketByteBuf buf, PacketSender responseSender) {
        String abilityString = buf.readString();

        IAbilityUser user = AbilityManager.getUser(player);
        if(user == null)
            return;

        PickVestigeAbility ability = user.getAbility(PickVestigeAbility.class);
        if(ability != null)
            ability.setCurrentAbility(abilityString);
    }

}

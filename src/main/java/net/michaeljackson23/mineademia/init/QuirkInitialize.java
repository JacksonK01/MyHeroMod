package net.michaeljackson23.mineademia.init;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.Empty;
import net.michaeljackson23.mineademia.abilities.explosion.ExplosionDash;
import net.michaeljackson23.mineademia.abilities.ofa.AirForce;
import net.michaeljackson23.mineademia.abilities.ofa.Cowling;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Random;

import static net.michaeljackson23.mineademia.networking.Server2Client.INITIAL_SYNC;

public class QuirkInitialize {
    private static List<String> allQuirks = List.of("One For All", "Explosion", "Half-Cold Half-Hot", "Whirlwind", "Electrification");
    private static final Random rand = new Random();

    public static void InitializeEvent() {
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            handler.getPlayer().sendMessage(Text.literal("Player join world event is working!!"));
            PlayerData playerData = StateSaverAndLoader.getPlayerState(handler.getPlayer());
            PacketByteBuf data = PacketByteBufs.create();

            if(playerData.getQuirk().equals("empty")) {
                quirkSetup(playerData);
            }

            data.writeString(playerData.getQuirk());
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
            });
        }));
    }

    public static AbilityBase[] abilitySetup(String quirk) {
        AbilityBase[] abilities = new AbilityBase[5];

        if(quirk.equals("One For All")) {
            abilities[0] = AirForce.getInstance();
//            abilities[1] = new Blackwhip();
            abilities[4] = Cowling.getInstance();
        } else if(quirk.equals("Explosion")) {
            abilities[2] = ExplosionDash.getInstance();
        }

        for (int i = 0; i < abilities.length; i++) {
            if(abilities[i] == null) {
                abilities[i] = Empty.getInstance();
            }
        }
        return abilities;
    }

    public static void quirkSetup(PlayerData playerData) {
        int randomInt = rand.nextInt(allQuirks.size());
        playerData.setQuirk(allQuirks.get(randomInt));
        playerData.setAbilities(abilitySetup(playerData.getQuirk()));
    }

    public static void setQuirk(PlayerData playerData, String quirk) {
        playerData.setQuirk(quirk);
        playerData.setAbilities(abilitySetup(quirk));
    }
}


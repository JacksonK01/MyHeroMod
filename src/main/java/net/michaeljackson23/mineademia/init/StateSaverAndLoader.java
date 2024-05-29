package net.michaeljackson23.mineademia.init;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class StateSaverAndLoader extends PersistentState {
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();

            playerNbt.putString("playerQuirk", playerData.getQuirk().getName());

            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);

        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        NbtCompound playersNbt = tag.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();

            String quirk = playersNbt.getCompound(key).getString("playerQuirk");
            QuirkInitialize.setQuirkWithString(playerData, quirk);

            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });
        return state;
    }

    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new,
            StateSaverAndLoader::createFromNbt,
            null
    );

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, Mineademia.Mod_id);

        state.markDirty();

        return state;
    }


    public static PlayerData getPlayerState(ServerPlayerEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());


        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());

        return playerState;
    }

}


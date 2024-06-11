package net.michaeljackson23.mineademia.savedata;

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 *     This is how data is stored for the player. All of which is stored server side.
 * </p>
 * <p>
 *     After reading this, check out {@link net.michaeljackson23.mineademia.savedata.PlayerData} and {@link net.michaeljackson23.mineademia.quirk.ServerQuirkTicks}
 * </p>
 */
public class StateSaverAndLoader extends PersistentState {
    /**
     * This is only used for building the string for reading saved data.
     * <p>
     *     This is not important.
     * </p>
     */
    private static final String NBT = Mineademia.MOD_ID + "_players";
    /**
     * <p>
     *     This is important. This uses a hashmap to store EVERY player's data. The Key
     *     value is UUIDs which are unique to every single player, and the value associated with the
     *     key is the {@link net.michaeljackson23.mineademia.savedata.PlayerData} object which
     *     stores all the important information this mod will use. Every player will have their own PlayerData
     *     Object. ALL OF THIS IS STORED SERVERSIDE.
     * </p>
     */
    public HashMap<UUID, PlayerData> players = new HashMap<>();
    private static final Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new,
            StateSaverAndLoader::createFromNbt,
            null
    );
    /**
     * <p>
     *     Saves data to computer's disk
     * </p>
     * <p>
     *     Used by = I actually don't know the exact location. Just know when the game closes, this method gets called
     * </p>
     */
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerData.writeNbt(playerNbt);
            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put(NBT, playersNbt);

        return nbt;
    }
    /**
     * <p>
     *     When the game is loaded, this method is used
     * </p>
     * <p>
     *     Used by = Don't know, just know it's used when the world is being loaded
     * </p>
     */
    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        NbtCompound playersNbt = tag.getCompound(NBT);
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            playerData.readNbt(playersNbt.getCompound(key));
            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });
        return state;
    }
    /**
     * <p>
     *     This is not important.
     * </p>
     * <p>
     *     Used by = {@link #getPlayerState(ServerPlayerEntity)}
     * </p>
     */
    private static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, Mineademia.MOD_ID);

        //Marking data dirty means the data was modified since the data was loaded in, which will be the case 99.9999% of the time.
        state.markDirty();

        return state;
    }
    /**
     * <p>
     *     This is important. It's not commonly used but all it does is get
     *     the PlayerData that is associated with the player that is passed through the
     *     parameters.
     * </p>
     * <p>
     *     Used by = {@link QuirkInitialize#InitializeEvent()},
     *     {@link net.michaeljackson23.mineademia.networking.ServerPackets#activateAbility(ServerPlayerEntity, PacketByteBuf, int)},
     *     {@link net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket#send(ServerPlayerEntity)},
     *     {@link net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataPacket#sendProxy(ServerPlayerEntity)},
     *     {@link OnPlayerRespawn#register()}
     * </p>
     */
    public static PlayerData getPlayerState(ServerPlayerEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());

        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());

        return playerState;
    }

}


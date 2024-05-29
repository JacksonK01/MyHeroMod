package net.michaeljackson23.mineademia.sound;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomSounds {
    public static final Identifier COWLING_START_ID = new Identifier(Mineademia.Mod_id, "cowling_start");
    public static SoundEvent COWLING_START_EVENT = SoundEvent.of(COWLING_START_ID);

    public static final Identifier COWLING_END_ID = new Identifier(Mineademia.Mod_id, "cowling_end");
    public static SoundEvent COWLING_END_EVENT = SoundEvent.of(COWLING_END_ID);

    public static final Identifier COWLING_REPEAT_ID = new Identifier(Mineademia.Mod_id, "cowling_repeat");
    public static SoundEvent COWLING_REPEAT_EVENT = SoundEvent.of(COWLING_REPEAT_ID);

    public static final Identifier MHA_FIRE_ID = new Identifier(Mineademia.Mod_id, "fire_sound_effect");
    public static SoundEvent MHA_FIRE_EVENT = SoundEvent.of(MHA_FIRE_ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, COWLING_START_ID, COWLING_START_EVENT);
        Registry.register(Registries.SOUND_EVENT, COWLING_END_ID, COWLING_END_EVENT);
        Registry.register(Registries.SOUND_EVENT, COWLING_REPEAT_ID, COWLING_REPEAT_EVENT);
        Registry.register(Registries.SOUND_EVENT, MHA_FIRE_ID, MHA_FIRE_EVENT);
    }
}

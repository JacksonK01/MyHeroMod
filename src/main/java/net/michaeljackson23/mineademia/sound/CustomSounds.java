package net.michaeljackson23.mineademia.sound;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomSounds {
    public static final Identifier COWLING_START_ID = new Identifier(Mineademia.MOD_ID, "cowling_start");
    public static SoundEvent COWLING_START_EVENT = SoundEvent.of(COWLING_START_ID);

    public static final Identifier COWLING_END_ID = new Identifier(Mineademia.MOD_ID, "cowling_end");
    public static SoundEvent COWLING_END_EVENT = SoundEvent.of(COWLING_END_ID);

    public static final Identifier COWLING_REPEAT_ID = new Identifier(Mineademia.MOD_ID, "cowling_repeat");
    public static SoundEvent COWLING_REPEAT_EVENT = SoundEvent.of(COWLING_REPEAT_ID);

    public static final Identifier MHA_FIRE_ID = new Identifier(Mineademia.MOD_ID, "fire_sound_effect");
    public static SoundEvent MHA_FIRE_EVENT = SoundEvent.of(MHA_FIRE_ID);

    public static final Identifier GRIDDY_ID = new Identifier(Mineademia.MOD_ID, "griddy");
    public static SoundEvent GRIDDY_EVENT = SoundEvent.of(GRIDDY_ID);

    public static final Identifier SLIDE_ID = new Identifier(Mineademia.MOD_ID, "slide");
    public static SoundEvent SLIDE_EVENT = SoundEvent.of(SLIDE_ID);

    public static final Identifier PHYSICAL_DAMAGE_ID = new Identifier(Mineademia.MOD_ID, "punch_impact");
    public static SoundEvent PHYSICAL_DAMAGE_EVENT = SoundEvent.of(PHYSICAL_DAMAGE_ID);

    public static final Identifier MHA_EXPLOSION_ID = new Identifier(Mineademia.MOD_ID, "explosion");
    public static SoundEvent MHA_EXPLOSION_EVENT = SoundEvent.of(MHA_EXPLOSION_ID);

    public static final Identifier OFA_CHARGE_ID = new Identifier(Mineademia.MOD_ID, "ofa_charge_up");
    public static SoundEvent OFA_CHARGE_EVENT = SoundEvent.of(OFA_CHARGE_ID);

    public static final Identifier OFA_RELEASE_ID = new Identifier(Mineademia.MOD_ID, "ofa_charge_release");
    public static SoundEvent OFA_RELEASE_EVENT = SoundEvent.of(OFA_RELEASE_ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, COWLING_START_ID, COWLING_START_EVENT);
        Registry.register(Registries.SOUND_EVENT, COWLING_END_ID, COWLING_END_EVENT);
        Registry.register(Registries.SOUND_EVENT, COWLING_REPEAT_ID, COWLING_REPEAT_EVENT);
        Registry.register(Registries.SOUND_EVENT, MHA_FIRE_ID, MHA_FIRE_EVENT);
        Registry.register(Registries.SOUND_EVENT, GRIDDY_ID, GRIDDY_EVENT);
        Registry.register(Registries.SOUND_EVENT, SLIDE_ID, SLIDE_EVENT);
        Registry.register(Registries.SOUND_EVENT, PHYSICAL_DAMAGE_ID, PHYSICAL_DAMAGE_EVENT);
        Registry.register(Registries.SOUND_EVENT, MHA_EXPLOSION_ID, MHA_EXPLOSION_EVENT);
        Registry.register(Registries.SOUND_EVENT, OFA_CHARGE_ID, OFA_CHARGE_EVENT);
        Registry.register(Registries.SOUND_EVENT, OFA_RELEASE_ID, OFA_RELEASE_EVENT);
    }
}

package net.michaeljackson23.mineademia.sound;

import net.michaeljackson23.mineademia.Mineademia;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static SoundEvent COWLING_START_EVENT = registerSound("cowling_start");
    public static SoundEvent COWLING_END = registerSound("cowling_end");
    public static SoundEvent COWLING_REPEAT = registerSound("cowling_repeat");

    public static SoundEvent MHA_FIRE = registerSound("fire_sound_effect");

    public static SoundEvent GRIDDY = registerSound("griddy");

    public static SoundEvent SLIDE = registerSound("slide");

    public static SoundEvent PUNCH_IMPACT = registerSound("punch_impact");

    public static SoundEvent MHA_EXPLOSION = registerSound("explosion");

    public static SoundEvent OFA_CHARGE = registerSound("ofa_charge_up");
    public static SoundEvent OFA_RELEASE = registerSound("ofa_charge_release");

    public static SoundEvent LEG_MOVEMENT = registerSound("leg_movement");

    public static SoundEvent DANGER_SENSE = registerSound("danger_sense");

    public static final SoundEvent DISTANT_EXPLOSION_1 = registerSound("distant_explosion_1");
    public static final SoundEvent DISTANT_EXPLOSION_2 = registerSound("distant_explosion_2");
    public static final SoundEvent DEEP_EXPLOSION = registerSound("deep_explosion");
    public static final SoundEvent SMALL_EXPLOSION = registerSound("small_explosion");

    public static final SoundEvent SMALL_TORNADO_START = registerSound("small_tornado_start");
    public static final SoundEvent SMALL_TORNADO_LOOP = registerSound("small_tornado_loop");
    public static final SoundEvent SMALL_TORNADO_END = registerSound("small_tornado_end");

    public static final SoundEvent BIG_TORNADO_START = registerSound("big_tornado_start");
    public static final SoundEvent BIG_TORNADO_LOOP = registerSound("big_tornado_loop");
    public static final SoundEvent BIG_TORNADO_END = registerSound("big_tornado_end");

    public static final SoundEvent QUIRK_RIFLE_TEAR_HAIR = registerSound("quirk_rifle_tear_hair");
    public static final SoundEvent QUIRK_RIFLE_FROM_BELT = registerSound("quirk_rifle_belt");
    // public static final SoundEvent QUIRK_RIFLE_TO_BELT = registerSound("quirk_rifle_belt");
    public static final SoundEvent QUIRK_RIFLE_RELOAD = registerSound("quirk_rifle_reload");
    public static final SoundEvent QUIRK_RIFLE_SHOOT = registerSound("quirk_rifle_shoot");
    public static final SoundEvent QUIRK_RIFLE_SHOOT_EMPTY = registerSound("quirk_rifle_shoot_empty");
    public static final SoundEvent QUIRK_RIFLE_ZOOM = registerSound("quirk_rifle_zoom");

    public static final SoundEvent QUIRK_THEWORLD_STOP_TIME_START = registerSound("quirk_theworld_stop_time_start");
    public static final SoundEvent QUIRK_THEWORLD_STOP_TIME_STOP = registerSound("quirk_theworld_stop_time_end");

    public static final SoundEvent QUIRK_ELECTRICITY_1 = registerSound("electricity_1");
    public static final SoundEvent QUIRK_ELECTRICITY_2 = registerSound("electricity_2");
    public static final SoundEvent QUIRK_ELECTRICITY_3 = registerSound("electricity_3");
    public static final SoundEvent QUIRK_ELECTRICITY_4 = registerSound("electricity_4");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = new Identifier(Mineademia.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void register() {
        Mineademia.LOGGER.info("Registering sounds for " + Mineademia.MOD_ID);
    }

}

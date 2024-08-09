package net.michaeljackson23.mineademia;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityEvents;
import net.michaeljackson23.mineademia.armor.ArmorRegister;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.combo.ComboEvent;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.michaeljackson23.mineademia.items.ItemRegister;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.ServerQuirkTicks;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.particles.ModParticles;
import net.michaeljackson23.mineademia.quirk.events.BeforeDamageEvent;
import net.michaeljackson23.mineademia.savedata.OnPlayerRespawn;
import net.michaeljackson23.mineademia.sound.ModSounds;
import net.michaeljackson23.mineademia.statuseffects.StatusEffectsRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**<p>
 * This mod is for Minecraft 1.20.4
 * </p>
 * Additional credit:
 * <ul>
 *   <li>Shroom - Helped develop the following:</li>
 *   <ul>
 *     <li>Explosion Dash</li>
 *     <li>Blackwhip</li>
 *     <li>Windblade projectile</li>
 *   </ul>
 * </ul>
 *
 * <ul>
 *   <li>Cookiebug - Has drawn all the assets for the mod</li>
 * </ul>
 * <p>
 * For understand this mod, start looking in {@link QuirkInitialize}.
 * For anything that might still be confusing, I reckon reading the Fabric wiki. <a href="https://fabricmc.net/wiki/start">Click Here</a>
 * </p>
 */

public class Mineademia implements ModInitializer {
	public static final String MOD_ID = "mineademia";
    public static final Logger LOGGER = LoggerFactory.getLogger("mineademia");

	//Registers elements for server + client. Stuff like items, blocks and any logic you want serverside goes here.
	//TODO hook into before player taking damage
	//TODO hook into before the player respawns
	@Override
	public void onInitialize() {
		Networking.registerServer();
		QuirkInitialize.InitializeEvent();
		ServerQuirkTicks.serverTickRegister();
		EntityRegister.register();
		BlockRegister.register();
		ArmorRegister.register();
		ItemRegister.register();
		ModParticles.register();
		ModSounds.register();
		OnPlayerRespawn.register();
		BeforeDamageEvent.register();
		ComboEvent.register();
		StatusEffectsRegister.register();

		registerEvents();
	}

	private void registerEvents() {
		ServerTickEvents.START_SERVER_TICK.register(AbilityManager::onServerTick);
		ServerPlayConnectionEvents.JOIN.register(AbilityManager::onPlayerJoin);
		ServerLivingEntityEvents.ALLOW_DAMAGE.register(AbilityEvents::onEntityDamage);
		ServerLivingEntityEvents.AFTER_DEATH.register(AbilityEvents::onEntityDeath);
	}

}
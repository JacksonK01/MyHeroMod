package net.michaeljackson23.mineademia;

import net.fabricmc.api.ModInitializer;

import net.michaeljackson23.mineademia.armor.ArmorRegister;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.michaeljackson23.mineademia.items.ItemRegister;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.ServerQuirkTicks;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.quirk.events.BeforeDamageEvent;
import net.michaeljackson23.mineademia.savedata.OnPlayerRespawn;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
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
		ParticleRegister.register();
		CustomSounds.register();
		OnPlayerRespawn.register();
		BeforeDamageEvent.register();
	}
}
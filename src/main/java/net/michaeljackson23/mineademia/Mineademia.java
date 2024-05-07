package net.michaeljackson23.mineademia;

import net.fabricmc.api.ModInitializer;

import net.michaeljackson23.mineademia.init.QuirkInitialize;
import net.michaeljackson23.mineademia.items.ItemRegister;
import net.michaeljackson23.mineademia.networking.Client2Server;
import net.michaeljackson23.mineademia.abilities.abilityinit.AbilitiesTicks;
import net.michaeljackson23.mineademia.abilities.abilityinit.AbilityMap;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.test.KillOnUse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mineademia implements ModInitializer {
	public static final String Mod_id = "mineademia";
    public static final Logger LOGGER = LoggerFactory.getLogger("mineademia");


	@Override
	public void onInitialize() {
		new Keybinds();
		new KillOnUse();
		Client2Server.registerClientToServerPackets();
		QuirkInitialize.InitializeEvent();
		AbilitiesTicks.AbilitiesTickEvent();
		EntityRegister.register();
		ItemRegister.register();
		ParticleRegister.register();
	}
}
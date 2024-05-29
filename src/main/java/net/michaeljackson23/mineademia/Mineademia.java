package net.michaeljackson23.mineademia;

import net.fabricmc.api.ModInitializer;

import net.michaeljackson23.mineademia.armor.ArmorRegister;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.quirk.QuirkInitialize;
import net.michaeljackson23.mineademia.items.ItemRegister;
import net.michaeljackson23.mineademia.networking.Client2Server;
import net.michaeljackson23.mineademia.quirk.ServerTicks;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.keybinds.Keybinds;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.michaeljackson23.mineademia.sound.CustomSounds;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mineademia implements ModInitializer {
	public static final String Mod_id = "mineademia";
    public static final Logger LOGGER = LoggerFactory.getLogger("mineademia");

	//Registers elements for server + client. Stuff like items, blocks and any logic you want serverside goes here.
	@Override
	public void onInitialize() {
		Keybinds.keysRegister();
		Client2Server.registerClientToServerPackets();
		QuirkInitialize.InitializeEvent();
		ServerTicks.AbilitiesTickEvent();
		EntityRegister.register();
		BlockRegister.register();
		ArmorRegister.register();
		ItemRegister.register();
		ParticleRegister.register();
		CustomSounds.register();
	}
}
package net.michaeljackson23.mineademia.abilities.ofa;

import net.michaeljackson23.mineademia.abilities.abilityinit.IAbilityHandler;
import net.michaeljackson23.mineademia.init.PlayerData;
import net.michaeljackson23.mineademia.particles.ParticleRegister;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Cowling implements IAbilityHandler {

    @Override
    public void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
        //1 = 10%
        //2 = 20%
        //etc etc

        switch (playerData.quirkAbilityTimers[slot]) {
            case 1:
                CowlingFunctionality(1, 0, player);
                break;
            case 2:
                CowlingFunctionality(2, 0, player);
                break;
            case 3:
                CowlingFunctionality(3, 0, player);
                break;
            case 4:
                CowlingFunctionality(4, 0, player);
                break;
            case 5:
                CowlingFunctionality(5, 0, player);
                break;
            case 6:
                CowlingFunctionality(6, 0, player);
                break;
            case 7:
                CowlingFunctionality(7, 0, player);
                break;
            case 8:
                CowlingFunctionality(8, 0, player);
                break;
            case 9:
                CowlingFunctionality(9, 0, player);
                break;
            case 10:
                CowlingFunctionality(10, 0, player);
                break;
            case 11:
                playerData.quirkAbilityTimers[slot] = 0;
                player.sendMessage(Text.literal("Cowling De-Activated"));
        }

    }

    private void CowlingFunctionality(int particleAmount, int power, ServerPlayerEntity player) {
        player.getServerWorld().spawnParticles(ParticleRegister.COWLING_PARTICLES, player.getX(), player.getY() + 1, player.getZ(),
                particleAmount, 0.3f, 0.5f, 0.3f, 0);
        player.sendMessage(Text.literal("Cowling "+ particleAmount + "0%"));
    }
}

//package net.michaeljackson23.mineademia.abilities.ofa;
//
//import net.michaeljackson23.mineademia.abilities.abilityinit.IAbilityHandler;
//import net.michaeljackson23.mineademia.init.PlayerData;
//import net.michaeljackson23.mineademia.util.RaycastToEntity;
//import net.minecraft.entity.projectile.ProjectileUtil;
//import net.minecraft.particle.ParticleTypes;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.text.Text;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.hit.EntityHitResult;
//import net.minecraft.util.hit.HitResult;
//import net.minecraft.util.math.BlockPos;
//
//import javax.swing.text.html.parser.Entity;
//
//
//public class Blackwhip implements IAbilityHandler {
//    public static final String title = "Blackwhip";
//    public static final String description = "Blackwhip grants the user the ability to produce energy tendrils from any part of their body and command them at will";
//
//    @Override
//    public void activate(ServerPlayerEntity player, PlayerData playerData, MinecraftServer server, int slot) {
//
//        if (playerData.quirkAbilityTimers[slot] == 1) {
//            EntityHitResult entity = RaycastToEntity.raycast(player);
//            if(entity != null) {
//                player.sendMessage(Text.literal("Pos before teleport X: " + (int) player.getX() + " Y: " + (int) player.getX() + " Z: " + (int) player.getZ()));
//                player.teleport(player.getServerWorld(), entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), player.getYaw(), player.getPitch());
//                player.sendMessage(Text.literal("Pos after teleport X: " + (int) player.getX() + " Y: " + (int) player.getX() + " Z: " + (int) player.getZ()));
//            }
//            playerData.quirkAbilityTimers[slot]++;
//        } else {
//            playerData.quirkAbilityTimers[slot] = 0;
//            player.sendMessage(Text.literal("Ability End"));
//        }
////        if (playerData.quirkAbilityTimers[slot] == 1) {
////            HitResult result = player.raycast(21, 1.0f, false);
////            if (result.getPos().isInRange(player.getPos(), 21) && !player.getWorld().isAir(((BlockHitResult) result).getBlockPos())) {
////                player.sendMessage(Text.literal("Timer: " + playerData.quirkAbilityTimers[slot]));
////
////                playerData.storedXBlackwhip = ((BlockHitResult) result).getBlockPos().getX();
////                playerData.storedYBlackwhip = ((BlockHitResult) result).getBlockPos().getY();
////                playerData.storedZBlackwhip = ((BlockHitResult) result).getBlockPos().getZ();
////
////            } else {
////                playerData.quirkAbilityTimers[slot] = 0;
////            }
////        }
////
////        if(player.getWorld().isAir(new BlockPos((int) playerData.storedXBlackwhip, (int) playerData.storedYBlackwhip, (int) playerData.storedZBlackwhip))) {
////            playerData.quirkAbilityTimers[slot] = 0;
////            player.sendMessage(Text.literal("Block Turned to Air"));
////        }
////
////        if(playerData.quirkAbilityTimers[slot] >= 1) {
////            // Calculate the number of particles to create for the line
////            int numberOfParticles = 50;
////
////            // Calculate the step size for interpolation
////            double stepSize = 1.0 / numberOfParticles;
////
////            double playerX = player.getX();
////            double playerY = player.getY() + 1;
////            double playerZ = player.getZ();
////
////            // Spawn particles in a line between the player and the hit block
////            for (double t = 0; t <= 1.0; t += stepSize) {
////                double interpolatedX = playerX + t * (playerData.storedXBlackwhip + 0.5 - playerX);
////                double interpolatedY = playerY + t * (playerData.storedYBlackwhip + 0.5 - playerY);
////                double interpolatedZ = playerZ + t * (playerData.storedZBlackwhip + 0.5 - playerZ);
////
////                player.getServerWorld().spawnParticles(ParticleTypes.ENCHANTED_HIT, interpolatedX, interpolatedY, interpolatedZ, 1, 0.0f, 0.0f, 0.0f, 0);
////            }
////
////        }
////        if(player.isSneaking()) {
////            playerData.quirkAbilityTimers[slot] = 0;
////            player.sendMessage(Text.literal("Reset to: " + playerData.quirkAbilityTimers[slot]));
////        }
////        if(playerData.quirkAbilityTimers[slot] != 0) {
////            playerData.quirkAbilityTimers[slot]++;
////        }
//    }
//}

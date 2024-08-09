package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.hchh.cold;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.minecraft.util.Hand;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class IceWallAbility extends ActiveAbility implements ICooldownAbility {
    private final Cooldown cooldown;
    public IceWallAbility (@NotNull IAbilityUser user) {
        super(user, "Ice Wall", "Creates a Wall of Ice", AbilityCategory.ATTACK);
        cooldown = new Cooldown(20);
    }

    @Override
    public @NotNull Cooldown getCooldown(){return cooldown;}

    @Override
    public void execute(boolean isKeyDown){

        LivingEntity entity = getEntity();
        ServerWorld world = (ServerWorld) getEntity().getWorld();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        double pz = z +1;
        double px = x + 1;
        double nx = x -3;
        double nz = z -3;
        double yaw = entity.getYaw();
        double scale = (180/Math.PI)*Math.cos(yaw);
        if(yaw >= -45 && yaw <= 45) {
            for (int i = 0; i < 3; i++) {
                double x1 = entity.getX() -1;
                for (int j = 0; j < 3; j++) {
                    var pos = new BlockPos((int) ((int) x1 + j), (int) y + i, (int) ((int) pz));
                    var state = entity.getWorld().getBlockState(pos);
                    double xx1 = x1+j;
                    if (state.isAir()) {
                        entity.getWorld().setBlockState(pos, BlockRegister.QUIRK_ICE.getDefaultState());
                        world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, xx1, y, pz, 3, 0, 3, 0, 0);
                        world.spawnParticles(ParticleTypes.SNOWFLAKE, xx1, y, pz, 3, 0, 3, 0, 0);
                    }
                    var pos0 = new BlockPos((int) ((int) x1 - j), (int) y + i, (int) ((int) pz));
                    var state0 = entity.getWorld().getBlockState(pos0);
                    double xx2= x1-j;
                    if (state.isAir()) {
                        entity.getWorld().setBlockState(pos0, BlockRegister.QUIRK_ICE.getDefaultState());
                        world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, xx2, y, pz, 3, 0, 3, 0, 0);
                        world.spawnParticles(ParticleTypes.SNOWFLAKE, xx2, y, pz, 3, 0, 3, 0, 0);
                    }

                }
            }
        }
        if(yaw < -45 && yaw >= -150){
            double z1 = entity.getZ()-1;
            for(int i=0; i<3; i++){
                for(int j=0; j<3;j++){
                    var pos = new BlockPos((int) ((int) px), (int) y + i, (int) ((int) z1+j));
                    var state = entity.getWorld().getBlockState(pos);
                    double zz1 = z1 + j;
                    if (state.isAir()) {
                        entity.getWorld().setBlockState(pos, BlockRegister.QUIRK_ICE.getDefaultState());
                        world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, px, y, zz1, 3, 0, 3,0,0);
                        world.spawnParticles(ParticleTypes.SNOWFLAKE, px, y, zz1, 3, 0, 3,0,0);
                    }
                    var pos0 = new BlockPos((int) ((int) px), (int) y + i, (int) ((int) z1-j));
                    var state0 = entity.getWorld().getBlockState(pos0);
                    double zz2 = z1 - j;
                        if (state.isAir()) {
                            entity.getWorld().setBlockState(pos0, BlockRegister.QUIRK_ICE.getDefaultState());
                            world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, px, y, zz2, 3, 0, 3,0,0);
                            world.spawnParticles(ParticleTypes.SNOWFLAKE, px, y, zz2, 3, 0, 3,0,0);
                        }

                }
            }
        }
        if((yaw >= -180 && yaw < -150)|| (yaw<=180 && yaw>=150)) {
            double x1 = entity.getX() -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                var pos = new BlockPos((int) ((int) x1 + j), (int) y + i, (int) ((int) nz));
                var state = entity.getWorld().getBlockState(pos);
                double xx1 = x1+j;
                if (state.isAir()) {
                    entity.getWorld().setBlockState(pos, BlockRegister.QUIRK_ICE.getDefaultState());
                    world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, xx1, y, nz, 3, 0, 3, 0, 0);
                    world.spawnParticles(ParticleTypes.SNOWFLAKE, xx1, y, nz, 3, 0, 3, 0, 0);
                }
                var pos0 = new BlockPos((int) ((int) x1 - j), (int) y + i, (int) ((int) nz));
                var state0 = entity.getWorld().getBlockState(pos0);
                double xx2= x1-j;
                if (state.isAir()) {
                    entity.getWorld().setBlockState(pos0, BlockRegister.QUIRK_ICE.getDefaultState());
                    world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, xx2, y, nz, 3, 0, 3, 0, 0);
                    world.spawnParticles(ParticleTypes.SNOWFLAKE, xx2, y, nz, 3, 0, 3, 0, 0);
                }

            }
        }
            }
        if(yaw < 150 && yaw > 45){
            double z1 = entity.getZ()-1;
            for(int i=0; i<3; i++){
                for(int j=0; j<3;j++){
                    var pos = new BlockPos((int) ((int) nx), (int) y + i, (int) ((int) z1+j));
                    var state = entity.getWorld().getBlockState(pos);
                    double zz1 = z1 + j;
                    if (state.isAir()) {
                        entity.getWorld().setBlockState(pos, BlockRegister.QUIRK_ICE.getDefaultState());
                        world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, nx, y, zz1, 3, 0, 3,0,0);
                        world.spawnParticles(ParticleTypes.SNOWFLAKE, nx, y, zz1, 3, 0, 3,0,0);
                    }
                    var pos0 = new BlockPos((int) ((int) nx), (int) y + i, (int) ((int) z1-j));
                    var state0 = entity.getWorld().getBlockState(pos0);
                    double zz2 = z1 - j;
                    if (state.isAir()) {
                        entity.getWorld().setBlockState(pos0, BlockRegister.QUIRK_ICE.getDefaultState());
                        world.spawnParticles(ParticleTypes.ITEM_SNOWBALL, nx, y, zz2, 3, 0, 3,0,0);
                        world.spawnParticles(ParticleTypes.SNOWFLAKE, nx, y, zz2, 3, 0, 3,0,0);
                    }
                }
            }
        }
        entity.swingHand(Hand.MAIN_HAND, true);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 10f, 4f);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, .25f, 2f);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.PLAYERS, 10f, 4f);

    }


    }

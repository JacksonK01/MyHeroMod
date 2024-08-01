package net.michaeljackson23.mineademia.quirk.abilities.hchh.ice;

import net.michaeljackson23.mineademia.blocks.BlockRegister;
import net.michaeljackson23.mineademia.entity.EntityRegister;
import net.michaeljackson23.mineademia.entity.projectile.hchh.IceProjectile;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.BasicAbility;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.YOffset;

public class IceWall extends BasicAbility {
    public IceWall() {
        super(1, 40, 20, "Create Ice Wall", "null");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        double x = player.getX() + player.getRotationVecClient().x*2;
        double y = player.getY();
        double z = player.getZ() + player.getRotationVecClient().z*2;
        for(int i = 0 ; i< 4; i++) {
            for (int j = 0; j < 10;  j++) {
                double angle = 1 * Math.PI * j / 5;
                double xOffset = 1 * Math.cos(angle) * player.getRotationVecClient().x;
                double zOffset = 1 * Math.sin(angle) * player.getRotationVecClient().z;
                var pos = new BlockPos((int) ((int) x + xOffset), (int) y + i, (int) ((int) z+ zOffset));
                var state = player.getWorld().getBlockState(pos);
                if (state.isAir())
                    player.getWorld().setBlockState(pos, BlockRegister.QUIRK_ICE.getDefaultState());
            }
        }
    }
}

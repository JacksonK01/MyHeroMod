package net.michaeljackson23.mineademia.abilitiestest.usage.abilities;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.michaeljackson23.mineademia.abilitiestest.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitiestest.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitiestest.intr.abilityyser.IAbilityUser;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import static net.michaeljackson23.mineademia.networking.Networking.DODGE;

public class Dodge extends ActiveAbility implements ICooldownAbility, ITickAbility {
    private Cooldown cooldown;
    private Vec3d oldPos;
    private boolean isDashing;
    private int ticks;
    public static final float DASH_STRENGHT = 2f;
    public Dodge(@NotNull IAbilityUser user) {
        super(user, "dodge", "dah dodge", AbilityCategory.MOBILITY);
    }

    @Override
    protected void init() {
        super.init();
        cooldown = new Cooldown(40);
        ticks=0;
    }

    @Override
    public void execute() {
        if(isReadyAndReset()) {
            var e = getUser().getEntity();
            oldPos=e.getPos();
            isDashing=true;
            ticks=0;
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        return cooldown;
    }

    @Override
    public void onTick() {
        if(isDashing&&ticks>=2){
            var newPos = getUser().getEntity().getPos();
            var v = new Vec3d(newPos.getX() - oldPos.getX() , 0, newPos.getZ() - oldPos.getZ());
            var entity = getUser().getEntity();
            if(v.length() == 0)
                v= entity.getRotationVecClient().normalize();
            entity.addVelocity(v.x * DASH_STRENGHT, 0.15f, v.z * DASH_STRENGHT);
            entity.velocityModified = true;
            isDashing=false;
            ticks=0;
        }
        ticks++;
    }
}

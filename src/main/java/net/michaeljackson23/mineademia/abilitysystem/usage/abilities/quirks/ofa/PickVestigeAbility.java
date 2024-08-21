package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.Cooldown;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ICooldownAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.extras.ITickAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.vestiges.FloatAbility;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.vestiges.SmokescreenAbility;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

//UNFINISHED
public class PickVestigeAbility extends ActiveAbility implements ITickAbility, ICooldownAbility {

    private final SmokescreenAbility smokescreen;
    private final FloatAbility floatAbility;

    private ActiveAbility currentAbility;

    private Cooldown cooldown;

    public PickVestigeAbility(@NotNull IAbilityUser user) {
        super(user, "Vestige", "Channel the vestiges that lay within your quirk, and use their power.", AbilityCategory.UTILITY);

        this.cooldown = new Cooldown(0);
        this.smokescreen = new SmokescreenAbility(user);
        this.floatAbility = new FloatAbility(user);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if((isKeyDown && currentAbility == null) || getEntity().isSneaking()) {
            if(getEntity() instanceof ServerPlayerEntity player)
                ServerPlayNetworking.send(player, Networking.S2C_OPEN_VESTIGE_GUI, PacketByteBufs.empty());
        }

        if(currentAbility != null) {
            getEntity().sendMessage(Text.literal("Ability Not Empty"));
            currentAbility.execute(isKeyDown);
        }
    }


    public void setCurrentAbility(String ability) {
        getEntity().sendMessage(Text.literal("Ability Trying to select: " + ability));
        switch (ability) {
            case "Smokescreen": this.currentAbility = smokescreen; break;
            case "Float": this.currentAbility = floatAbility; break;
            default: this.currentAbility = null; break;
        }
    }

    @Override
    public @NotNull Cooldown getCooldown() {
        if(currentAbility != null && currentAbility instanceof ICooldownAbility cooldownAbility) {
            return cooldownAbility.getCooldown();
        }
        return cooldown;
    }

    @Override
    public void onStartTick() {
        if(currentAbility != null && currentAbility instanceof ITickAbility tickAbility) {
            tickAbility.onStartTick();
        }
    }
}

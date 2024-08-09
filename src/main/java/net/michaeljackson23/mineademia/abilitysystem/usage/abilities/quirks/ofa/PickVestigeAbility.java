package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.abilitysystem.impl.ability.ActiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.AbilityCategory;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.michaeljackson23.mineademia.abilitysystem.usage.abilities.quirks.ofa.vestiges.SmokescreenAbility;
import net.michaeljackson23.mineademia.networking.Networking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

//UNFINISHED
public class PickVestigeAbility extends ActiveAbility {

    private final SmokescreenAbility smokescreen;

    public PickVestigeAbility(@NotNull IAbilityUser user) {
        super(user, "Vestige", "Channel the vestiges that lay within your quirk, and use their power.", AbilityCategory.UTILITY);

        this.smokescreen = new SmokescreenAbility(user);
    }

    @Override
    public void execute(boolean isKeyDown) {
        if(isKeyDown) {
            if(getEntity() instanceof ServerPlayerEntity player)
                ServerPlayNetworking.send(player, Networking.OPEN_VESTIGE_GUI, PacketByteBufs.empty());
        }
    }
}

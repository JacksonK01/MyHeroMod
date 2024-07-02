package net.michaeljackson23.mineademia.quirk.abilities.ofa;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.michaeljackson23.mineademia.networking.Networking;
import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityExtractor;
import net.michaeljackson23.mineademia.quirk.abilities.InfiniteAbility;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.Blackwhip;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.FaJin;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.Float;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.SmokeScreen;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class PickVestigeAbility extends InfiniteAbility implements AbilityExtractor {
    private final Blackwhip blackwhip = new Blackwhip();
    private final SmokeScreen smokescreen = new SmokeScreen();
    private final Float floatAbility = new Float();
    private final FaJin faJin = new FaJin();

    private Optional<AbilityBase> activeAbility = Optional.empty();

    public PickVestigeAbility() {
        super(0, 0, "Vestiges", "Hold shift while using this ability");
    }

    @Override
    protected void activate(ServerPlayerEntity player, Quirk quirk) {
        ServerPlayNetworking.send(player, Networking.OPEN_VESTIGE_GUI, PacketByteBufs.empty());
        player.sendMessage(Text.literal(this.title));
        deActivate(player, quirk);
    }

    public void setVestigeAbility(String ability) {
        switch(ability) {
            case "Blackwhip": this.activeAbility = Optional.of(blackwhip); break;
            case "Smokescreen": this.activeAbility = Optional.of(smokescreen); break;
            case "Float": this.activeAbility = Optional.of(floatAbility); break;
            case "FaJin": this.activeAbility = Optional.of(faJin); break;
            default: this.activeAbility = Optional.empty();
        }
    }

    @Override
    public AbilityBase extract(ServerPlayerEntity player, Quirk quirk) {
        if(player.isSneaking()) {
            return this;
        }
        return activeAbility.orElse(this);
    }
}

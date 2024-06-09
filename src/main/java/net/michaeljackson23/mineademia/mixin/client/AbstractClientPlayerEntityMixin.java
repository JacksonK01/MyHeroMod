package net.michaeljackson23.mineademia.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkData;
import net.michaeljackson23.mineademia.quirk.quirkdata.QuirkDataHelper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(value= EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin implements QuirkDataHelper {
    //This is purely to package the data from the server onto the client and have a place that it's stored
    @Unique
    private QuirkData quirkData = new QuirkData();

    @Unique
    public QuirkData myHeroMod$getQuirkData() {
        if(this.quirkData != null) {
            return this.quirkData;
        }
        return new QuirkData();
    }
    @Unique
    public void myHeroMod$setQuirkData(QuirkData data) {
        this.quirkData = data;
    }
}

package net.michaeljackson23.mineademia.quirk.quirkdata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//This is purely for the mixin to have methods I can call. Ignore this interface
@Environment(value= EnvType.CLIENT)
public interface QuirkDataHelper {
    QuirkData myHeroMod$getQuirkData();
    void myHeroMod$setQuirkData(QuirkData quirkData);
}

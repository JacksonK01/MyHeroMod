package net.michaeljackson23.mineademia.quirk.quirkdata;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.minecraft.server.MinecraftServer;

public interface QuirkDataHelper {
    QuirkData myHeroMod$getQuirkData();
    void myHeroMod$setQuirkData(QuirkData quirkData);
    Quirk myHeroMod$getQuirk(MinecraftServer server);
    void myHeroMod$setQuirk(MinecraftServer server, Quirk quirk);
}

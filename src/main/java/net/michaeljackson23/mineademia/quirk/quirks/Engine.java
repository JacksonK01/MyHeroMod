package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.AbilityBase;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.engine.SlideAndKicks;

public class Engine extends Quirk {

    public Engine() {
        super("Engine", new SlideAndKicks(), new Empty(), new Empty(), new Empty(), new Empty());
        setModelsForQuirk("EnginesAndEngineFire");
    }
}

package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.whirlwind.Tornado;
import net.michaeljackson23.mineademia.quirk.abilities.whirlwind.WindBlade;
import net.michaeljackson23.mineademia.quirk.abilities.whirlwind.WindFly;

public class Whirlwind extends Quirk {
    public Whirlwind() {
        super("Whirlwind", new WindBlade(), new Tornado(), new WindFly(), new Empty(), new Empty());
    }
}

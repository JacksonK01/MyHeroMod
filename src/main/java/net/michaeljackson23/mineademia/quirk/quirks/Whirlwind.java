package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.whirlwind.WindBlade;

public class Whirlwind extends Quirk {
    public Whirlwind() {
        super("Whirlwind", new WindBlade(), new Empty(), new Empty(), new Empty(), new Empty());
    }
}

package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.explosion.*;

public class Explosion extends Quirk {
    public Explosion() {
        super("Explosion", new APShot(), new Explode(), new ExplosionDash(), new HowitzerImpact(), new StunGrenade());
    }
}

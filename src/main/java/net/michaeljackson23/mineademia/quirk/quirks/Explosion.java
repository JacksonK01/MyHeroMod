package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.explosion.ExplosionDash;

public class Explosion extends Quirk {
    public Explosion() {
        super("Explosion", new Empty(), new Empty(), new ExplosionDash(), new Empty(), new Empty());
    }
}

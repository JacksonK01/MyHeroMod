package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.engine.SlideAndKicks;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.*;

public class OneForAll extends Quirk {

    public OneForAll() {
        super("One For All", new AirForce(), new PickVestigeAbility(), new ShootStyleKicks(), new ManchesterSmash(), new Cowling());
    }
}

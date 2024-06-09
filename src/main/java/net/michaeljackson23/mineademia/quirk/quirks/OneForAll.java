package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.miscellaneous.Griddy;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.*;

public class OneForAll extends Quirk {

    public OneForAll() {
        super("One For All", new AirForce(), new Blackwhip(), new Slide(), new Empty(), new Cowling());
    }
}

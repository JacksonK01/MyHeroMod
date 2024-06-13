package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.*;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.vestiges.Blackwhip;

public class OneForAll extends Quirk {

    public OneForAll() {
        super("One For All", new AirForce(), new PickVestigeAbility(), new SlideAndKicks(), new StLouisSmash(), new Cowling());
    }
}

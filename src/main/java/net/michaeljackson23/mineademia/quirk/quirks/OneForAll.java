package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.AirForce;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.Blackwhip;
import net.michaeljackson23.mineademia.quirk.abilities.ofa.Cowling;

public class OneForAll extends Quirk {

    public OneForAll() {
        super("One For All", new AirForce(), new Blackwhip(), new Empty(), new Empty(), new Cowling());
    }
}

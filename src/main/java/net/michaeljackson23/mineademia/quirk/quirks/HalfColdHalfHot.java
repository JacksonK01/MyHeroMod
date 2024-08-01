package net.michaeljackson23.mineademia.quirk.quirks;

import net.michaeljackson23.mineademia.quirk.Quirk;
import net.michaeljackson23.mineademia.quirk.abilities.Empty;
import net.michaeljackson23.mineademia.quirk.abilities.hchh.fire.FireShockwave;
import net.michaeljackson23.mineademia.quirk.abilities.hchh.fire.FireShoot;
import net.michaeljackson23.mineademia.quirk.abilities.hchh.fire.WallOfFlame;
import net.michaeljackson23.mineademia.quirk.abilities.hchh.fire.Volcano;
import net.michaeljackson23.mineademia.quirk.abilities.hchh.ice.IceShoot;
import net.michaeljackson23.mineademia.quirk.abilities.hchh.ice.IceWall;

public class HalfColdHalfHot extends Quirk {

    public HalfColdHalfHot() {
        super("Half-Cold Half-Hot", new FireShoot(), new WallOfFlame(), new Volcano(), new FireShockwave(), new IceWall());
    }
}

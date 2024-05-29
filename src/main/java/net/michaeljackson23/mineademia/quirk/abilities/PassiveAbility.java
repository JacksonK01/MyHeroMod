package net.michaeljackson23.mineademia.quirk.abilities;

@FunctionalInterface
public interface PassiveAbility {
    //will return true when ability is done, false while is still active
    //true = ability is done
    boolean activate();
}

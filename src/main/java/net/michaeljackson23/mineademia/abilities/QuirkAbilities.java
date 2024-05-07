package net.michaeljackson23.mineademia.abilities;

public enum QuirkAbilities {
    EMPTY(0),
    AIR_FORCE(1),
    BLACKWHIP(2),
    COWLING(3);

    private final int id;

    private QuirkAbilities(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}



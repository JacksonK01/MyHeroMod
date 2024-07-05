package net.michaeljackson23.mineademia.combo;

public enum ComboType {
    PUNCH("punch"),
    KICK("kick"),
    AERIAL("aerial"),
    NONE("none");

    private final String name;

    ComboType(String name) {
        this.name = name;
    }

    public String getType() {
        return name;
    }
}
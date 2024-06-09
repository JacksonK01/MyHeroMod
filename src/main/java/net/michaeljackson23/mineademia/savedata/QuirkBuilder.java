package net.michaeljackson23.mineademia.savedata;

public class QuirkBuilder {
    private String quirkName;
    private boolean isRandom;

    public QuirkBuilder(String quirkName, boolean isRandom) {
        this.quirkName = quirkName;
        this.isRandom = isRandom;
    }

    public String getQuirkName() {
        return this.quirkName;
    }

    public boolean isRandom() {
        return this.isRandom;
    }

    public void reBuild(String quirkName, boolean isRandom) {
        this.quirkName = quirkName;
        this.isRandom = isRandom;
    }
}

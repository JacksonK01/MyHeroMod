package net.michaeljackson23.mineademia.init;
import net.michaeljackson23.mineademia.quirk.Quirk;

import java.util.ArrayList;

//This may feel like useless abstraction but this is built to properly scale with size of the mod as it grows
public class PlayerData {
    Quirk quirk;
    int dodgeCounter;
    int blockingCounter;
    ArrayList<String> obtainedQuirks = new ArrayList<>();

    public void setQuirk(Quirk quirk) {
        if(this.quirk != null) {
            obtainedQuirks.add(quirk.getName());
        }
        this.quirk = quirk;
    }

    public Quirk getQuirk() {
        return this.quirk;
    }

}

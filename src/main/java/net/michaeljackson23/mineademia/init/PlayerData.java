package net.michaeljackson23.mineademia.init;
import net.michaeljackson23.mineademia.abilities.AbilityBase;
import net.michaeljackson23.mineademia.abilities.abilityinit.IAbilityHandler;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    //This is all the information I store onto the player
    public String playerQuirk = "empty";
    public int[] quirkAbilities = {0, 0, 0, 0, 0};
    public int[] quirkAbilityTimers = {0, 0, 0, 0, 0};
    public boolean[] abilityActive = {false, false, false, false, false};
    public boolean[] keyBindsHeld = {false, false, false, false, false}; //In order of keybinds 1 through 5
    public int quirkCooldown = 0;
    public int quirkStamina = 1000;

    public Stack<AbilityBase> abilityStack = new Stack<>();

    //This is for leveling up your quirk, I don't know what I'll use it for yet
    public List<Integer> quirkStats = new ArrayList<>();

    public double[] storedBlackwhip = {0, 0, 0};

    public PlayerData() {
        quirkStats.add(0);
        quirkStats.add(0);
        quirkStats.add(0);
    }
}

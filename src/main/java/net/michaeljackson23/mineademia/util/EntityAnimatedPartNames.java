package net.michaeljackson23.mineademia.util;

import net.minecraft.client.render.entity.model.EntityModelPartNames;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

public final class EntityAnimatedPartNames {

    private static final ArrayList<String> allParts = new ArrayList<>();

    static {
        addPart("root");
    }

    public static final String JOINTED_RIGHT_ARM = addPart("jointed_right_arm");
    public static final String JOINTED_RIGHT_UPPER_ARM = addPart("jointed_right_upper_arm");
    public static final String JOINTED_RIGHT_FORE_ARM = addPart("jointed_right_fore_arm");

    public static final String JOINTED_LEFT_ARM = addPart("jointed_left_arm");
    public static final String JOINTED_LEFT_UPPER_ARM = addPart("jointed_left_upper_arm");
    public static final String JOINTED_LEFT_FORE_ARM = addPart("jointed_left_fore_arm");

    public static final String JOINTED_RIGHT_LEG = addPart("jointed_right_leg");
    public static final String JOINTED_RIGHT_UPPER_LEG = addPart("jointed_right_upper_leg");
    public static final String JOINTED_RIGHT_LOWER_LEG = addPart("jointed_right_lower_leg");

    public static final String JOINTED_LEFT_LEG = addPart("jointed_left_leg");
    public static final String JOINTED_LEFT_UPPER_LEG = addPart("jointed_left_upper_leg");
    public static final String JOINTED_LEFT_LOWER_LEG = addPart("jointed_left_lower_leg");

    public final static String ANIMATED_ROOT = addPart("animated_root");

    public final static String ANIMATED_HEAD = addPart("animated_head");

    public final static String ANIMATED_BODY = addPart("animated_body");

    private static String addPart(String part) {
        allParts.add(part);
        return part;
    }

    public static String getEquivalencePart(String part) {
        switch(part) {
            case EntityModelPartNames.ROOT -> {
                return ANIMATED_ROOT;
            }
            case EntityModelPartNames.HEAD -> {
                return ANIMATED_HEAD;
            }
            case EntityModelPartNames.BODY -> {
                return ANIMATED_BODY;
            }
            case EntityModelPartNames.RIGHT_ARM -> {
                return JOINTED_RIGHT_ARM;
            }
            case EntityModelPartNames.LEFT_ARM -> {
                return JOINTED_LEFT_ARM;
            }
            case EntityModelPartNames.RIGHT_LEG -> {
                return JOINTED_RIGHT_LEG;
            }
            case EntityModelPartNames.LEFT_LEG -> {
                return JOINTED_LEFT_LEG;
            }
            default -> {
                return part;
            }
        }
    }

    public static ArrayList<String> getAllParts() {
        return allParts;
    }
}

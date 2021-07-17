package models.cards.monsters;
import java.util.HashMap;

public enum Attribute {
    DARK("DARK"),
    EARTH("EARTH"),
    FIRE("FIRE"),
    LIGHT("LIGHT"),
    WATER("WATER"),
    WIND("WIND");

    private static final HashMap<String, Attribute> MAP = new HashMap<>();
    private final String label;

    Attribute(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static Attribute getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (Attribute field : Attribute.values()) {
            MAP.put(field.getLabel(), field);
        }
    }
}

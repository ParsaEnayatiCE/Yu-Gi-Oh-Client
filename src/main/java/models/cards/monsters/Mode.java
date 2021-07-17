package models.cards.monsters;

public enum Mode {
    ATTACK("attack"),
    DEFENSE("defense");

    private final String label;

    Mode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}

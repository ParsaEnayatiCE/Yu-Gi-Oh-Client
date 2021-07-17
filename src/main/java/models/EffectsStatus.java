package models;

import models.cards.monsters.SpecialSummonStatus;


public class EffectsStatus {
    private boolean isRivalSpellBlocked = false;
    private boolean isRivalTrapsBlocked = false;
    private boolean canRivalAttack = true;
    private boolean isRivalReveled = false;
    private boolean canStrongRivalAttack = true;
    private boolean canRivalPickCard = true;
    private boolean canRitualSummon = false;
    private SpecialSummonStatus specialSummonStatus = SpecialSummonStatus.NONE;

    public boolean getRivalSpellBlocked() {
        return this.isRivalSpellBlocked;
    }

    public void setRivalSpellBlocked(boolean isRivalSpellBlocked) {
        this.isRivalSpellBlocked = isRivalSpellBlocked;
    }

    public boolean getRivalTrapsBlocked() {
        return this.isRivalTrapsBlocked;
    }

    public void setRivalTrapsBlocked(boolean isRivalTrapsBlocked) {
        this.isRivalTrapsBlocked = isRivalTrapsBlocked;
    }

    public boolean isRivalReveled() {
        return this.isRivalReveled;
    }

    public void setRivalReveled(boolean rivalReveled) {
        this.isRivalReveled = rivalReveled;
    }

    public boolean getCanRivalAttack() {
        return this.canRivalAttack;
    }

    public void setCanRivalAttack(boolean canRivalAttack) {
        this.canRivalAttack = canRivalAttack;
    }

    public boolean getCanStrongRivalAttack() {
        return this.canStrongRivalAttack;
    }

    public void setCanRivalPickCard(boolean canRivalPickCard) {
        this.canRivalPickCard = canRivalPickCard;
    }

    public boolean getCanRivalPickCard() {
        return this.canRivalPickCard;
    }

    public void setCanStrongRivalAttack(boolean canStrongRivalAttack) {
        this.canStrongRivalAttack = canStrongRivalAttack;
    }

    public boolean getCanRitualSummon() {
        return this.canRitualSummon;
    }

    public void setCanRitualSummon(boolean canRitualSummon) {
        this.canRitualSummon = canRitualSummon;
    }

    public SpecialSummonStatus getSpecialSummonStatus() {
        return this.specialSummonStatus;
    }

    public void setSpecialSummonStatus(SpecialSummonStatus specialSummonStatus) {
        this.specialSummonStatus = specialSummonStatus;
    }
}

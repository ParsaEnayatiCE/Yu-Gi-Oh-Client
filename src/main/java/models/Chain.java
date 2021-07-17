package models;

import controller.duel.effect.spells.NormalActivate;
import controller.duel.effect.spells.QuickPlays;
import controller.duel.effect.spells.RingOfDefense;
import controller.duel.effect.spells.SpellAbsorption;
import controller.duel.effect.traps.MagicJammer;
import controller.duel.effect.traps.NormalTraps;
import controller.duel.effect.traps.SummonTraps;
import controller.duel.effect.traps.TimeSeal;
import models.cards.CardType;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

import java.util.ArrayList;

public class Chain {
    private static final ArrayList<SpellTrapCard> chain = new ArrayList<>();
    private static final ArrayList<Board> myBoards = new ArrayList<>();
    private static final ArrayList<Board> rivalBoards = new ArrayList<>();
    private static final ArrayList<MonsterCard> summoned = new ArrayList<>();

    public static void addSpell(SpellTrapCard spellTrapCard, Board myBoard, Board rivalBoard, MonsterCard attacked) {
        chain.add(spellTrapCard);
        myBoards.add(myBoard);
        rivalBoards.add(rivalBoard);
        summoned.add(attacked);
    }

    public static void activate() {
        boolean hasAbsorptionWorked = false;
        for (int i = chain.size() - 1; i >= 0; i--) {
            SpellTrapCard spellTrapCard = chain.get(i);
            activateSpells(spellTrapCard, myBoards.get(i), rivalBoards.get(i));
            activateTraps(spellTrapCard, myBoards.get(i), rivalBoards.get(i), summoned.get(i));
            if (!hasAbsorptionWorked) {
                SpellAbsorption.check(myBoards.get(i), rivalBoards.get(i));
                hasAbsorptionWorked = true;
            }
            chain.remove(i);
            myBoards.remove(i);
            rivalBoards.remove(i);
            summoned.remove(i);
        }
    }

    private static void activateTraps(SpellTrapCard spellTrapCard, Board myBoard, Board rivalBoard, MonsterCard summonedCard) {
        if (spellTrapCard.getCardType() != CardType.TRAP)
            return;
        if (rivalBoard.getEffectsStatus().getRivalTrapsBlocked())
            return;
        if (!spellTrapCard.getName().equals("Magic Jammer"))
            MagicJammer.activateMagicJammer(spellTrapCard, myBoard);
        else if (spellTrapCard.getName().equals("Mind Crush"))
            NormalTraps.activateMindCrush(myBoard, rivalBoard);
        else if (spellTrapCard.getName().equals("Call of The Haunted"))
            NormalTraps.activateCallOfHaunted(myBoard);
        else if (spellTrapCard.getName().equals("Time Seal"))
            TimeSeal.activateTimeSeal(myBoard);
        if (spellTrapCard.getName().equals("Trap Hole"))
            SummonTraps.activateTrapHole(summonedCard, rivalBoard);
        else if (spellTrapCard.getName().equals("Torrential Tribute"))
            SummonTraps.activateTorrentialTribute(myBoard, rivalBoard);
        else if (spellTrapCard.getName().equals("Solemn Warning"))
            SummonTraps.activateSolemnWarning(summonedCard, myBoard, rivalBoard);
        if (spellTrapCard.getName().equals("Ring of defense"))
            RingOfDefense.activate(myBoard);
    }

    private static void activateSpells(SpellTrapCard spellTrapCard, Board myBoard, Board rivalBoard) {
        if (spellTrapCard.getCardType() != CardType.SPELL)
            return;
        if (rivalBoard.getEffectsStatus().getRivalSpellBlocked()) {
            rivalBoard.getEffectsStatus().setRivalSpellBlocked(false);
            return;
        }
        if (spellTrapCard.getName().equals("Twin Twisters"))
            QuickPlays.activateTwinTwister(myBoard, rivalBoard);
        else if (spellTrapCard.getName().equals("Mystical space typhoon"))
            QuickPlays.activateMysticalSpaceTyphoon(rivalBoard);
        else if (spellTrapCard.getName().equals("Ring of defense"))
            QuickPlays.activateRingOfDefenses(myBoard);
        else if (spellTrapCard.getName().equals("Pot of Greed"))
            NormalActivate.affectPotOfGreed(myBoard);
        else if (spellTrapCard.getName().equals("Raigeki"))
            NormalActivate.affectReigeki(rivalBoard);
        else if (spellTrapCard.getName().equals("Harpie's Feather Duster"))
            NormalActivate.affectHarpie(rivalBoard);
        else if (spellTrapCard.getName().equals("Dark Hole"))
            NormalActivate.affectDarkHole(myBoard, rivalBoard);
        else if (spellTrapCard.getName().equals("Terraforming"))
            NormalActivate.activateTeraforming(myBoard);
        else if (spellTrapCard.getName().equals("Advanced Ritual Art"))
            NormalActivate.activateRitual(myBoard);
    }

    public static int getSize() {
        return chain.size();
    }
}

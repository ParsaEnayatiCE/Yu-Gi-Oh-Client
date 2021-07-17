package controller.duel;

import controller.duel.singlePlayer.GameController;
import models.cards.Location;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.SummonType;
import models.cards.spelltrap.Icon;
import models.cards.spelltrap.SpellTrapCard;
import view.DuelView;

public class SettingController {


    public String set() {
        if (SelectionController.selectedCard instanceof MonsterCard)
            return setMonster();
        else
            return setSpellTrap();
    }

    private String setSpellTrap() {
        if (SelectionController.selectedCard == null)
            return "no card is selected yet";
        SpellTrapCard selectedSpellTrap = (SpellTrapCard) SelectionController.selectedCard;
        if (DuelView.isMultiPlayer) {
            if (!PhaseController.playerInTurn.getPlayerBoard().getHandCards().contains(SelectionController.selectedCard))
                return "you can't set this card";
            if (PhaseController.currentPhase != GamePhase.MAIN1 && PhaseController.currentPhase != GamePhase.MAIN2)
                return "you can't do this action in this phase";
            if (PhaseController.playerInTurn.getPlayerBoard().getSpellTraps().size() == 5 && selectedSpellTrap.getIcon() != Icon.FIELD)
                return "spell card zone is full";
        } else {
            if (!GameController.player.getPlayerBoard().getHandCards().contains(SelectionController.selectedCard))
                return "you can't set this card";
            if (GameController.currentPhase != GamePhase.MAIN1 && GameController.currentPhase != GamePhase.MAIN2)
                return "you can't do this action in this phase";
            if (GameController.player.getPlayerBoard().getSpellTraps().size() == 5 && selectedSpellTrap.getIcon() != Icon.FIELD)
                return "spell card zone is full";
        }
        selectedSpellTrap.setLocation(Location.FIELD);
        selectedSpellTrap.setIsHidden(true);
        if (DuelView.isMultiPlayer) {
            if (selectedSpellTrap.getIcon() == Icon.FIELD)
                PhaseController.playerInTurn.getPlayerBoard().setFieldZone(selectedSpellTrap);
            else
                PhaseController.playerInTurn.getPlayerBoard().summonOrSetSpellAndTrap(selectedSpellTrap);
        } else {
            if (selectedSpellTrap.getIcon() == Icon.FIELD)
                GameController.player.getPlayerBoard().setFieldZone(selectedSpellTrap);
            else
                GameController.player.getPlayerBoard().summonOrSetSpellAndTrap(selectedSpellTrap);
        }
        return "set successfully";
    }

    private String setMonster() {
        if (SummonController.checkNormalSummonSetConditions(false) != null)
            return SummonController.checkNormalSummonSetConditions(false);
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        selectedMonster.setLocation(Location.FIELD);
        selectedMonster.setIsHidden(true);
        selectedMonster.setMode(Mode.DEFENSE);
        if (DuelView.isMultiPlayer)
            PhaseController.playerInTurn.getPlayerBoard().summonOrSetMonster(selectedMonster);
        else
            GameController.player.getPlayerBoard().summonOrSetMonster(selectedMonster);
        SummonController.hasSummonedInThisTurn = true;
        selectedMonster.setSummonType(SummonType.NORMAL_SET);
        return "set successfully";
    }

    public String changePosition() {
        if (SelectionController.selectedCard == null)
            return "no card is selected yet";
        if (DuelView.isMultiPlayer) {
            if (!PhaseController.playerInTurn.getPlayerBoard().getMonsters().contains((MonsterCard) SelectionController.selectedCard))
                return "you can't change this card position";
            if (PhaseController.currentPhase != GamePhase.MAIN1 && PhaseController.currentPhase != GamePhase.MAIN2)
                return "you can't do this action in this phase";
        } else {
            if (!GameController.player.getPlayerBoard().getMonsters().contains((MonsterCard) SelectionController.selectedCard))
                return "you can't change this card position";
            if (GameController.currentPhase != GamePhase.MAIN1 && GameController.currentPhase != GamePhase.MAIN2)
                return "you can't do this action in this phase";
        }
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        if (selectedMonster.getMode() == Mode.ATTACK)
            selectedMonster.setMode(Mode.DEFENSE);
        else
            selectedMonster.setMode(Mode.ATTACK);
        return "monster card position changed successfully";
    }
}

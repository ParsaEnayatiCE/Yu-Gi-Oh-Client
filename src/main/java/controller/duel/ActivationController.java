package controller.duel;

import controller.duel.effect.CustomEffects;
import controller.duel.singlePlayer.GameController;
import controller.duel.effect.spells.*;
import controller.duel.effect.traps.MagicJammer;
import controller.duel.effect.traps.NormalTraps;
import controller.duel.effect.traps.SummonTraps;
import controller.duel.effect.traps.TimeSeal;
import models.Board;
import models.cards.CardType;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;
import view.DuelView;
import view.StatusEnum;

public class ActivationController {

    public String equip(int monsterIndex) {
        if (checkActivationConditions() != null)
            return checkActivationConditions();
        if (DuelView.isMultiPlayer) {
            if (PhaseController.currentPhase != GamePhase.MAIN2 && PhaseController.currentPhase != GamePhase.MAIN1)
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        } else {
            if (GameController.currentPhase != GamePhase.MAIN2 && GameController.currentPhase != GamePhase.MAIN1)
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        }
        if (monsterIndex > 5)
            return StatusEnum.INVALID_SELECTION.getStatus();
        MonsterCard monsterCard;
        if (DuelView.isMultiPlayer)
            monsterCard = PhaseController.playerInTurn.getPlayerBoard().getMonsterBoard().get(monsterIndex - 1);
        else
            monsterCard = GameController.player.getPlayerBoard().getMonsterBoard().get(monsterIndex - 1);
        if (monsterCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        boolean hasAffected = EquipSpells.equip((SpellTrapCard) SelectionController.selectedCard, monsterCard);
        if (!hasAffected)
            return "wrong selection";
        return StatusEnum.SPELL_ACTIVATED.getStatus();
    }

    public String activate() {
        if (checkActivationConditions() != null)
            return checkActivationConditions();
        if (DuelView.isMultiPlayer) {
            if (CustomEffects.activate(SelectionController.selectedCard, PhaseController.playerInTurn.getPlayerBoard(),
                    PhaseController.playerAgainst.getPlayerBoard()))
                return "spell activated";
        } else {
            if (CustomEffects.activate(SelectionController.selectedCard, GameController.player.getPlayerBoard(),
                    GameController.bot.getBoard()))
                return "spell activated";
        }
        if (SelectionController.selectedCard.getCardType() == CardType.SPELL)
            return activateSpell();
        else
            return activateTrap();
    }

    public String activateTrap() {
        if (checkActivationConditions() != null)
            return checkActivationConditions();
        SpellTrapCard trapCard = (SpellTrapCard) SelectionController.selectedCard;
        Board myBoard;
        Board rivalBoard;
        if (DuelView.isMultiPlayer) {
            myBoard = PhaseController.playerInTurn.getPlayerBoard();
            rivalBoard = PhaseController.playerAgainst.getPlayerBoard();
            if ((PhaseController.currentPhase == GamePhase.RIVAL_TURN && !trapCard.getIsHidden())
                    || (PhaseController.currentPhase != GamePhase.MAIN1 && PhaseController.currentPhase != GamePhase.MAIN2))
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        } else {
            myBoard = GameController.player.getPlayerBoard();
            rivalBoard = GameController.bot.getBoard();
            if ((GameController.currentPhase == GamePhase.RIVAL_TURN && !trapCard.getIsHidden())
                    || (GameController.currentPhase != GamePhase.MAIN1 && GameController.currentPhase != GamePhase.MAIN2))
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        }
        if (MagicJammer.activate(trapCard, myBoard, rivalBoard))
            return StatusEnum.SPELL_OR_TRAP_ACTIVATED.getStatus();
        else if (NormalTraps.activate(trapCard, myBoard, rivalBoard))
            return StatusEnum.SPELL_OR_TRAP_ACTIVATED.getStatus();
        else if (SummonTraps.activate(trapCard, SummonController.lastSummonedMonster, myBoard, rivalBoard))
            return StatusEnum.SPELL_OR_TRAP_ACTIVATED.getStatus();
        else if (TimeSeal.activate(trapCard, myBoard))
            return StatusEnum.SPELL_OR_TRAP_ACTIVATED.getStatus();
        return "trap can't be activated";
    }

    public String activateSpell() {
        if (checkActivationConditions() != null)
            return checkActivationConditions();
        SpellTrapCard spellCard = (SpellTrapCard) SelectionController.selectedCard;

        if (DuelView.isMultiPlayer) {
            if (PhaseController.currentPhase == GamePhase.RIVAL_TURN && SelectionController.selectedCard.getIsHidden()) {
                if (QuickPlays.activate(spellCard, PhaseController.playerInTurn.getPlayerBoard(), PhaseController.playerAgainst.getPlayerBoard()))
                    return StatusEnum.SPELL_ACTIVATED.getStatus();
                else return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
            }
            if (PhaseController.currentPhase != GamePhase.MAIN2 && PhaseController.currentPhase != GamePhase.MAIN1)
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
            if (MessengerOfPeace.activate(spellCard, PhaseController.playerInTurn.getPlayerBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (NormalActivate.activate(spellCard, PhaseController.playerInTurn.getPlayerBoard(), PhaseController.playerAgainst.getPlayerBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (TurnSpells.activate(spellCard, PhaseController.playerInTurn.getPlayerBoard(), AttackController.isAnyMonsterDead))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (QuickPlays.activate(spellCard, PhaseController.playerInTurn.getPlayerBoard(), PhaseController.playerAgainst.getPlayerBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (RingOfDefense.activate(spellCard, PhaseController.playerInTurn.getPlayerBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
        } else {
            if (GameController.currentPhase == GamePhase.RIVAL_TURN && SelectionController.selectedCard.getIsHidden()) {
                if (QuickPlays.activate(spellCard, GameController.player.getPlayerBoard(), GameController.bot.getBoard()))
                    return StatusEnum.SPELL_ACTIVATED.getStatus();
                else return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
            }
            if (GameController.currentPhase != GamePhase.MAIN2 && GameController.currentPhase != GamePhase.MAIN1)
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
            if (MessengerOfPeace.activate(spellCard, GameController.player.getPlayerBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (NormalActivate.activate(spellCard, GameController.player.getPlayerBoard(), GameController.bot.getBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (TurnSpells.activate(spellCard, GameController.player.getPlayerBoard(), AttackController.isAnyMonsterDead))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (QuickPlays.activate(spellCard, GameController.player.getPlayerBoard(), GameController.bot.getBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
            else if (RingOfDefense.activate(spellCard, GameController.player.getPlayerBoard()))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
        }

        return StatusEnum.PREPARATION_OF_SPELL_NOT_DONE.getStatus();
    }

    public String activateOnMonster(int monsterIndex) {
        if (checkActivationConditions() != null)
            return checkActivationConditions();
        if (DuelView.isMultiPlayer) {
            if (PhaseController.currentPhase != GamePhase.MAIN2 && PhaseController.currentPhase != GamePhase.MAIN1)
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
            if (OnMonsterSpells.activate((SpellTrapCard) SelectionController.selectedCard,
                    PhaseController.playerInTurn.getPlayerBoard(), PhaseController.playerAgainst.getPlayerBoard(), monsterIndex))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
        } else {
            if (GameController.currentPhase != GamePhase.MAIN2 && GameController.currentPhase != GamePhase.MAIN1)
                return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
            if (OnMonsterSpells.activate((SpellTrapCard) SelectionController.selectedCard,
                    GameController.player.getPlayerBoard(), GameController.bot.getBoard(), monsterIndex))
                return StatusEnum.SPELL_ACTIVATED.getStatus();
        }
        return "wrong selection";
    }

    private String checkActivationConditions() {
        if (SelectionController.selectedCard == null)
            return StatusEnum.NO_CARD_SELECTED_YET.getStatus();
        if (SelectionController.selectedCard.getCardType() == CardType.MONSTER)
            return "activate is not for monsters";
        return null;
    }
}

package controller.duel;

import controller.duel.effect.monsterseffect.ContinuousEffects;
import controller.duel.effect.monsterseffect.SummonEffects;
import controller.duel.singlePlayer.GameController;
import models.Board;
import models.EffectsStatus;
import models.cards.Location;
import models.cards.monsters.*;
import view.DuelView;
import view.StatusEnum;

public class SummonController {

    public static boolean hasSummonedInThisTurn = false;
    public static MonsterCard lastSummonedMonster;

    public String summon() {
        if (checkNormalSummonSetConditions(false) != null)
            return checkNormalSummonSetConditions(false);
        return finalizeSummon();
    }

    public String tributeSummon(String tributes) {
        if (checkNormalSummonSetConditions(false) != null)
            return checkNormalSummonSetConditions(false);
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        if (DuelView.isMultiPlayer) {
            if ((selectedMonster.getLevel() < 7 && PhaseController.playerInTurn.getPlayerBoard().getMonsters().size() < 1)
                    || selectedMonster.getLevel() > 6 && PhaseController.playerInTurn.getPlayerBoard().getMonsters().size() < 2)
                return "there are not enough cards for tribute";
        } else {
            if ((selectedMonster.getLevel() < 7 && GameController.player.getPlayerBoard().getMonsters().size() < 1)
                    || selectedMonster.getLevel() > 6 && GameController.player.getPlayerBoard().getMonsters().size() < 2)
                return "there are not enough cards for tribute";
        }
        int firstMonster, secondMonster = 0;
        if (tributes.length() == 1)
            firstMonster = Integer.parseInt(tributes);
        else {
            firstMonster = Integer.parseInt(tributes.substring(0, 1));
            secondMonster = Integer.parseInt(tributes.substring(2, 3));
        }
        if (DuelView.isMultiPlayer) {
            if (selectedMonster.getLevel() > 6 && secondMonster == 0
                    || (selectedMonster.getLevel() > 6 && (PhaseController.playerInTurn.getPlayerBoard().getMonsterBoard().get(firstMonster - 1) == null
                    || PhaseController.playerInTurn.getPlayerBoard().getMonsterBoard().get(secondMonster - 1) == null))
                    || selectedMonster.getLevel() < 7 && PhaseController.playerInTurn.getPlayerBoard().getMonsterBoard().get(firstMonster - 1) == null)
                return "there are not enough monsters on these addresses";
        } else {
            if (selectedMonster.getLevel() > 6 && secondMonster == 0
                    || (selectedMonster.getLevel() > 6 && (GameController.player.getPlayerBoard().getMonsterBoard().get(firstMonster - 1) == null
                    || GameController.player.getPlayerBoard().getMonsterBoard().get(secondMonster - 1) == null))
                    || selectedMonster.getLevel() < 7 && GameController.player.getPlayerBoard().getMonsterBoard().get(firstMonster - 1) == null)
                return "there are not enough monsters on these addresses";
        }
        selectedMonster.setSummonType(SummonType.TRIBUTE_SUMMON);
        return finalizeSummon();
    }

    private String finalizeSummon() {
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        selectedMonster.setLocation(Location.FIELD);
        selectedMonster.setIsHidden(false);
        selectedMonster.setMode(Mode.ATTACK);
        if (DuelView.isMultiPlayer)
            PhaseController.playerInTurn.getPlayerBoard().summonOrSetMonster(selectedMonster);
        else
            GameController.player.getPlayerBoard().summonOrSetMonster(selectedMonster);
        hasSummonedInThisTurn = true;
        lastSummonedMonster = selectedMonster;
        if (DuelView.isMultiPlayer)
            ContinuousEffects.run(PhaseController.playerInTurn.getPlayerBoard(), PhaseController.playerAgainst.getPlayerBoard());
        else
            ContinuousEffects.run(GameController.player.getPlayerBoard(), GameController.bot.getBoard());
        return "summoned successfully";
    }

    public static String checkNormalSummonSetConditions(boolean isSpecial) {
        if (SelectionController.selectedCard == null)
            return "no card is selected yet";
        if (DuelView.isMultiPlayer) {
            if (!PhaseController.playerInTurn.getPlayerBoard().getHandCards().contains(SelectionController.selectedCard)
                    || !(SelectionController.selectedCard instanceof MonsterCard))
                return "you can't summon this card";
        } else {
            if (!GameController.player.getPlayerBoard().getHandCards().contains(SelectionController.selectedCard)
                    || !(SelectionController.selectedCard instanceof MonsterCard))
                return "you can't summon this card";
        }
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        if (!isSpecial && selectedMonster.getLevel() > 4)
            return "you can't summon this card";
        if (DuelView.isMultiPlayer) {
            if (PhaseController.currentPhase != GamePhase.MAIN1 && PhaseController.currentPhase != GamePhase.MAIN2)
                return "action not allowed in this phase";
            if (PhaseController.playerInTurn.getPlayerBoard().getMonsters().size() == 5)
                return "monster card zone is full";
        } else {
            if (GameController.currentPhase != GamePhase.MAIN1 && GameController.currentPhase != GamePhase.MAIN2)
                return "action not allowed in this phase";
            if (GameController.player.getPlayerBoard().getMonsters().size() == 5)
                return "monster card zone is full";
        }
        if (!isSpecial && hasSummonedInThisTurn)
            return "you already summoned/set on this turn";
        return null;
    }

    public String flipSummon() {
        if (SelectionController.selectedCard == null)
            return "no card is selected yet";
        if (DuelView.isMultiPlayer) {
            if (!PhaseController.playerInTurn.getPlayerBoard().getMonsters().contains((MonsterCard) SelectionController.selectedCard))
                return "you can't change this card position";
        } else {
            if (!GameController.player.getPlayerBoard().getMonsters().contains((MonsterCard) SelectionController.selectedCard))
                return "you can't change this card position";
        }
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        if (DuelView.isMultiPlayer) {
            if (PhaseController.currentPhase != GamePhase.MAIN1 && PhaseController.currentPhase != GamePhase.MAIN2)
                return "action not allowed in this phase";
        } else {
            if (GameController.currentPhase != GamePhase.MAIN1 && GameController.currentPhase != GamePhase.MAIN2)
                return "action not allowed in this phase";
        }
        if (!selectedMonster.getIsHidden())
            return "you can't flip this card";
        selectedMonster.setMode(Mode.ATTACK);
        selectedMonster.setIsHidden(false);
        lastSummonedMonster = selectedMonster;
        selectedMonster.setSummonType(SummonType.FLIP_SUMMON);
        if (DuelView.isMultiPlayer) {
            ContinuousEffects.run(PhaseController.playerInTurn.getPlayerBoard(), PhaseController.playerAgainst.getPlayerBoard());
            SummonEffects.run((MonsterCard) SelectionController.selectedCard, PhaseController.playerAgainst.getPlayerBoard(), PhaseController.playerInTurn.getPlayerBoard());
        } else {
            ContinuousEffects.run(GameController.player.getPlayerBoard(), GameController.bot.getBoard());
            SummonEffects.run((MonsterCard) SelectionController.selectedCard, GameController.bot.getBoard(), GameController.player.getPlayerBoard());
        }
        return "flip summoned successfully";
    }

    public String specialSummon() {
        if (checkNormalSummonSetConditions(true) != null)
            return checkNormalSummonSetConditions(true);
        if (checkSpecialSummonStatus() != null)
            return checkSpecialSummonStatus();
        return finalizeSummon();
    }

    private String checkSpecialSummonStatus() {
        EffectsStatus effectsStatus;
        if (DuelView.isMultiPlayer)
            effectsStatus = PhaseController.playerInTurn.getPlayerBoard().getEffectsStatus();
        else
            effectsStatus = GameController.player.getPlayerBoard().getEffectsStatus();
        MonsterCard monsterCard = (MonsterCard) SelectionController.selectedCard;
        if (effectsStatus.getSpecialSummonStatus() == SpecialSummonStatus.NONE)
            return StatusEnum.NO_WAY_TO_SPECIAL_SUMMON.getStatus();
        if (effectsStatus.getSpecialSummonStatus() == SpecialSummonStatus.FROM_GRAVEYARD
                && monsterCard.getLocation() != Location.GRAVEYARD)
            return StatusEnum.NO_WAY_TO_SPECIAL_SUMMON.getStatus();
        if (effectsStatus.getSpecialSummonStatus() == SpecialSummonStatus.LEVEL7H_FROM_GRAVE
                && (monsterCard.getLocation() != Location.GRAVEYARD || monsterCard.getLevel() < 7))
            return StatusEnum.NO_WAY_TO_SPECIAL_SUMMON.getStatus();
        if (effectsStatus.getSpecialSummonStatus() == SpecialSummonStatus.NORMAL_CYBERSE
                && (monsterCard.getMonsterType() != MonsterType.CYBERSE || monsterCard.getTrait() != Trait.NORMAL))
            return StatusEnum.NO_WAY_TO_SPECIAL_SUMMON.getStatus();
        if (effectsStatus.getSpecialSummonStatus() == SpecialSummonStatus.NORMAL_LEVEL4L_FROM_HAND
                && (monsterCard.getLocation() != Location.HAND || monsterCard.getLevel() > 4 || monsterCard.getTrait() != Trait.NORMAL))
            return StatusEnum.NO_WAY_TO_SPECIAL_SUMMON.getStatus();
        monsterCard.setSummonType(SummonType.SPECIAL_SUMMON);
        effectsStatus.setSpecialSummonStatus(SpecialSummonStatus.NONE);
        return null;
    }

    public String ritualSummon(String monsterIndexes) {
        if (checkNormalSummonSetConditions(true) != null)
            return checkNormalSummonSetConditions(true);
        MonsterCard selectedMonster = (MonsterCard) SelectionController.selectedCard;
        Board board;
        if (DuelView.isMultiPlayer)
            board = PhaseController.playerInTurn.getPlayerBoard();
        else
            board = GameController.player.getPlayerBoard();
        if (selectedMonster.getTrait() != Trait.RITUAL || !board.getEffectsStatus().getCanRitualSummon())
            return StatusEnum.NO_WAY_TO_RITUAL.getStatus();
        String[] handIndexes = monsterIndexes.split(" ");
        int levelSum = 0;
        for (String handIndex : handIndexes) {
            if (!handIndex.matches("\\d") || Integer.parseInt(handIndex) >= board.getHandCards().size() ||
                    board.getHandCards().get(Integer.parseInt(handIndex)) == null ||
                    !(board.getHandCards().get(Integer.parseInt(handIndex)) instanceof MonsterCard))
                return StatusEnum.NO_WAY_TO_RITUAL.getStatus();
            else
                levelSum += ((MonsterCard) board.getHandCards().get(Integer.parseInt(handIndex))).getLevel();
        }
        if (levelSum != selectedMonster.getLevel())
            return StatusEnum.NO_WAY_TO_RITUAL.getStatus();
        board.getEffectsStatus().setCanRitualSummon(false);
        selectedMonster.setSummonType(SummonType.SPECIAL_SUMMON);
        return finalizeSummon();
    }
}

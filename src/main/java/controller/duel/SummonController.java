package controller.duel;

import controller.ConnectionController;
import controller.duel.singlePlayer.GameController;
import controller.menucontroller.LoginMenuController;
import models.cards.monsters.*;
import view.DuelView;
import view.StatusEnum;

import java.io.IOException;

public class SummonController {

    public static boolean hasSummonedInThisTurn = false;
    public static MonsterCard lastSummonedMonster;

    public String summon() {
        try {
            ConnectionController.getDataOutputStream().writeUTF("summon-" + LoginMenuController.getToken());
            ConnectionController.getDataOutputStream().flush();
            String result = ConnectionController.getDataInputStream().readUTF();
            String[] response = result.split("\n");
            //TODO update board
            return response[0];
        } catch (IOException ignored) {
            return StatusEnum.SERVER_CONNECTION_FAILED.getStatus();
        }
    }

    public String tributeSummon(String tributes) {
        try {
            ConnectionController.getDataOutputStream().writeUTF("tributeSummon-" + tributes + "-" + LoginMenuController.getToken());
            ConnectionController.getDataOutputStream().flush();
            String result = ConnectionController.getDataInputStream().readUTF();
            String[] response = result.split("\n");
            //TODO update board
            return response[0];
        } catch (IOException ignored) {
            return StatusEnum.SERVER_CONNECTION_FAILED.getStatus();
        }
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
        try {
            ConnectionController.getDataOutputStream().writeUTF("flipSummon-" + LoginMenuController.getToken());
            ConnectionController.getDataOutputStream().flush();
            String result = ConnectionController.getDataInputStream().readUTF();
            String[] response = result.split("\n");
            //TODO update board
            return response[0];
        } catch (IOException ignored) {
            return StatusEnum.SERVER_CONNECTION_FAILED.getStatus();
        }
    }

    public String specialSummon() {
        try {
            ConnectionController.getDataOutputStream().writeUTF("specialSummon-" + LoginMenuController.getToken());
            ConnectionController.getDataOutputStream().flush();
            String result = ConnectionController.getDataInputStream().readUTF();
            String[] response = result.split("\n");
            //TODO update board
            return response[0];
        } catch (IOException ignored) {
            return StatusEnum.SERVER_CONNECTION_FAILED.getStatus();
        }
    }

    public String ritualSummon(String monsterIndexes) {
        try {
            ConnectionController.getDataOutputStream().writeUTF("ritualSummon-" + monsterIndexes + "-" + LoginMenuController.getToken());
            ConnectionController.getDataOutputStream().flush();
            String result = ConnectionController.getDataInputStream().readUTF();
            String[] response = result.split("\n");
            //TODO update board
            return response[0];
        } catch (IOException ignored) {
            return StatusEnum.SERVER_CONNECTION_FAILED.getStatus();
        }
    }
}

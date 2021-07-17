package controller.duel;

import controller.GUI.DuelViewSceneController;
import controller.duel.effect.monsterseffect.ContinuousEffects;
import controller.duel.effect.monsterseffect.TurnEffects;
import controller.duel.singlePlayer.AI;
import controller.duel.effect.spells.OnMonsterSpells;
import controller.duel.effect.spells.FiledSpells;
import controller.duel.effect.spells.MessengerOfPeace;
import controller.duel.effect.spells.TurnSpells;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import models.Chain;
import models.Player;
import models.User;
import models.cards.Card;
import view.DuelView;
import view.MenuEnum;
import view.ProgramController;
import view.StatusEnum;

import java.io.IOException;

public class PhaseController {
    public static Player playerInTurn;
    public static Player playerAgainst;
    public static GamePhase currentPhase = GamePhase.DRAW;
    public static boolean isFirstPlay = true;

    public void startTheGame() {
        int coin = (int) (Math.random() * 2);
        if (coin == 0) {
            playerInTurn = Player.getFirstPlayer();
            playerAgainst = Player.getSecondPlayer();
        } else {
            playerInTurn = Player.getSecondPlayer();
            playerAgainst = Player.getFirstPlayer();
        }
        System.out.println("Coin has Flipped and " + playerInTurn.getNickName() + " goes First");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(printBoard());
    }

    public String printBoard() {
        String middleLine = "\n--------------------------\n";
        if (!DuelView.isMultiPlayer) {
            String playerBoard = Player.getFirstPlayer().getPlayerBoard().toString();
            String botBoard = AI.getInstance().getBoard().reverseToString();
            return botBoard + middleLine + playerBoard;
        }
            String firstBoard = playerInTurn.getPlayerBoard().toString();
            String secondBoard = playerAgainst.getPlayerBoard().reverseToString();
            return secondBoard + middleLine + firstBoard;
    }

    public String changePhase(MouseEvent event) {
        findNextPhase();
        AttackController.alreadyAttackedCards.clear();
        AttackController.isBattleHappened = false;
        StringBuilder result = new StringBuilder(currentPhase.getLabel());
        if (currentPhase == GamePhase.DRAW) {
            SummonController.hasSummonedInThisTurn = false;
            Player keepPlayer = PhaseController.playerInTurn;
            playerInTurn = playerAgainst;
            playerAgainst = keepPlayer;
            if (playerAgainst.getPlayerBoard().getEffectsStatus().getCanRivalPickCard()) {
                Card card = playerInTurn.getPlayerBoard().drawCard();
                if (card == null)
                    endGame(playerAgainst, playerInTurn, event);
                assert card != null;
                result.append("\nnew card added to hand: ").append(card.getName());
            }
            else result.append("\n can't pick card");
        }
        else if (currentPhase == GamePhase.STANDBY) {
            TurnEffects.run(playerInTurn.getPlayerBoard(), playerAgainst.getPlayerBoard());
            FiledSpells.check(playerInTurn.getPlayerBoard(), playerAgainst.getPlayerBoard());
            MessengerOfPeace.checkStandBy(playerInTurn.getPlayerBoard());
            playerAgainst.getPlayerBoard().getEffectsStatus().setCanRivalPickCard(true);
        }
        else if (currentPhase == GamePhase.BATTLE)
            ContinuousEffects.run(playerInTurn.getPlayerBoard(), playerAgainst.getPlayerBoard());
        else if (currentPhase == GamePhase.RIVAL_TURN) {
            result.append("\nit's ").append(playerAgainst.getNickName()).append("'s turn to activate quick spells or traps");
            Player keepPlayer = PhaseController.playerInTurn;
            playerInTurn = playerAgainst;
            playerAgainst = keepPlayer;
        }
        else if (currentPhase == GamePhase.MAIN2) {
            result.append("\nit's ").append(playerAgainst.getNickName()).append("'s turn again to respond to rival");
            Player keepPlayer = PhaseController.playerInTurn;
            playerInTurn = playerAgainst;
            playerAgainst = keepPlayer;
        }
        else if (currentPhase == GamePhase.END) {
            result.append("\nit's ").append(playerAgainst.getNickName()).append("'s turn");
            ContinuousEffects.run(playerInTurn.getPlayerBoard(), playerAgainst.getPlayerBoard());
            Chain.activate();
            SelectionController.selectedCard = null;
            resetSomeEffects();
            SummonController.hasSummonedInThisTurn = false;
            System.out.println(changePhase(event));
            isFirstPlay = false;
        }
        else if (currentPhase == GamePhase.SWITCH_CARDS1) {
            cardSwitched = 0;
            result.append("\nit's first player turn to switch cards between main and side deck");
            playerInTurn = Player.getFirstPlayer();
            playerAgainst = Player.getSecondPlayer();
        }
        else if (currentPhase == GamePhase.SWITCH_CARDS2) {
            cardSwitched = 0;
            result.append("\nit's second player turn to switch cards between main and side deck");
            playerInTurn = Player.getSecondPlayer();
            playerAgainst = Player.getFirstPlayer();
        }
        return result.toString();
    }

    private void resetSomeEffects() {
        playerInTurn.getPlayerBoard().getEffectsStatus().setRivalTrapsBlocked(false);
        playerAgainst.getPlayerBoard().getEffectsStatus().setRivalTrapsBlocked(false);
        AttackController.isAnyMonsterDead = false;
        AttackController.isBattleHappened = false;
        TurnSpells.checkTurn(playerInTurn.getPlayerBoard());
        OnMonsterSpells.deactivate(playerInTurn.getPlayerBoard(), playerAgainst.getPlayerBoard());
    }

    private void findNextPhase() {
        if (currentPhase == GamePhase.DRAW)
            currentPhase = GamePhase.STANDBY;
        else if (currentPhase == GamePhase.STANDBY)
            currentPhase = GamePhase.MAIN1;
        else if (currentPhase == GamePhase.MAIN1)
            currentPhase = GamePhase.BATTLE;
        else if (currentPhase == GamePhase.BATTLE)
            currentPhase = GamePhase.RIVAL_TURN;
        else if (currentPhase == GamePhase.RIVAL_TURN)
            currentPhase = GamePhase.MAIN2;
        else if (currentPhase == GamePhase.MAIN2)
            currentPhase = GamePhase.END;
        else if (currentPhase == GamePhase.SWITCH_CARDS1)
            currentPhase = GamePhase.SWITCH_CARDS2;
        else
            currentPhase = GamePhase.DRAW;
    }

    public void endGame(Player winner, Player looser, MouseEvent event) {
        User winnerUser = winner.getUser();
        User loserUser = looser.getUser();
        if (DuelView.rounds == 1) {
            winnerUser.setMoney(winnerUser.getMoney() + 1000 + winner.getPlayerBoard().getLifePoints());
            loserUser.setMoney(loserUser.getMoney() + 100);
            winnerUser.setScore(winnerUser.getScore() + 1000);
            ProgramController.currentMenu = MenuEnum.MAIN_MENU;
            Card.resetSwitch();
            Player.removePlayers();
            System.out.println(winnerUser.getUserName() + " won the whole match with score: 1-0");
            DuelView.shouldDrawBoard = false;
            try {
                DuelViewSceneController.end(winnerUser, event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (winner.equals(Player.getFirstPlayer()))
                DuelView.player1Wins++;
            else
                DuelView.player2Wins++;
            winner.setMaxLifePoint(winner.getPlayerBoard().getLifePoints());
            looser.setMaxLifePoint(looser.getPlayerBoard().getLifePoints());
            if (DuelView.player2Wins == 2 || DuelView.player1Wins == 2) {
                loserUser.setMoney(loserUser.getMoney() + 300);
                winnerUser.setScore(winnerUser.getScore() + 3000);
                winnerUser.setMoney(winnerUser.getMoney() + winner.getMaxLifePoint() * 3 + 3000);
                ProgramController.currentMenu = MenuEnum.MAIN_MENU;
                Card.resetSwitch();
                Player.removePlayers();
                System.out.println(winnerUser.getUserName() + " won the whole match with score: " +
                        DuelView.player1Wins + "-" + DuelView.player2Wins);
                DuelView.shouldDrawBoard = false;
                try {
                    DuelViewSceneController.end(winnerUser, event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println(winnerUser.getUserName() + "won the game and the score is: " +
                        DuelView.player1Wins + "-" + DuelView.player2Wins);
                isFirstPlay = true;
                currentPhase = GamePhase.SWITCH_CARDS1;
            }
        }
    }

    private int cardSwitched = 0;
    public String switchCards(int mainCardIndex, int sideCardIndex) {
        if (currentPhase != GamePhase.SWITCH_CARDS1)
            return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        if (mainCardIndex >= playerInTurn.getPlayerDeck().getMainDeck().size() || sideCardIndex >= playerInTurn.getPlayerDeck().getSideDeck().size())
            return "wrong index";
        if (playerInTurn.getPlayerDeck().getMainDeck().get(mainCardIndex) == null || playerInTurn.getPlayerDeck().getSideDeck().get(sideCardIndex) == null)
            return "there is no cards in chosen index";
        if (cardSwitched == 2)
            return "you can't switch any more cards";
        if (playerInTurn.getPlayerDeck().getMainDeck().get(mainCardIndex).getIsSwitched()
                || playerInTurn.getPlayerDeck().getSideDeck().get(sideCardIndex).getIsSwitched())
            return "this card has already been switched";
        cardSwitched++;
        playerInTurn.getPlayerDeck().getMainDeck().get(mainCardIndex).setSwitched(true);
        playerInTurn.getPlayerDeck().getSideDeck().get(sideCardIndex).setSwitched(true);
        playerInTurn.getPlayerBoard().resetTheBoard(mainCardIndex, sideCardIndex);
        return "switched cards successfully";
    }
}

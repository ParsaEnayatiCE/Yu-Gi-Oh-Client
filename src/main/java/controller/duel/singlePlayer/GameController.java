package controller.duel.singlePlayer;

import controller.GUI.DuelViewSceneController;
import controller.duel.*;
import controller.duel.effect.monsterseffect.ContinuousEffects;
import controller.duel.effect.monsterseffect.TurnEffects;
import controller.duel.effect.spells.FiledSpells;
import controller.duel.effect.spells.MessengerOfPeace;
import controller.duel.effect.spells.OnMonsterSpells;
import controller.duel.effect.spells.TurnSpells;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import models.Chain;
import models.Player;
import models.User;
import models.cards.Card;
import view.DuelView;
import view.MenuEnum;
import view.ProgramController;

import java.io.IOException;

public class GameController {
    public static AI bot;
    public static Player player;
    public static boolean isPlayerTurn;
    public static GamePhase currentPhase = GamePhase.DRAW;
    public static boolean isFirstPlay = true;

    public void startTheGame(MouseEvent event) {
        isFirstPlay = true;
        bot = AI.getInstance();
        player = bot.getOpponent();
        int coin = (int) (Math.random() * 2);
        if (coin == 0) {
            isPlayerTurn = true;
            System.out.println("Coin has Flipped and " + player.getNickName() + " goes First");
        } else {
            isPlayerTurn = false;
            System.out.println("Coin has Flipped and " + bot.getName() + " goes First");
        }
        System.out.println(printBoard());
        if (!isPlayerTurn)
            runBot(event);
    }

    public String printBoard() {
        String middleLine = "\n--------------------------\n";
        if (isPlayerTurn) {
            String playerBoard = Player.getFirstPlayer().getPlayerBoard().toString();
            String botBoard = AI.getInstance().getBoard().reverseToString();
            return botBoard + middleLine + playerBoard;
        }
        else {
            String playerBoard = Player.getFirstPlayer().getPlayerBoard().reverseToString();
            String botBoard = AI.getInstance().getBoard().toString();
            return playerBoard + middleLine + botBoard;
        }
    }

    public String changePhase(MouseEvent event) {
        findNextPhase();
        AttackController.alreadyAttackedCards.clear();
        AttackController.isBattleHappened = false;
        StringBuilder result = new StringBuilder(currentPhase.getLabel());
        if (currentPhase == GamePhase.DRAW) {
            SummonController.hasSummonedInThisTurn = false;
            isPlayerTurn = !isPlayerTurn;
            Card card;
            if (isPlayerTurn)
                card = player.getPlayerBoard().drawCard();
            else
                card = bot.getBoard().drawCard();
            if (card == null) {
                if (isPlayerTurn)
                    endGame("bot", event);
                else
                    endGame("player", event);
            }
            if (isPlayerTurn) {
                assert card != null;
                result.append("\nnew card added to hand: ").append(card.getName());
            } else
                runBot(event);
        }
        else if (currentPhase == GamePhase.STANDBY) {
            if (isPlayerTurn) {
                TurnEffects.run(player.getPlayerBoard(), bot.getBoard());
                FiledSpells.check(player.getPlayerBoard(), bot.getBoard());
                MessengerOfPeace.checkStandBy(player.getPlayerBoard());
                bot.getBoard().getEffectsStatus().setCanRivalPickCard(true);
            } else {
                TurnEffects.run(bot.getBoard(), player.getPlayerBoard());
                FiledSpells.check(bot.getBoard(), player.getPlayerBoard());
                MessengerOfPeace.checkStandBy(bot.getBoard());
                player.getPlayerBoard().getEffectsStatus().setCanRivalPickCard(true);
            }
        }
        else if (currentPhase == GamePhase.BATTLE) {
            if (isPlayerTurn)
                ContinuousEffects.run(player.getPlayerBoard(), bot.getBoard());
            else
                ContinuousEffects.run(bot.getBoard(), player.getPlayerBoard());
        }
        else if (currentPhase == GamePhase.END) {
            if (isPlayerTurn) {
                result.append("\nit's ").append(bot.getName()).append("'s turn");
                ContinuousEffects.run(player.getPlayerBoard(), bot.getBoard());
            }
            else {
                result.append("\nit's ").append(player.getNickName()).append("'s turn");
                ContinuousEffects.run(bot.getBoard(), player.getPlayerBoard());
            }
            Chain.activate();
            SelectionController.selectedCard = null;
            resetSomeEffects();
            SummonController.hasSummonedInThisTurn = false;
            isFirstPlay = false;
            System.out.println(changePhase(event));
        }
        return result.toString();
    }

    private void resetSomeEffects() {
        player.getPlayerBoard().getEffectsStatus().setRivalTrapsBlocked(false);
        bot.getBoard().getEffectsStatus().setRivalTrapsBlocked(false);
        AttackController.isAnyMonsterDead = false;
        AttackController.isBattleHappened = false;
        if (isPlayerTurn)
            TurnSpells.checkTurn(player.getPlayerBoard());
        else
            TurnSpells.checkTurn(bot.getBoard());
        if (isPlayerTurn)
            OnMonsterSpells.deactivate(player.getPlayerBoard(), bot.getBoard());
        else
            OnMonsterSpells.deactivate(bot.getBoard(), player.getPlayerBoard());
    }

    private void findNextPhase() {
        if (currentPhase == GamePhase.DRAW)
            currentPhase = GamePhase.STANDBY;
        else if (currentPhase == GamePhase.STANDBY)
            currentPhase = GamePhase.MAIN1;
        else if (currentPhase == GamePhase.MAIN1)
            currentPhase = GamePhase.BATTLE;
        else if (currentPhase == GamePhase.BATTLE)
            currentPhase = GamePhase.END;
        else
            currentPhase = GamePhase.DRAW;
    }

    public void endGame(String winner, MouseEvent event) {
        User user = player.getUser();
        if (DuelView.rounds == 1) {
            if (winner.equals("bot")) {
                user.setMoney(user.getMoney() + 100);
            } else {
                user.setMoney(user.getMoney() + 1000 + player.getPlayerBoard().getLifePoints());
                user.setScore(user.getScore() + 1000);
            }
            ProgramController.currentMenu = MenuEnum.MAIN_MENU;
            Player.removePlayers();
            if (winner.equals("bot")) {
                System.out.println(bot.getName() + " won the whole match with score: 1-0");
                try {
                    DuelViewSceneController.end(null, event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                System.out.println(user.getUserName() + " won the whole match with score: 1-0");
                try {
                    DuelViewSceneController.end(user, event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            DuelView.shouldDrawBoard = false;
        }
        else {
            if (winner.equals("bot"))
                DuelView.botWins++;
            else
                DuelView.player1Wins++;
            player.setMaxLifePoint(player.getPlayerBoard().getLifePoints());
            if (DuelView.player1Wins == 2 || DuelView.botWins == 2) {
                if (winner.equals("bot")) {
                    user.setMoney(user.getMoney() + 300);
                } else {
                    user.setMoney(user.getMoney() + player.getMaxLifePoint() * 3 + 3000);
                    user.setScore(user.getScore() + 3000);
                }
                ProgramController.currentMenu = MenuEnum.MAIN_MENU;
                Player.removePlayers();
                if (winner.equals("bot")) {
                    System.out.println(bot.getName() + " won the whole match with score: " +
                            DuelView.botWins + "-" + DuelView.player1Wins);
                    try {
                        DuelViewSceneController.end(null, event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println(player.getUserName() + " won the whole match with score: " +
                            DuelView.player1Wins + "-" + DuelView.botWins);
                    try {
                        DuelViewSceneController.end(user, event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                DuelView.shouldDrawBoard = false;
            }
            else {
                if (winner.equals("bot"))
                    System.out.println(bot.getName() + "won the game and the score is: " +
                            DuelView.botWins + "-" + DuelView.player1Wins);
                else
                    System.out.println(player.getUserName() + "won the game and the score is: " +
                            DuelView.player1Wins + "-" + DuelView.botWins);
                bot.getBoard().resetTheBoard(null, null);
                isFirstPlay = true;
                currentPhase = GamePhase.DRAW;
            }
        }
    }

    private void runBot(MouseEvent event) {
        bot.resetAttacks();
        if (bot.getName().equals("EasyBot")) {
            Timeline timeline = new Timeline();
            KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(1), e -> {
                ((EasyBot) bot).summonBestMonster();
                bot.summonSpellTrapIfCan();
                if (!isFirstPlay)
                    DuelViewSceneController.getInstance().showChangesForBot();
            });
            KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1), e -> {
                ((EasyBot) bot).checkSpellForActivate(currentPhase);
                ((EasyBot) bot).checkTrapForActivate();
                if (!isFirstPlay)
                    DuelViewSceneController.getInstance().showChangesForBot();
            });
            KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(1), e -> {
                if (!isFirstPlay) {
                    ((EasyBot) bot).attack();
                    DuelViewSceneController.getInstance().showChangesForBot();
                }
            });
            timeline.getKeyFrames().setAll(keyFrame1, keyFrame2, keyFrame3);
            timeline.setCycleCount(1);
            timeline.play();
        } else {
            Timeline timeline = new Timeline();
            KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(1), e -> {
                ((HardBot) bot).summonBestMonster();
                bot.summonSpellTrapIfCan();
                if (!isFirstPlay)
                    DuelViewSceneController.getInstance().showChangesForBot();
            });
            KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1), e -> {
                ((HardBot) bot).checkSpellForActivate(currentPhase);
                ((HardBot) bot).checkTrapForActivate();
                if (!isFirstPlay)
                    DuelViewSceneController.getInstance().showChangesForBot();
            });
            KeyFrame keyFrame3 = new KeyFrame(Duration.seconds(1), e -> {
                if (!isFirstPlay) {
                    ((HardBot) bot).attack();
                    DuelViewSceneController.getInstance().showChangesForBot();
                }
            });
            timeline.getKeyFrames().setAll(keyFrame1, keyFrame2, keyFrame3);
            timeline.setCycleCount(1);
            timeline.play();
        }
        if (bot.getOpponent().getPlayerBoard().getLifePoints() <= 0)
            endGame("bot", event);
        if (bot.getBoard().getLifePoints() <= 0)
            endGame("player", event);
        boolean isFirst = isFirstPlay;
        currentPhase = GamePhase.BATTLE;
        System.out.println(changePhase(event));
        if (isFirst)
            System.out.println(printBoard());
    }
}

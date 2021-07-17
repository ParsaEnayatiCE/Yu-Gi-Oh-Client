package controller.duel;

import controller.GUI.DuelViewSceneController;
import controller.duel.effect.monsterseffect.GetAttackedEffects;
import controller.duel.effect.traps.GetAttackedTraps;
import controller.duel.singlePlayer.AI;
import controller.duel.singlePlayer.GameController;
import javafx.scene.input.MouseEvent;
import models.Player;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import view.DuelView;
import view.StatusEnum;

import java.util.ArrayList;

public class AttackController {
    //Note That player is always the attacker and opponent is attacker or defender
    public static boolean isBattleHappened;
    public static boolean isAnyMonsterDead = false;
    private GamePhase gamePhase;
    private Player opponent;
    private AI opponentBot;
    private Player player;
    public static ArrayList<MonsterCard> alreadyAttackedCards = new ArrayList<>();
    private final PhaseController phaseController = new PhaseController();
    private final GameController gameController = new GameController();

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    private void damagePlayer(Player player, int damagePoints) {
        player.getPlayerBoard().setLifePoints(player.getPlayerBoard().getLifePoints() - damagePoints);
    }

    private void damageBot(AI bot, int damagePoints) {
        bot.getBoard().setLifePoints(bot.getBoard().getLifePoints() - damagePoints);
    }

    private int isPlayerMonsterStrongerThanOpponent(MonsterCard playerCard, MonsterCard opponentCard) {
        // return 1 if player is stronger. 2 if are the same. 3 if is weaker
        if (calculateDifferenceOPoint(playerCard, opponentCard) > 0) {
            return 1;
        } else if (calculateDifferenceOPoint(playerCard, opponentCard) == 0) {
            return 2;
        } else {
            return 3;
        }

    }

    private boolean areBothMonstersOffensive(MonsterCard playerCard, MonsterCard opponentCard) {
        return (isOffensive(playerCard) && isOffensive(opponentCard));
    }

    private boolean isOffensive(MonsterCard card) {
        return card.getMode().equals(Mode.ATTACK);
    }

    public int calculateDifferenceOPoint(MonsterCard playerCard, MonsterCard opponentCard) {
        if (areBothMonstersOffensive(playerCard, opponentCard)) {
            return playerCard.getAttackPoint() - opponentCard.getAttackPoint();
        } else {
            return playerCard.getAttackPoint() - opponentCard.getDefensePoint();
        }
    }

    public void destroyMonster(MonsterCard playerCard, MonsterCard opponentCard) {

        if (DuelView.isMultiPlayer) {
            if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 1) {
                if (GetAttackedEffects.run(playerCard, opponentCard, opponent.getPlayerBoard(), player.getPlayerBoard()))
                    return;
                this.opponent.getPlayerBoard().removeMonster(opponent.getPlayerBoard().getMonsterIndexInMonsterBoard(opponentCard));
            } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 2) {
                if (areBothMonstersOffensive(playerCard, opponentCard)) {
                    this.opponent.getPlayerBoard().removeMonster(opponent.getPlayerBoard().getMonsterIndexInMonsterBoard(opponentCard));
                    this.player.getPlayerBoard().removeMonster(player.getPlayerBoard().getMonsterIndexInMonsterBoard(playerCard));
                }
            } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 3) {
                if (areBothMonstersOffensive(playerCard, opponentCard)) {
                    this.player.getPlayerBoard().removeMonster(player.getPlayerBoard().getMonsterIndexInMonsterBoard(playerCard));
                }
            }
        } else {
            if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 1) {
                if (GetAttackedEffects.run(playerCard, opponentCard, opponentBot.getBoard(), player.getPlayerBoard()))
                    return;
                this.opponentBot.getBoard().removeMonster(opponentBot.getBoard().getMonsterIndexInMonsterBoard(opponentCard));
            } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 2) {
                if (areBothMonstersOffensive(playerCard, opponentCard)) {
                    this.opponentBot.getBoard().removeMonster(opponentBot.getBoard().getMonsterIndexInMonsterBoard(opponentCard));
                    this.player.getPlayerBoard().removeMonster(player.getPlayerBoard().getMonsterIndexInMonsterBoard(playerCard));
                }
            } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 3) {
                if (areBothMonstersOffensive(playerCard, opponentCard)) {
                    this.player.getPlayerBoard().removeMonster(player.getPlayerBoard().getMonsterIndexInMonsterBoard(playerCard));
                }
            }
        }
        alreadyAttackedCards.add(playerCard);


    }


    //-----------------------Attacks------------------------------
    public String directAttack() {
        if (DuelView.isMultiPlayer) {
            setGamePhase(PhaseController.currentPhase);
            opponent = PhaseController.playerAgainst;
            player = PhaseController.playerInTurn;
        } else {
            setGamePhase(GameController.currentPhase);
            opponentBot = GameController.bot;
            player = GameController.player;
        }
        MonsterCard playerCard = (MonsterCard) SelectionController.selectedCard;
        if (playerCard == null)
            return StatusEnum.NO_CARD_SELECTED_YET.getStatus();
        if (DuelView.isMultiPlayer) {
            if (!PhaseController.playerInTurn.getPlayerBoard().getMonsters().contains(playerCard))
                return StatusEnum.CANT_ATTACK_WITH_CARD.getStatus();
        } else {
            if (!GameController.player.getPlayerBoard().getMonsters().contains(playerCard))
                return StatusEnum.CANT_ATTACK_WITH_CARD.getStatus();
        }
        if (!checkPhaseValidity())
            return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        if (checkAlreadyAttacked(playerCard))
            return StatusEnum.CARD_ALREADY_ATTACKED.getStatus();
        if (DuelView.isMultiPlayer) {
            if (PhaseController.playerAgainst.getPlayerBoard().getMonsters().size() > 0 ||
                    PhaseController.isFirstPlay)
                return StatusEnum.CANT_ATTACK_DIRECTLY.getStatus();
        } else {
            if (GameController.bot.getBoard().getMonsters().size() > 0 ||
                    GameController.isFirstPlay)
                return StatusEnum.CANT_ATTACK_DIRECTLY.getStatus();
        }
            isBattleHappened = true;
            if (DuelView.isMultiPlayer)
                damagePlayer(this.opponent, playerCard.getAttackPoint());
            else if (opponentBot.activateTimeSeal())
                return "your opponent activated Time Seal and all your monsters Destroyed";
            else
                damageBot(this.opponentBot, playerCard.getAttackPoint());
            alreadyAttackedCards.add(playerCard);
            return "your opponent receives" + playerCard.getAttackPoint() + "battle damage\n";
    }

    public String attackMonsterToMonster(String rivalCardNumber) {
        MonsterCard opponentCard;
        if (DuelView.isMultiPlayer) {
            setGamePhase(PhaseController.currentPhase);
            opponent = PhaseController.playerAgainst;
            player = PhaseController.playerInTurn;
            opponentCard = PhaseController.playerAgainst.getPlayerBoard().getMonsterBoard()
                    .get(Integer.parseInt(rivalCardNumber) - 1);
        } else {
            setGamePhase(GameController.currentPhase);
            opponentBot = GameController.bot;
            player = GameController.player;
            opponentCard = GameController.bot.getBoard().getMonsterBoard()
                    .get(Integer.parseInt(rivalCardNumber) - 1);
        }
        MonsterCard playerCard = (MonsterCard) SelectionController.selectedCard;
        if (playerCard == null)
            return StatusEnum.NO_CARD_SELECTED_YET.getStatus();
        if (DuelView.isMultiPlayer) {
            if ((!opponent.getPlayerBoard().getEffectsStatus().getCanStrongRivalAttack() && playerCard.getAttackPoint() >= 1500)
                    || !opponent.getPlayerBoard().getEffectsStatus().getCanRivalAttack()
                    || GetAttackedTraps.activate(playerCard, opponent.getPlayerBoard(), player.getPlayerBoard()))
                return "can't attack";
        } else {
            if ((!opponentBot.getBoard().getEffectsStatus().getCanStrongRivalAttack() && playerCard.getAttackPoint() >= 1500)
                    || !opponentBot.getBoard().getEffectsStatus().getCanRivalAttack()
                    || GetAttackedTraps.activate(playerCard, opponentBot.getBoard(), player.getPlayerBoard()))
                return "can't attack";
        }
        if (DuelView.isMultiPlayer) {
            if (!PhaseController.playerInTurn.getPlayerBoard().getMonsters().contains(playerCard))
                return StatusEnum.CANT_ATTACK_WITH_CARD.getStatus();
        } else {
            if (!GameController.player.getPlayerBoard().getMonsters().contains(playerCard))
                return StatusEnum.CANT_ATTACK_WITH_CARD.getStatus();
        }
        if (!checkPhaseValidity()) {
            return StatusEnum.CANT_DO_THIS_ACTION_IN_THIS_PHASE.getStatus();
        } else if (checkAlreadyAttacked(playerCard)) {
            return StatusEnum.CARD_ALREADY_ATTACKED.getStatus();
        } else if (opponentCard == null) {
            return StatusEnum.NO_CARD_TO_ATTACK_HERE.getStatus();
        } else {
            //--------------------Attack OO-------------------------
            isBattleHappened = true;
            alreadyAttackedCards.add(playerCard);
            if (!DuelView.isMultiPlayer && opponentBot.activateTimeSeal())
                return "your opponent activated Time Seal and all your monsters Destroyed";
            if (areBothMonstersOffensive(playerCard, opponentCard)) {
                destroyMonster(playerCard, opponentCard);
                int damage = calculateDifferenceOPoint(playerCard, opponentCard);

                if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 1) {
                    if (DuelView.isMultiPlayer)
                        damagePlayer(opponent, damage);
                    else
                        damageBot(opponentBot, damage);
                    return "your opponent’s monster is destroyed and your opponent receives " + damage + " battle damage";
                } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 2) {
                    return StatusEnum.BOTH_RECEIVED_DAMAGE_OO.getStatus();
                } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 3) {
                    damage *= -1;
                    damagePlayer(player, damage);
                    return "Your monster card is destroyed and you received " + damage + " battle damage";

                }
            }
            //--------------------Attack DH & DO--------------------
            else {
                destroyMonster(playerCard, opponentCard);
                int damage = calculateDifferenceOPoint(playerCard, opponentCard);
                //DO------------------------------------
                if (!opponentCard.getIsHidden()) {
                    if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 1) {
                        return StatusEnum.DEFENSE_POSITION_DESTROYED_DO.getStatus();
                    } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 2) {
                        return StatusEnum.NO_CARD_DESTROYED_EQUAL_DEFENSES_DO.getStatus();
                    } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 3) {
                        damage *= -1;
                        damagePlayer(player, damage);
                        return "no card is destroyed and you received" + damage + "battle damage";
                    }
                }
                //DH-------------------------------------
                else {
                    if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 1) {
                        return "opponent’s monster card was " + opponentCard.getName() + " and " + StatusEnum.DEFENSE_POSITION_DESTROYED_DH.getStatus();
                    } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 2) {
                        return "opponent’s monster card was " + opponentCard.getName() + " and " + StatusEnum.NO_CARD_DESTROYED_EQUAL_DEFENSES_DO.getStatus();
                    } else if (isPlayerMonsterStrongerThanOpponent(playerCard, opponentCard) == 3) {
                        damage *= -1;
                        damagePlayer(player, damage);
                        return "opponent’s monster card was " + opponentCard.getName() + " and " + "no card is destroyed and you received" + damage + "battle damage";
                    }
                }

            }
        }
        return null;
    }

    //------------------------------------------------------------
    private boolean checkPhaseValidity() {
        return gamePhase.equals(GamePhase.BATTLE);
    }


    private boolean checkAlreadyAttacked(MonsterCard card) {
        for (MonsterCard a : alreadyAttackedCards
        ) {
            if (a == card) {
                return true;
            }
        }
        return false;
    }

    public void checkEndGame(MouseEvent event) {
        if (DuelViewSceneController.isMultiPlayer) {
            if (Player.getFirstPlayer().getPlayerBoard().getLifePoints() <= 0)
                phaseController.endGame(Player.getSecondPlayer(), Player.getFirstPlayer(), event);
            else if (Player.getSecondPlayer().getPlayerBoard().getLifePoints() <= 0)
                phaseController.endGame(Player.getFirstPlayer(), Player.getSecondPlayer(), event);
        } else {
            if (GameController.player.getPlayerBoard().getLifePoints() <= 0)
                gameController.endGame("bot", event);
            else if (GameController.bot.getBoard().getLifePoints() <= 0)
                gameController.endGame("human", event);
        }
    }

}

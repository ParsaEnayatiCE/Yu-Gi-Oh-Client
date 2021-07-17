package controller.duel.singlePlayer;

import controller.duel.GamePhase;
import models.Board;
import models.Deck;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.Player;

public class EasyBot extends AI {

    public EasyBot(Player opponent) throws CloneNotSupportedException {
        setName("EasyBot");
        setDeck((Deck) opponent.getPlayerDeck().clone());
        setBoard(new Board(this));
        setOpponent(opponent);
        aiBot = this;
    }

    public void summonBestMonster() {
        int index = getBestMonsterCard(this.board.getHandCards());
        if (index != -1 && this.board.hasMonsterZoneSpace()) {
            MonsterCard monster = (MonsterCard) this.board.getHandCards().get(index);
            monster.setMode(Mode.ATTACK);
            int level = monster.getLevel();
            if (level > 6) {
                sacrificeWeakestMonster();
                sacrificeWeakestMonster();
            } else if (level > 4)
                sacrificeWeakestMonster();

            this.board.summonOrSetMonster(index);
        }
    }

    public void attack() {
        int opponentIndex;
        while ((opponentIndex = getOpponentMonsterIndexEasy(opponent)) != -1) {
            int monsterIndex = getBestMonsterToAttack(getAIMonsters());
            int opponentMonsterPower;
            int aiMonsterPower = getAIMonsters().get(monsterIndex).getAttackPoint();
            getAIMonsters().get(monsterIndex).setHasAttacked(true);
            if (getOpponentMonsters().get(opponentIndex).getMode() == Mode.ATTACK)
                opponentMonsterPower = getOpponentMonsters().get(opponentIndex).getAttackPoint();
            else
                opponentMonsterPower = getOpponentMonsters().get(opponentIndex).getDefensePoint();
            if (opponentMonsterPower > aiMonsterPower) {
                this.board.setLifePoints(this.board.getLifePoints() + aiMonsterPower - opponentMonsterPower);
                this.board.removeMonster(monsterIndex);
            } else if (opponentMonsterPower < aiMonsterPower) {
                if (getOpponentMonsters().get(opponentIndex).getMode() == Mode.ATTACK)
                    opponent.getPlayerBoard().setLifePoints(
                        opponent.getPlayerBoard().getLifePoints() + opponentMonsterPower - aiMonsterPower);
                opponent.getPlayerBoard().removeMonster(opponentIndex);
            } else {
                opponent.getPlayerBoard().removeMonster(opponentIndex);
                this.board.removeMonster(monsterIndex);
            }
        }
        while (getBestMonsterToAttack(getAIMonsters()) != -1 && canDirectAttack()) {
                int monsterIndex = getBestMonsterToAttack(getAIMonsters());
                int monsterPower = getAIMonsters().get(monsterIndex).getAttackPoint();
                getAIMonsters().get(monsterIndex).setHasAttacked(true);
                opponent.getPlayerBoard().setLifePoints(opponent.getPlayerBoard().getLifePoints() - monsterPower);
        }
    }

    public void checkSpellForActivate(GamePhase phase) {
        for (int i = 0; i < getAISpellTraps().size(); i++)
            if (isSpellReasonableToActive(getAISpellTraps().get(i).getName(),
                    phase) != ReasonableLevel.NOT_REASONABLE)
                activeSpellTrap(getAISpellTraps().get(i));
    }

    public void checkTrapForActivate() {
        for (int i = 0; i < getAISpellTraps().size(); i++)
            if (isTrapReasonableToActive(getAISpellTraps().get(i).getName()) != ReasonableLevel.NOT_REASONABLE)
                activeSpellTrap(getAISpellTraps().get(i));
    }

}

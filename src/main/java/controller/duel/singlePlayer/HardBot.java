package controller.duel.singlePlayer;

import models.Board;
import models.Deck;
import controller.duel.GamePhase;
import models.Player;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.Mode;

public class HardBot extends AI {

    public HardBot(Player opponent) throws CloneNotSupportedException {
        setName("HardBot");
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

            if (monster.getAttackPoint() < monster.getDefensePoint()) {
                this.board.getHandCards().get(index).setIsHidden(true);
                monster.setMode(Mode.DEFENSE);
            }
            this.board.summonOrSetMonster(index);
        }
    }

    public void attack() {
        int opponentIndex;
        while ((opponentIndex = getOpponentMonsterIndexHard(opponent)) != -1) {
            int monsterIndex = getBestMonsterToAttack(getAIMonsters());
            int opponentMonsterPower;
            int aiMonsterPower = getAIMonsters().get(monsterIndex).getAttackPoint();
            getAIMonsters().get(monsterIndex).setHasAttacked(true);
            if (opponent.getPlayerBoard().getMonsters().get(opponentIndex).getMode() == Mode.ATTACK)
                opponentMonsterPower = opponent.getPlayerBoard().getMonsters().get(opponentIndex).getAttackPoint();
            else
                opponentMonsterPower = opponent.getPlayerBoard().getMonsters().get(opponentIndex).getDefensePoint();
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
                    phase) == ReasonableLevel.REASONABLE_FOR_HARD)
                activeSpellTrap(getAISpellTraps().get(i));
    }

    public void checkTrapForActivate() {
        for (int i = 0; i < getAISpellTraps().size(); i++)
            if (isTrapReasonableToActive(getAISpellTraps().get(i).getName()) == ReasonableLevel.REASONABLE_FOR_HARD)
                activeSpellTrap(getAISpellTraps().get(i));
    }
}

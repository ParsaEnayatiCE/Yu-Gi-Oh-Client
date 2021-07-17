package view;

import controller.duel.*;
import controller.duel.singlePlayer.GameController;
import controller.menucontroller.CheatMenuController;
import view.menus.DuelMenu;

import java.util.regex.Matcher;

public class DuelView {

    public static boolean isMultiPlayer;
    public static int rounds, player1Wins = 0, player2Wins = 0, botWins = 0;
    private Matcher matcher;
    public static boolean shouldDrawBoard = true;

    boolean isCommandValid;
    SummonController summonController = new SummonController();
    SettingController settingController = new SettingController();
    ShowController showController = new ShowController();
    PhaseController phaseController = new PhaseController();
    GameController gameController = new GameController();
    SelectionController selectionController = new SelectionController();
    AttackController attackController = new AttackController();
    CheatMenuController cheatMenuController = new CheatMenuController();
    ActivationController activationController = new ActivationController();
    DuelMenu duelMenu = new DuelMenu();

    public void run(String command) {
        isCommandValid = false;
        //Selection
        selectMyMonster(command);
        selectRivalMonster(command);
        selectMySpell(command);
        selectRivalSpell(command);
        selectMyFieldCard(command);
        selectRivalFieldCard(command);
        selectMyHand(command);
        deSelect(command);
        //Summon
        summon(command);
        tributeSummon(command);
        flipSummon(command);
        specialSummon(command);
        ritualSummon(command);
        //setting
        set(command);
        setPosition(command);
        //attack
        attack(command);
        directAttack(command);
        //effects
        activate(command);
        activateOnMonster(command);
        equip(command);
        //phase
        switchPhase(command);
        //show
        showGraveyard(command);
        showCard(command);
        duelMenu.showMenu(command);
        duelMenu.changeMenu(command);
        //surrender
        surrender(command);
        //cheat
        if (!isCommandValid)
            System.out.println(cheatMenuController.run(command));
        //switch cards between games
        switchCards(command);
        if (shouldDrawBoard)
            if (isMultiPlayer)
                System.out.println(phaseController.printBoard());
            else
                System.out.println(gameController.printBoard());
    }

    private void showCard(String command) {
        if (!Regex.getMatcher(command, Regex.SHOW_SELECTED_CARD).find())
            return;
        isCommandValid = true;
        System.out.println(showController.showSelectedCard());
    }

    private void showGraveyard(String command) {
        if (!Regex.getMatcher(command, Regex.SHOW_GRAVEYARD).find())
            return;
        isCommandValid = true;
        System.out.println(showController.showGraveyard());
    }

    private void selectMyMonster(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.SELECT_OWN_MONSTER)).matches())
            return;
        isCommandValid = true;
        System.out.println(selectionController.selectMyMonster(matcher.group(2)));
    }

    private void selectRivalMonster(String command) {
        String monsterNum;
        if ((matcher = Regex.getMatcher(command, Regex.SELECT_OPPONENT_MONSTER_1)).matches()) {
            monsterNum = matcher.group(2);
            isCommandValid = true;
        } else if ((matcher = Regex.getMatcher(command, Regex.SELECT_OPPONENT_MONSTER_2)).matches()) {
            monsterNum = matcher.group(3);
            isCommandValid = true;
        }
        else return;
        System.out.println(selectionController.selectRivalMonster(monsterNum));
    }

    private void selectMySpell(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.SELECT_OWN_SPELL_CARD)).matches())
            return;
        isCommandValid = true;
        System.out.println(selectionController.selectMySpell(matcher.group(2)));
    }

    private void selectRivalSpell(String command) {
        String spellNum;
        if ((matcher = Regex.getMatcher(command, Regex.SELECT_OPPONENT_SPELL_CARD_1)).matches()) {
            spellNum = matcher.group(3);
            isCommandValid = true;
        } else if ((matcher = Regex.getMatcher(command, Regex.SELECT_OPPONENT_SPELL_CARD_2)).matches()) {
            spellNum = matcher.group(2);
            isCommandValid = true;
        }
        else return;
        System.out.println(selectionController.selectRivalSpell(spellNum));
    }

    private void selectMyFieldCard(String command) {
        if (!Regex.getMatcher(command, Regex.SELECT_OWN_FIELD).find())
            return;
        isCommandValid = true;
        System.out.println(selectionController.selectMyFieldCard());
    }

    private void selectRivalFieldCard(String command) {
        if (Regex.getMatcher(command, Regex.SELECT_OPPONENT_FIELD_1).find()
        || Regex.getMatcher(command, Regex.SELECT_OPPONENT_FIELD_2).find())
            isCommandValid = true;
        else return;
        System.out.println(selectionController.selectRivalFieldCard());
    }

    private void selectMyHand(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.SELECT_HAND_CARD)).matches())
            return;
        isCommandValid = true;
        System.out.println(selectionController.selectHandCard(matcher.group(2)));
    }

    private void deSelect(String command) {
        if (!Regex.getMatcher(command, Regex.DESELECT_CARD).find())
            return;
        isCommandValid = true;
        System.out.println(selectionController.deSelect());
    }

    private void summon(String command) {
        if (!Regex.getMatcher(command, Regex.SUMMON).find())
            return;
        isCommandValid = true;
        System.out.println(summonController.summon());
    }

    private void tributeSummon(String command) {
        Matcher matcher = Regex.getMatcher(command, Regex.TRIBUTE_SUMMON);
        if (!matcher.find())
            return;
        isCommandValid = true;
        System.out.println(summonController.tributeSummon(matcher.group(1)));
    }

    private void flipSummon(String command) {
        if (!Regex.getMatcher(command, Regex.FLIP_SUMMON).find())
            return;
        isCommandValid = true;
        System.out.println(summonController.flipSummon());
    }

    private void specialSummon(String command) {
        if (!Regex.getMatcher(command, Regex.SPECIAL_SUMMON).find())
            return;
        isCommandValid = true;
        System.out.println(summonController.specialSummon());
    }
    
    private void ritualSummon(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.RITUAL_SUMMON)).matches())
            return;
        isCommandValid = true;
        System.out.println(summonController.ritualSummon(matcher.group(1)));
    }

    private void set(String command) {
        if (!Regex.getMatcher(command, Regex.SET).find())
            return;
        isCommandValid = true;
        System.out.println(settingController.set());
    }

    private void setPosition(String command) {
        Matcher matcher = Regex.getMatcher(command, Regex.SET_CARD_POSITION);
        if (!matcher.find())
            return;
        isCommandValid = true;
        //System.out.println(settingController.setPosition(matcher.group(2)));
    }

    private void attack(String command) {
        Matcher matcher = Regex.getMatcher(command, Regex.ATTACK_MONSTER);
        if (!matcher.find())
            return;
        isCommandValid = true;
        System.out.println(attackController.attackMonsterToMonster(matcher.group(1)));
        //attackController.checkEndGame();
    }

    private void directAttack(String command) {
        if (!Regex.getMatcher(command, Regex.ATTACK_DIRECT).find())
            return;
        isCommandValid = true;
        System.out.println(attackController.directAttack());
        //attackController.checkEndGame();
    }

    private void activate(String command) {
        if (!Regex.getMatcher(command, Regex.ACTIVATE_EFFECT).find())
            return;
        isCommandValid = true;
        System.out.println(activationController.activate());
    }

    private void activateOnMonster(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.ACTIVATE_EFFECT_ON_MONSTER)).matches())
            return;
        isCommandValid = true;
        System.out.println(activationController.activateOnMonster(Integer.parseInt(matcher.group(1))));
    }

    private void equip(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.EQUIP_MONSTER)).matches())
            return;
        isCommandValid = true;
        System.out.println(activationController.equip(Integer.parseInt(matcher.group(1))));
    }

    private void switchPhase(String command) {
        if (!Regex.getMatcher(command, Regex.SWITCH_PHASE).find())
            return;
        isCommandValid = true;
        //if (isMultiPlayer)
          //  System.out.println(phaseController.changePhase());
        //else
          //  System.out.println(gameController.changePhase());
    }

    private void surrender(String command) {
        if (!Regex.getMatcher(command, Regex.SURRENDER).find())
            return;
        isCommandValid = true;
        //if (isMultiPlayer)
            //phaseController.endGame(PhaseController.playerAgainst, PhaseController.playerInTurn, event);
        //else
            //gameController.endGame("bot");
    }

    private void switchCards(String command) {
        if (!(matcher = Regex.getMatcher(command, Regex.SWITCH_CARDS)).matches())
            return;
        isCommandValid = true;
        if (isMultiPlayer)
            System.out.println(phaseController.switchCards(
                    Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
        else
            System.out.println("you can't switch in singlePlayer Mode");
    }
}

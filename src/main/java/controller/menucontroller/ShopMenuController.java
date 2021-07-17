package controller.menucontroller;


import models.User;
import models.cards.Card;
import models.cards.MakeCards;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;
import view.StatusEnum;

import java.io.IOException;
import java.util.ArrayList;


public class ShopMenuController {

    private final User currentUser;

    public ShopMenuController(User currentUser) {
        this.currentUser = currentUser;
    }

    public String buyCard(String cardName) throws IOException {
        Card cardDemo = getCardByName(cardName);
        if (cardDemo == null)
            return StatusEnum.INVALID_CARD_IN_SHOP.getStatus();

        if (!doesUserHaveEnoughMoney(cardDemo))
            return StatusEnum.NOT_ENOUGH_MONEY.getStatus();

        Card card = MakeCards.makeCard(cardName);
        currentUser.addCard(card);
        assert card != null;
        currentUser.setMoney(currentUser.getMoney() - card.getPrice());
        return StatusEnum.CARD_BOUGHT_SUCCESSFULLY.getStatus();
    }

    private boolean doesUserHaveEnoughMoney(Card card) {
        return currentUser.getMoney() >= card.getPrice();
    }

    private Card getCardByName(String cardName) throws IOException {
        ArrayList<MonsterCard> allMonsterCards = MonsterCard.getAllMonsterCardsToShow();
        ArrayList<SpellTrapCard> allSpellTrapCards = SpellTrapCard.getAllSpellTrapCardsToShow();

        for (MonsterCard monsterCard : allMonsterCards) {
            if (monsterCard.getName().equals(cardName))
                return monsterCard;
        }

        for (SpellTrapCard spellTrapCard : allSpellTrapCards) {
            if (spellTrapCard.getName().equals(cardName))
                return spellTrapCard;
        }

        return null;
    }

}

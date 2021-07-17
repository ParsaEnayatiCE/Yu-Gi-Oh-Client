package view;

import view.menus.*;

import java.io.IOException;
import java.util.Scanner;

public class ProgramController {
    public static MenuEnum currentMenu = MenuEnum.LOGIN_MENU;
    public static Scanner scanner = new Scanner(System.in);


    public void run() throws CloneNotSupportedException, IOException {
        while(currentMenu!= MenuEnum.EXIT){
            String command = scanner.nextLine();
            if (currentMenu == MenuEnum.LOGIN_MENU)
                new LoginMenu().run(command);
            else if(currentMenu == MenuEnum.MAIN_MENU)
                new MainMenu().run(command);
            else if (currentMenu == MenuEnum.SHOP_MENU)
                new ShopMenu().run(command);
            else if (currentMenu == MenuEnum.SCOREBOARD)
                new ScoreboardMenu().run(command);
            else if (currentMenu == MenuEnum.DECK_MENU)
                new DeckMenu().run(command);
            else if(currentMenu == MenuEnum.IMPORT_EXPORT)
                new ImportExportMenu().run(command);
            else if (currentMenu == MenuEnum.PROFILE_MENU)
                new ProfileMenu().run(command);
            else if (currentMenu == MenuEnum.DUEL_MENU)
                new DuelMenu().run(command);
            else if (currentMenu == MenuEnum.DUEL_VIEW)
                new DuelView().run(command);
        }
    }

}

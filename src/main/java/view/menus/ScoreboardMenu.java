package view.menus;


import view.MenuEnum;
import view.ProgramController;
import view.StatusEnum;
import models.User;
import view.Regex;

import java.util.ArrayList;


public class ScoreboardMenu {

    public ScoreboardMenu(){
    }

    public void run(String command){
        if ((Regex.getMatcher(command, Regex.SHOW_SCOREBOARD)).matches()){
            showScoreboard();
        }
        else if ((Regex.getMatcher(command, Regex.EXIT_MENU)).matches()){
           ProgramController.currentMenu = MenuEnum.MAIN_MENU;
        }
        else if ((Regex.getMatcher(command, Regex.ENTER_MENU)).matches()){
            System.out.println(StatusEnum.MENU_NAVIGATION_NOT_POSSIBLE.getStatus());
        }
        else if ((Regex.getMatcher(command, Regex.SHOW_CURRENT_MENU)).matches()){
            System.out.println("Scoreboard");
        }
        else{
            System.out.println(StatusEnum.INVALID_COMMAND.getStatus());
        }
    }
    private void showScoreboard(){
        ArrayList<User> sortedUsers = User.getSortedUsers();
        for (int i = 0, rank = 1; i < sortedUsers.size(); i++,rank++) {
            if (i > 0) {
                if (sortedUsers.get(i).getScore() == sortedUsers.get(i - 1).getScore())
                    rank--;
                else
                    rank = i + 1;
            }
            String nickName = sortedUsers.get(i).getNickName();
            int score = sortedUsers.get(i).getScore();
            System.out.println(rank+"-"+nickName+": "+score);
        }
    }
}

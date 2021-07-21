package controller.menucontroller;

import controller.ConnectionController;
import view.MenuEnum;
import view.ProgramController;
import view.StatusEnum;
import models.User;

import java.io.IOException;
import java.util.Objects;

//-----------------------------------PLEASE LOGIN FIRST NOT FIXED-------------------

public class LoginMenuController {
    public static User currentUser = null;
    private final ConnectionController connectionController = new ConnectionController();
    public static boolean isLoggedOn = false;
    private static String token = "";

    public static String getToken() {
        return token;
    }


    public String loginUSer(String username, String password) {
        String forServer = "login-"+username+"-"+password;
        try {
            ConnectionController.getDataOutputStream().writeUTF(forServer);
            ConnectionController.getDataOutputStream().flush();
            String serverResponse = ConnectionController.getDataInputStream().readUTF();
            String[] temp = serverResponse.split(" ");
            if (!serverResponse.equals(StatusEnum.USERNAME_AND_PASSWORD_MISMATCH.getStatus()) &&
            !temp[0].equals("There")){
                isLoggedOn = true;
                token = serverResponse;
                return StatusEnum.USER_LOGIN_SUCCESSFULLY.getStatus();
            }
            else {
                return serverResponse;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "Error";
    }

    public String createUser(String username, String nickname, String password) {
        String forServer = "register-"+username+"-"+nickname+"-"+password;
        try {
            connectionController.getDataOutputStream().writeUTF(forServer);
            connectionController.getDataOutputStream().flush();
            String serverResponse = connectionController.getDataInputStream().readUTF();
            return serverResponse;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "Error";
    }

}

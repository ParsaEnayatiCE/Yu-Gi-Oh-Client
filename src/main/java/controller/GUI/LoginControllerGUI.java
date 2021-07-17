package controller.GUI;


import controller.menucontroller.*;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import models.SaveData;
import view.GUI.AlertBox;
import view.StatusEnum;


import java.io.File;
import java.io.IOException;

public class LoginControllerGUI {

    public static MediaPlayer player;


    public TextField login_Username;
    public TextField loign_Password;
    public TextField register_username;
    public TextField register_nickname;
    public TextField register_pass;
    public ImageView sound;

    public static MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("./src/main/resources/sound/bt.mp3").toURI().toString()));


    public void initialize() {
        if (player.isMute()) {
            sound.setImage(new Image("./image/mute.jpg"));
        } else {
            sound.setImage(new Image("./image/unmute.jpg"));
        }
    }

    public void enterLoginMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/login_menu.fxml", actionEvent);
    }

    public void login(ActionEvent actionEvent) throws IOException {
        String name = login_Username.getText();
        String pass = loign_Password.getText();
        if (name.equals("") || pass.equals("")) {
            AlertBox.display("Please fill the empty fields!");
        } else {
            String res = new LoginMenuController().loginUSer(name, pass);

            if (res.equals("There is no user with username " + name)) {
                String msg = "There is no user with username " + name;
                AlertBox.display(msg);
            } else if (res.equals(StatusEnum.USERNAME_AND_PASSWORD_MISMATCH.getStatus())) {
                AlertBox.display(StatusEnum.USERNAME_AND_PASSWORD_MISMATCH.getStatus());
            } else if (res.equals(StatusEnum.USER_LOGIN_SUCCESSFULLY.getStatus())) {
                player.stop();
                String musicFile = "./src/main/resources/sound/main2.mp3";
                Media sound = new Media(new File(musicFile).toURI().toString());
                player = new MediaPlayer(sound);
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.play();
                SceneController.switchScene("/fxml/mainMenu.fxml", actionEvent);


            }
        }
    }

    public void enterRegisterMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/register_menu.fxml", actionEvent);
    }

    public void register() {
        String name = register_username.getText();
        String nick = register_nickname.getText();
        String pass = register_pass.getText();

        if (name.equals("") || pass.equals("") || nick.equals("")) {
            AlertBox.display("Please fill the empty fields!");
        } else {
            String res = new LoginMenuController().createUser(name, nick, pass);
            if (res.equals("user with username " + name + " already exists")) {
                AlertBox.display("user with username " + name + " already exists");
            } else if (res.equals("user with nickname " + nick + " already exists")) {
                AlertBox.display("user with nickname " + nick + " already exists");
            } else if (res.equals(StatusEnum.USER_CREATE_SUCCESSFULLY.getStatus())) {
                AlertBox.display(StatusEnum.USER_CREATE_SUCCESSFULLY.getStatus());
                register_username.clear();
                register_nickname.clear();
                register_pass.clear();
            }
        }

    }

    public void returnToStart(MouseEvent inputEvent) throws IOException {
        SceneController.switchSceneMouse("/fxml/start.fxml", inputEvent);
    }

    public void exit() {
        SaveData.save();
        System.exit(0);
    }

    public void muteAndUnmute() {
        if (player.isMute()) {
            player.setMute(false);
            sound.setImage(new Image("./image/unmute.jpg"));
        } else {
            player.setMute(true);
            sound.setImage(new Image("./image/mute.jpg"));
        }
    }

    public void highlightBt(MouseEvent event) {
        mediaPlayer = new MediaPlayer(new Media(new File("./src/main/resources/sound/bt.mp3").toURI().toString()));
        mediaPlayer.play();
        ImageView imageView = ((ImageView) event.getSource());
        double w = imageView.getFitWidth();
        double h = imageView.getFitHeight();
        imageView.setFitHeight(h + 5);
        imageView.setFitWidth(w + 5);


    }

    public void returnNormalBt(MouseEvent event) {
        ImageView imageView = ((ImageView) event.getSource());
        double w = imageView.getFitWidth();
        double h = imageView.getFitHeight();
        imageView.setFitHeight(h - 5);
        imageView.setFitWidth(w - 5);
    }

    public void creditScene(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/credit.fxml", actionEvent);
    }


}

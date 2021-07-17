package controller.GUI;

import controller.menucontroller.LoginMenuController;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.User;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardControllerGUI {

    @FXML
    private AnchorPane anchor;

    public void initialize() {
        showScoreboard(User.getSortedUsers());
    }

    public void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
    }

    public void showScoreboard(ArrayList<User> users) {
        TableView scoreboard = new TableView();
        scoreboard.setEditable(false);
        TableColumn rank = new TableColumn("Rank");
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rank.setResizable(false);
        rank.setSortable(false);
        rank.setPrefWidth(70);
        TableColumn nickname = new TableColumn("Nickname");
        nickname.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        nickname.setResizable(false);
        nickname.setSortable(false);
        nickname.setPrefWidth(200);
        TableColumn score = new TableColumn("Score");
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        score.setResizable(false);
        score.setSortable(false);
        score.setPrefWidth(100);
        scoreboard.getColumns().setAll(rank, nickname, score);
        scoreboard.getItems().setAll(users);
        scoreboard.setPrefWidth(rank.getPrefWidth() + nickname.getPrefWidth() + score.getPrefWidth());
        scoreboard.setPrefHeight(600);
        scoreboard.setLayoutX(400 - (scoreboard.getPrefWidth()/2));
        scoreboard.setLayoutY(0);
        scoreboard.setFixedCellSize(600/21.0);
        scoreboard.setSelectionModel(null);
        scoreboard.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;

            @Override
            public void onChanged(Change c) {
                c.next();
                if (c.wasReplaced() && !suspended) {
                    suspended = true;
                    scoreboard.getColumns().setAll(rank, nickname, score);
                    suspended = false;
                }
            }
        });

        anchor.getChildren().add(scoreboard);
        for (int i = 0 ; i < users.size(); i++)
            if ((nickname.getCellData(i)).equals(LoginMenuController.currentUser.getNickName())) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(scoreboard.getPrefWidth());
                rectangle.setHeight(scoreboard.getFixedCellSize());
                rectangle.setX(scoreboard.getLayoutX());
                rectangle.setY((600 * (i + 1) / 21.0) - 5);
                rectangle.setFill(Color.YELLOW);
                rectangle.setOpacity(0.35);
                anchor.getChildren().add(rectangle);
                break;
            }
    }
}

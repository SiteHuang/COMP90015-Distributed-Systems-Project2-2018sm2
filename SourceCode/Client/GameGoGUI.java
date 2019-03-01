package CLIENT;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameGoGUI {
    private static GameGoGUI gameGoGUI;
    private Scene sceneGo;
    private Button quickGame = new Button("QUICK GAME");
    private Button quitGame = new Button("QUIT");
    private Client client;
    private Text errorConnection = new Text();


    public static GameGoGUI getInstance(){
        if (gameGoGUI==null){
            gameGoGUI=new GameGoGUI();
        }
        return gameGoGUI;
    }

    public GameGoGUI(){}



    public void setClient(Client client){
        this.client = client;
    }

    public Scene getSceneGo(){
        return this.sceneGo;
    }

    public void gameGoInterface(Stage primaryStage){
        GridPane gridGo = new GridPane();
        VBox vBoxGameGo = new VBox();
        vBoxGameGo.setAlignment(Pos.BASELINE_CENTER);
        vBoxGameGo.setSpacing(120);
        gridGo.setAlignment(Pos.CENTER);
        gridGo.setHgap(15);
        gridGo.setVgap(5);
        gridGo.setPadding(new Insets(25, 25, 25, 25));

        final Text blank = new Text();
        vBoxGameGo.getChildren().add(blank);

        try{
            Image brandImg = new Image(GameGoGUI.class.getResourceAsStream("brand.png"));
            ImageView brandImgView = new ImageView();
            brandImgView.setImage(brandImg);
            brandImgView.setFitHeight(200);
            brandImgView.setFitWidth(900);
            vBoxGameGo.getChildren().add(brandImgView);
        }catch(Exception e){
            e.printStackTrace();
        }

        GridPane subGridClick = new GridPane();
        subGridClick.setHgap(10);
        subGridClick.setVgap(10);
        quickGame.setStyle("-fx-background-color: transparent;");
        quitGame.setStyle("-fx-background-color: transparent;");
        quickGame.setFont(Font.font("Arial", FontPosture.ITALIC,20));
        quitGame.setFont(Font.font("Arial",FontPosture.ITALIC,20));
        quickGame.setTextAlignment(TextAlignment.LEFT);
        quitGame.setTextAlignment(TextAlignment.LEFT);
        subGridClick.add(quickGame,1,0,1,1);
        subGridClick.add(quitGame,1,1,1,1);

        try{
            Image iconImg = new Image(GameGoGUI.class.getResourceAsStream("play-button.png"));
            ImageView quickGameImg = new ImageView();
            ImageView quitGameImg = new ImageView();
            quickGameImg.setImage(iconImg);
            quitGameImg.setImage(iconImg);
            quickGameImg.setFitHeight(30);
            quickGameImg.setFitWidth(30);
            quitGameImg.setFitHeight(30);
            quitGameImg.setFitWidth(30);
            subGridClick.add(quickGameImg,0,0,1,1);
            subGridClick.add(quitGameImg,0,1,1,1);
        }catch(Exception e){
            e.printStackTrace();
        }
        gridGo.add(subGridClick,0,1,1,1);
        vBoxGameGo.getChildren().add(gridGo);
        errorConnection.setFont(Font.font("chalkboard", FontWeight.THIN,18));

        sceneGo = new Scene(vBoxGameGo,1250,800);

        quickGame.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                quickGame.setTextFill(Color.GREEN);
            }
        });
        quickGame.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                quickGame.setTextFill(Color.BLACK);
            }
        });
        quickGame.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                GameMainGUI.getGameMainGUI().initGameMain(primaryStage,client);
                GameMainGUI.getGameMainGUI().gameMainInterface();
                GameMainGUI.getGameMainGUI().getSceneGameMain().getStylesheets().add(this.getClass().getResource("background.css").toExternalForm());
                GameMainGUI.getGameMainGUI().setWatch();
                GameMainGUI.getGameMainGUI().getWho().setText("WELCOME TO SCRABBLE");
                GameMainGUI.getGameMainGUI().getInviteList().clear();
                GameMainGUI.getGameMainGUI().getScores().setText("");
                ClientStart.setScene(primaryStage,GameMainGUI.getGameMainGUI().getSceneGameMain());
                client.quickGame();
            }
        });
        // quit game button event
        quitGame.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                quitGame.setTextFill(Color.ORANGERED);
            }
        });
        quitGame.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                quitGame.setTextFill(Color.BLACK);
            }
        });
        quitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameMainGUI.getGameMainGUI().clear();
                client.notConnected();
                System.exit(0);
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.notConnected();
                System.exit(0);
            }
        });

    }
}

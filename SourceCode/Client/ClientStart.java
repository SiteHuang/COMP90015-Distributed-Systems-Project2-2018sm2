package CLIENT;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientStart extends Application {

    private LoginGUI loginGUI;

    public ClientStart(){
        loginGUI = new LoginGUI();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Welcome");
        loginGUI.LoginInterface(primaryStage);
        loginGUI.getSceneLogin().getStylesheets().add(this.getClass().getResource("background.css").toExternalForm());

        setScene(primaryStage,loginGUI.getSceneLogin());
        primaryStage.show();

    }
    public static void setScene(Stage stage, Scene scene){
        stage.setScene(scene);
    }

}

package CLIENT;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginGUI {
    private Scene sceneLogin;

    private Button login = new Button("LOGIN");
    private Button cancel = new Button("CANCEL");
    private TextField userNameTF;
    private TextField portTF;
    private VBox vBoxLogin = new VBox();

    public Scene getSceneLogin(){
        return this.sceneLogin;
    }

    public void LoginInterface(Stage primaryStage){
        DropShadow shadow = new DropShadow();
        userNameTF = new TextField();
        portTF = new TextField();
        GridPane gridLogin = new GridPane();
        vBoxLogin.setAlignment(Pos.BASELINE_CENTER);
        vBoxLogin.setSpacing(120);
        gridLogin.setAlignment(Pos.CENTER);
        gridLogin.setHgap(10);
        gridLogin.setVgap(10);
        gridLogin.setPadding(new Insets(25, 25, 25, 25));

        final Text blank = new Text();
        vBoxLogin.getChildren().add(blank);

        try{
            Image brandImg = new Image(LoginGUI.class.getResourceAsStream("brand.png"));
            ImageView brandImgView = new ImageView();
            brandImgView.setImage(brandImg);
            brandImgView.setFitHeight(200);
            brandImgView.setFitWidth(900);
            vBoxLogin.getChildren().add(brandImgView);
        }catch(Exception e){
            e.printStackTrace();
        }

        Label address = new Label("Address");
        Label port = new Label("Port");
        address.setFont(Font.font("Arial", FontPosture.REGULAR,18));
        port.setFont(Font.font("Arial",FontPosture.REGULAR,18));
        address.setLabelFor(userNameTF);
        port.setLabelFor(portTF);
        gridLogin.add(address,0,1,1,1);
        gridLogin.add(port,0,2,1,1);
        gridLogin.add(userNameTF,1,1,1,1);
        gridLogin.add(portTF,1,2,1,1);

        HBox clickBox = new HBox();
        clickBox.setSpacing(10);
        clickBox.setAlignment(Pos.CENTER);
        login.setFont(Font.font("Arial",FontPosture.REGULAR,18));
        cancel.setFont(Font.font("Arial",FontPosture.REGULAR,18));
        clickBox.getChildren().add(login);
        clickBox.getChildren().add(cancel);
        gridLogin.add(clickBox,0,3,2,1);
        vBoxLogin.getChildren().add(gridLogin);

        login.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String host = null;
                int port = 0;
                Client client;
                try{
                    host = userNameTF.getText().trim();
                    port = Integer.valueOf(portTF.getText().trim());
                    if (!host.equals("") && port !=0){
                        userNameTF.setEffect(null);
                        portTF.setEffect(null);
                    }
                    client = new Client(host,port);
                    UsernameGUI.getInstance().initUsernameGUI(client);
                    UsernameGUI.getInstance().usernameInterface(primaryStage);
                    UsernameGUI.getInstance().getSceneUsername().getStylesheets().add(this.getClass().getResource("background.css").toExternalForm());
                    ClientStart.setScene(primaryStage,UsernameGUI.getInstance().getSceneUsername());
                }
                catch (NumberFormatException nfe) {
                    noticeInput(host, port);
                }
                catch (IOException ioe)
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Connection Refused");
                    alert.showAndWait();
                }
            }
            public void noticeInput(String host, int port){
                if (host.equals("")){
                    userNameTF.setEffect(shadow);
                }
                if (port==0){
                   portTF.setEffect(shadow);
                }
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });


        sceneLogin = new Scene(vBoxLogin,1250,800);
    }

}

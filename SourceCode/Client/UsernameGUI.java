package CLIENT;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class UsernameGUI {
    private static UsernameGUI usernameGUI;
    private Scene sceneUsername;
    private Button confirmUsername = new Button("CONFIRM");
    private Client client;
    private Text failureUsername = new Text();


    public static UsernameGUI getInstance(){
        if (usernameGUI==null){
            usernameGUI=new UsernameGUI();
        }
        return usernameGUI;
    }

    public void initUsernameGUI(Client client){
        this.client = client;
    }

    public UsernameGUI(){

    }

    public Text getFailureUsername(){
        return this.failureUsername;
    }

    public Scene getSceneUsername(){
        return this.sceneUsername;
    }

    public void usernameInterface(Stage primaryStage){

        VBox vBoxUsername = new VBox();
        vBoxUsername.setAlignment(Pos.BASELINE_CENTER);
        vBoxUsername.setSpacing(120);
        GridPane usernameGrid = new GridPane();
        usernameGrid.setAlignment(Pos.BASELINE_CENTER);
        usernameGrid.setVgap(5);
        usernameGrid.setHgap(5);

        final Text blank = new Text();
        vBoxUsername.getChildren().add(blank);

        try{
            Image brandImg = new Image(UsernameGUI.class.getResourceAsStream("brand.png"));
            ImageView brandImgView = new ImageView();
            brandImgView.setImage(brandImg);
            brandImgView.setFitHeight(200);
            brandImgView.setFitWidth(900);
            vBoxUsername.getChildren().add(brandImgView);
        }catch(Exception e){
            e.printStackTrace();
        }

        failureUsername.setFont(Font.font("Arial", FontWeight.THIN,18));

        Text titleUsername = new Text("Username");
        titleUsername.setFont(Font.font("chalkboard", FontPosture.ITALIC,25));
        titleUsername.setFill(Color.ROSYBROWN);

        TextField usernameInput = new TextField();
        usernameInput.setFont(Font.font("Arial",FontPosture.REGULAR,18));

        confirmUsername.setFont(Font.font("chalkboard",FontWeight.THIN,18));

        usernameGrid.add(failureUsername,0,0,2,1);
        usernameGrid.add(titleUsername,0,1,2,1);
        usernameGrid.add(usernameInput,0,2,1,1);
        usernameGrid.add(confirmUsername,1,2,1,1);

        vBoxUsername.getChildren().add(usernameGrid);



        confirmUsername.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                String input;
                if (!(input=usernameInput.getText().trim()).equals("")){
                    client.register(input);
                    while(ThreadLock.usernameLock){
                        // dead
                        confirmUsername.setDisable(true);
                    }
                    confirmUsername.setDisable(false);

                    if (ThreadLock.usernameSucc){
                        GameGoGUI.getInstance().setClient(client);
                        GameGoGUI.getInstance().gameGoInterface(primaryStage);
                        GameGoGUI.getInstance().getSceneGo().getStylesheets().add(this.getClass().getResource("background.css").toExternalForm());
                        ClientStart.setScene(primaryStage,GameGoGUI.getInstance().getSceneGo());
                    } else{
                        failureUsername.setText(ThreadLock.errorMsg);
                    }
                }
                else {
                    ThreadLock.errorMsg = "Username cannot be empty";
                    failureUsername.setText(ThreadLock.errorMsg);
                }
            }
        });

        sceneUsername = new Scene(vBoxUsername,1250,800);
    }
}

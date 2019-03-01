package CLIENT;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Invitee {
    private SimpleStringProperty UserName;
    private Button button;
    private String name;



    public Invitee(String fName,Client client,String username){
        this.name = username;
        this.UserName = new SimpleStringProperty(fName);
        this.button = new Button("Invite");
        this.button.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED
                , new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        button.setDisable(true);
                        client.invite(getUserName());
                    }
                });
    }
    public void setGuestBtnDis(){
        this.button.setDisable(true);
    }
    public void resetBtn(){
        this.button.setDisable(false);
    }

    public String getUserName() {
        return UserName.get();
    }

    public Button getButton(){
        return this.button;
    }
    public String getName() {
        return this.name;
    }

}

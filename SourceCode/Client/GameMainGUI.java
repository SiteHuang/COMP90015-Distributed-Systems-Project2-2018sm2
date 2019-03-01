package CLIENT;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class GameMainGUI {
    private static GameMainGUI gameMainGUI;
    private Scene sceneGameMain;
    private Label[][] tile = new Label[20][20];
    private static boolean[][] selectedTile = new boolean[20][20];
    private Text challengeWord1 = new Text("Click Here To Challenge");
    private static Label[] handTiles = new Label[26];
    private static Button confirm = new Button("SEND");
    private static Button voting = new Button("VOTING");
    private static Button undo = new Button("UNDO");
    private Button pass = new Button("PASS");
    private Button endGame = new Button("END GAME");
    private static int round = 1;
    private static boolean success = false;
    private static int position_x, position_y;
    private Button challengeBtn1 = new Button("CHALLENGE");
    private Button play = new Button("P\nL\nA\nY");
    private Button acceptBtn = new Button();
    private Button declineBtn = new Button();
    private final TableView<Invitee> table = new TableView<>();
    private ObservableList<Invitee> inviteList = FXCollections.observableArrayList();
    private Stage primaryStage;
    private Client client;
    private boolean ifChallenge1= true;
    private Text scores = new Text();
    private Text who = new Text("WELCOME TO SCRABBLE");
    private boolean gameState = false;
    private Text invitationMsg = new Text();
    private static boolean ackInvite = false;
    private Text noticeSlected = new Text("Select Before Voting\nClick SEND to Continue");


    public static GameMainGUI getGameMainGUI(){
        if (gameMainGUI==null){
            gameMainGUI=new GameMainGUI();
        }
        return gameMainGUI;
    }

    public void clear(){
        if (gameMainGUI!=null){
            gameMainGUI = null;
        }
    }

    public GameMainGUI(){

    }

    public void resetHighlight(){
        for (int i=0; i<tile.length;i++){
            for (int j=0; j<tile[i].length; j++){
                tile[i][j].setStyle("-fx-background-color: white;" +
                        "-fx-border-width: 0.2;" +
                        "-fx-border-insets: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: black;");
            }
        }
    }

    public Text getWho(){
        return this.who;
    }
    public boolean getGameState(){
        return this.gameState;
    }
    public void setRound(){
        round=1;
    }
    public Label[][] getTile(){
        return this.tile;
    }
    public Text getScores(){
        return this.scores;
    }
    public void setChallengeWord1(){
        this.ifChallenge1=true;
    }
    public Text getChallengeWord1(){
        return this.challengeWord1;
    }
    public boolean getIfChallengeWord1(){
        return this.ifChallenge1;
    }
    public void lockChallengeBtn(){
        this.challengeBtn1.setDisable(true);
    }
    public void unLockChallengeBtn(){
        this.challengeBtn1.setDisable(false);
    }
    public void setGameState(boolean gameState){
        this.gameState = gameState;
    }
    public ObservableList<Invitee> getInviteList() {return this.inviteList;}
    public Text getInvitationMsg(){
        return this.invitationMsg;
    }
    public Button getAcceptBtn(){
        return this.acceptBtn;
    }
    public Button getDeclineBtn(){
        return this.declineBtn;
    }
    public void setAcceptBtn(){
        this.acceptBtn.setDisable(false);
    }
    public void setDeclineBtn(){
        this.declineBtn.setDisable(false);
    }
    public boolean getAckInvite(){
        return this.ackInvite;
    }
    public void setAckInvite(){ ackInvite=false; }
    public TableView<Invitee> getTable(){
        return this.table;
    }
    public Button getPlay(){
        return this.play;
    }



    public void setWatch(){
        confirm.setDisable(true);
        voting.setDisable(true);
        undo.setDisable(true);
        pass.setDisable(true);
        challengeBtn1.setDisable(true);
        for (Label handTile: handTiles){
            handTile.setDisable(true);
        }
        acceptBtn.setVisible(false);
        declineBtn.setVisible(false);
        play.setDisable(true);
    }
    public void setUnWatch(){
        pass.setDisable(false);
        for (Label handTile: handTiles){
            handTile.setDisable(false);
        }
    }

    public void initGameMain(Stage primaryStage, Client client){
        this.primaryStage = primaryStage;
        this.client = client;
    }

    public Scene getSceneGameMain(){
        return sceneGameMain;
    }

    public void gameMainInterface(){
        new HeartBeats(client).start();
        GridPane gameMainGrid = new GridPane();
        gameMainGrid.setAlignment(Pos.BASELINE_CENTER);
        gameMainGrid.setHgap(35);
        gameMainGrid.setVgap(15);
        gameMainGrid.setPadding(new Insets(25, 25, 25, 25));

//        TOP MIDDLE -- WHO's turn
        who.setFont(Font.font("chalkboard", FontPosture.ITALIC,35));
        who.setTextAlignment(TextAlignment.LEFT);
        gameMainGrid.add(who,1,0,2,1);

//        TOP RIGHT -- VOTING
        GridPane subGridVoting = new GridPane();
        subGridVoting.setHgap(10);
        subGridVoting.setVgap(15);

        Text votingTitle = new Text("VOTING");
        votingTitle.setFont(Font.font("chalkboard", FontPosture.ITALIC,25));
        votingTitle.setFill(Color.GREY);
        votingTitle.setTextAlignment(TextAlignment.CENTER);

        challengeWord1.setFont(Font.font("Arial", FontPosture.REGULAR,20));
        challengeWord1.setFill(Color.SKYBLUE);
        challengeWord1.setTextAlignment(TextAlignment.CENTER);
        challengeBtn1.setStyle("-fx-background-color: transparent");
        challengeBtn1.setFont(Font.font("Arial",FontPosture.ITALIC,20));
        try{
            Image chalIconImg = new Image(GameMainGUI.class.getResourceAsStream("decline.png"));
            ImageView challImg = new ImageView();
            challImg.setImage(chalIconImg);
            challImg.setFitHeight(30);
            challImg.setFitWidth(30);

            subGridVoting.add(challImg,0,2,1,1);
        }catch(Exception e){
            e.printStackTrace();
        }


        setTopRightBtn();

        subGridVoting.add(votingTitle,0,0,2,1);
        subGridVoting.add(challengeWord1,0,1,2,1);
        subGridVoting.add(challengeBtn1,1,2,1,1);
        gameMainGrid.add(subGridVoting,2,1,1,5);

//        BOTTOM RIGHT -- BUTTONS
        GridPane subGridButtons = new GridPane();
        subGridButtons.setHgap(10);
        subGridButtons.setVgap(8);
        confirm.setStyle("-fx-background-color: transparent;");
        voting.setStyle("-fx-background-color: transparent;");
        undo.setStyle("-fx-background-color: transparent;");
        pass.setStyle("-fx-background-color: transparent;");
        endGame.setStyle("-fx-background-color: transparent;");
        confirm.setFont(Font.font("Arial",FontPosture.ITALIC,17));
        voting.setFont(Font.font("Arial",FontPosture.ITALIC,17));
        undo.setFont(Font.font("Arial",FontPosture.ITALIC,20));
        pass.setFont(Font.font("Arial",FontPosture.ITALIC,20));
        endGame.setFont(Font.font("Arial",FontPosture.ITALIC,20));
        confirm.setTextAlignment(TextAlignment.LEFT);
        voting.setTextAlignment(TextAlignment.LEFT);
        undo.setTextAlignment(TextAlignment.LEFT);
        pass.setTextAlignment(TextAlignment.LEFT);
        endGame.setTextAlignment(TextAlignment.LEFT);

        noticeSlected.setFont(Font.font("chalkboard",FontPosture.REGULAR,16));
        noticeSlected.setTextAlignment(TextAlignment.LEFT);
        noticeSlected.setFill(Color.ROSYBROWN);
        VBox confirmVBox = new VBox();
        confirmVBox.setAlignment(Pos.CENTER);
        HBox confirmHbox = new HBox();
        confirmHbox.setAlignment(Pos.BASELINE_CENTER);
        confirmHbox.setSpacing(3);
        confirmHbox.setStyle("-fx-background-color: transparent;");
        confirmHbox.getChildren().addAll(voting,confirm);
        confirmVBox.getChildren().addAll(noticeSlected,confirmHbox);

        TitledPane confirmPane = new TitledPane("CONFIRM",confirmVBox);
        confirmPane.setExpanded(false);
        confirmPane.setStyle("-fx-background-color: transparent;");
        confirmPane.setFont(Font.font("chalkboard",FontPosture.ITALIC,20));
        confirmPane.setAlignment(Pos.BASELINE_LEFT);
        confirmPane.minWidth(300);

        setBotRightBtn();
        subGridButtons.add(confirmPane,1,0,1,1);
        subGridButtons.add(undo,1,1,1,1);
        subGridButtons.add(pass,1,2,1,1);
        subGridButtons.add(endGame,1,3,1,1);
        try{

            Image confirmIconImg = new Image(GameMainGUI.class.getResourceAsStream("confirm.png"));
            Image undoIconImg = new Image(GameMainGUI.class.getResourceAsStream("undo.png"));
            Image passIconImg = new Image(GameMainGUI.class.getResourceAsStream("pass.png"));
            Image endgameIconImg = new Image(GameMainGUI.class.getResourceAsStream("endgame.png"));

            ImageView confirmImg = new ImageView();
            ImageView undoImg = new ImageView();
            ImageView passImg = new ImageView();
            ImageView endgameImg = new ImageView();

            confirmImg.setImage(confirmIconImg);
            undoImg.setImage(undoIconImg);
            passImg.setImage(passIconImg);
            endgameImg.setImage(endgameIconImg);

            confirmImg.setFitHeight(30);
            confirmImg.setFitWidth(30);
            undoImg.setFitHeight(30);
            undoImg.setFitWidth(30);
            passImg.setFitHeight(30);
            passImg.setFitWidth(30);
            endgameImg.setFitHeight(30);
            endgameImg.setFitWidth(30);

            subGridButtons.add(confirmImg,0,0,1,1);
            subGridButtons.add(undoImg,0,1,1,1);
            subGridButtons.add(passImg,0,2,1,1);
            subGridButtons.add(endgameImg,0,3,1,1);
        }catch(Exception e){
            e.printStackTrace();
        }

        gameMainGrid.add(subGridButtons,2,6,1,1);

//        BOTTOM LEFT -- SCORES
        scores.setFont(Font.font("chalkboard",FontPosture.ITALIC,20));
        scores.setTextAlignment(TextAlignment.LEFT);
        VBox scoreVBox = new VBox();
        scoreVBox.setSpacing(20);
        scoreVBox.setMaxWidth(200);
        try{
            ImageView separateLineImg = new ImageView();
            separateLineImg.setImage(new Image(GameMainGUI.class.getResourceAsStream("Hline.png")));
            separateLineImg.setFitWidth(350);
            scoreVBox.getChildren().add(separateLineImg);
        }catch (Exception e){
            e.printStackTrace();
        }
        scoreVBox.getChildren().add(scores);
        gameMainGrid.add(scoreVBox,0,5,1,6);

//        MIDDLE LEFT -- invitation
        HBox hBoxAckBtn = new HBox();
        VBox invitationVBox = new VBox();
        hBoxAckBtn.setPadding(new Insets(0,30,0,0));
        invitationVBox.setAlignment(Pos.BASELINE_CENTER);
        invitationVBox.setSpacing(8);
        hBoxAckBtn.setAlignment(Pos.BASELINE_RIGHT);
        hBoxAckBtn.setSpacing(15);

        acceptBtn.setId("accept");
        acceptBtn.setPrefSize(27,27);

        declineBtn.setId("decline");
        declineBtn.setPrefSize(27,27);
        hBoxAckBtn.getChildren().addAll(acceptBtn,declineBtn);
        setMiddleLeftBtn();

        invitationMsg.setFill(Color.BROWN);
        invitationMsg.setFont(Font.font("Arial",FontPosture.REGULAR,18));
        invitationVBox.getChildren().addAll(invitationMsg,hBoxAckBtn);

        gameMainGrid.add(invitationVBox,0,4,1,1);


//        TOP LEFT - INVITE LIST
        HBox inviteHBox = new HBox();
        inviteHBox.setSpacing(10);
        inviteHBox.setAlignment(Pos.BOTTOM_RIGHT);
        // invite table
        table.setEditable(false);
        table.setMaxWidth(200);
        table.setStyle("-fx-background-color: transparent");
        TableColumn online = new TableColumn("Online");
        online.setMinWidth(130);
        online.setCellValueFactory(new PropertyValueFactory<Invitee, String>("UserName"));

        TableColumn actionCol = new TableColumn("Invite");
        actionCol.setMinWidth(60);
        actionCol.setCellValueFactory(new PropertyValueFactory<Invitee, String>("button"));

        table.getColumns().addAll(online, actionCol);


        play.setFont(Font.font("chalkboard",FontPosture.ITALIC,20));
        play.setStyle("-fx-background-color: transparent");
        setTopLeftBtn();
        inviteHBox.getChildren().addAll(table,play);


        gameMainGrid.add(inviteHBox,0,0,1,3);


//        MEDIUM MIDDLE -- GAME INTERFACE
        GridPane subGrid = new GridPane();

        String[] indexLetter = new String[]{"A","B","C","D","E","F","G","H","I","J"
                ,"K","L","M","N","O","P","Q","R","S","T"};
        Text cornerIndex = new Text("@");
        Text[] HIndices = new Text[20];
        Text[] VIndices = new Text[20];
        HBox[] HBoxVIndices = new HBox[20];
        HBox[] HBoxHIndices = new HBox[20];
        HBox HBoxCorner = new HBox();

        cornerIndex.setFont(Font.font("chalkboard",FontWeight.SEMI_BOLD,15));
        HBoxCorner.getChildren().add(cornerIndex);
        HBoxCorner.setAlignment(Pos.CENTER);
        HBoxCorner.setMinSize(25,25);
        HBoxCorner.setStyle("-fx-background-color: ghostwhite;"+
                "-fx-opacity: 0.6");
        subGrid.add(HBoxCorner,0,0,1,1);
        for (int i=0; i<HIndices.length; i++){
            Text newHIndex = new Text(indexLetter[i]);
            HIndices[i] = newHIndex;
            HIndices[i].setFont(Font.font("chalkboard",FontWeight.SEMI_BOLD,15));
            HBox hBoxHIndex = new HBox();
            HBoxHIndices[i] = hBoxHIndex;
            HBoxHIndices[i].getChildren().add(HIndices[i]);
            HBoxHIndices[i].setStyle("-fx-background-color: ghostwhite;"+
                    "-fx-opacity: 0.6");
            HBoxHIndices[i].setMinSize(25,25);
            HBoxHIndices[i].setAlignment(Pos.CENTER);
            subGrid.add(HBoxHIndices[i],i+1,0,1,1);
        }
        for (int i=0; i<VIndices.length; i++){
            Text newVIndex = new Text(String.valueOf(i+1));
            VIndices[i] = newVIndex;
            VIndices[i].setFont(Font.font("chalkboard",FontWeight.SEMI_BOLD,15));
            HBox hBoxVIndex = new HBox();
            HBoxVIndices[i] = hBoxVIndex;
            HBoxVIndices[i].getChildren().add(VIndices[i]);
            HBoxVIndices[i].setStyle("-fx-background-color: ghostwhite;"+
                    "-fx-opacity: 0.6");
            HBoxVIndices[i].setMinSize(25,25);
            HBoxVIndices[i].setAlignment(Pos.CENTER);
            subGrid.add(HBoxVIndices[i],0,i+1,1,1);
        }

        for (int i=0; i<tile.length;i++){
            for (int j=0; j<tile[i].length; j++){
                Label newTile = new Label();
                tile[i][j] = newTile;
                tile[i][j].setFont(Font.font("Arial",FontPosture.REGULAR,20));
                tile[i][j].setMaxSize(30,40);
                tile[i][j].setAlignment(Pos.CENTER);
                tile[i][j].setStyle("-fx-background-color: white;" +
                        "-fx-border-width: 0.2;" +
                        "-fx-border-insets: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: black;");
                subGrid.add(tile[i][j],j+1,i+1,1,1);

            }
        }

        gameMainGrid.add(subGrid,1,1,1,10);


//        BOTTOM MIDDLE - TILES IN HAND
        GridPane handGrid = new GridPane();
        handGrid.setAlignment(Pos.CENTER);
        handGrid.setHgap(10);
        handGrid.setVgap(10);
        String[] initHandTiles = new String[]{
                "A","B","C","D","E","F","G","H","I","J","K","L","M",
                "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        for (int i=0; i<handTiles.length; i++){
            Label newHandLabel = new Label(initHandTiles[i]);
            handTiles[i]=newHandLabel;
            handTiles[i].setFont(Font.font("Arial",FontWeight.THIN,1));
            handTiles[i].setId(String.valueOf(i));
            handTiles[i].setMinSize(25,25);

            if (i<13) handGrid.add(handTiles[i],i,0,1,1);
            else handGrid.add(handTiles[i],i-13,1,1,1);
        }

        gameMainGrid.add(handGrid,1,11,1,1);

        // dragging effects
        String[] dragFile = new String[26];
        dragFile[0]= "yeah.jpg";
        dragFile[1]= "smirk.jpg";
        dragFile[2]= "smart.png";
        dragFile[3]= "kiss.png";
        dragFile[4]= "hey.png";
        dragFile[5]= "facepalm.jpg";
        dragFile[6]= "smirk.jpg";
        dragFile[7]= "yeah.jpg";
        dragFile[8]= "smirk.jpg";
        dragFile[9]= "smart.png";
        dragFile[10]= "kiss.png";
        dragFile[11]= "hey.png";
        dragFile[12]= "facepalm.jpg";
        dragFile[13]= "yeah.jpg";
        dragFile[14]= "smirk.jpg";
        dragFile[15]= "smart.png";
        dragFile[16]= "kiss.png";
        dragFile[17]= "hey.png";
        dragFile[18]= "facepalm.jpg";
        dragFile[19]= "yeah.jpg";
        dragFile[20]= "smirk.jpg";
        dragFile[21]= "smart.png";
        dragFile[22]= "kiss.png";
        dragFile[23]= "hey.png";
        dragFile[24]= "facepalm.jpg";
        dragFile[25]= "smirk.jpg";


        // set dragging operations for each hand tile
        for (int i=0; i<handTiles.length; i++){
            handleHandTileDrag(handTiles[i],dragFile[i]);
        }
        // set dragging effects for each tile
        for (int i=0; i<tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {
                boolean selected = false;
                selectedTile[i][j] = selected;
                handleTileDrag(tile,i,j);
            }
        }
        sceneGameMain = new Scene(gameMainGrid,1250,800);

    }

    public static void handleHandTileDrag(Label handTile, String dragImg){
        handTile.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard dragboard = handTile.startDragAndDrop(TransferMode.ANY);
                try{
                    Image image = new Image(GameMainGUI.class.getResourceAsStream(dragImg));
                    dragboard.setDragView(image);
                }catch(Exception e){
                    e.printStackTrace();
                }
                ClipboardContent content = new ClipboardContent();
                content.putString(handTile.getText());
                dragboard.setContent(content);
                event.consume();
            }

        });
        handTile.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
//                    handTile.setText("");
                }
                event.consume();
            }
        });
    }

    public static void handleTileDrag(Label[][] tile, int i, int j){
        tile[i][j].setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                /* accept it only if it is not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != tile[i][j] &&
                        event.getDragboard().hasString()) {
                    /* allow for moving */
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            }
        });
        tile[i][j].setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != tile[i][j] &&
                        event.getDragboard().hasString()) {
                    tile[i][j].setStyle("-fx-background-color: red;" +
                            "-fx-border-width: 0.2;" +
                            "-fx-border-insets: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-border-color: red;");
                }

                event.consume();
            }
        });
        tile[i][j].setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                tile[i][j].setStyle("-fx-background-color: white;" +
                        "-fx-border-width: 0.2;" +
                        "-fx-border-insets: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: black;");
                event.consume();
            }
        });

        //Dai edit
        tile[i][j].setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                success=false;
                if (db.hasString() ) {

                    if(round == 1){
                        tile[i][j].setText(db.getString());// add if
                        tile[i][j].setTextFill(Color.BLACK);
                        success = true;

                    }else if(tile[i][j].getText().equals("")){
                        for (int number = -1;number<2;number++){
                            if(i+number >0 && i +number <tile.length){
                                if(tile[i+number][j].getText() != ""){
                                    tile[i][j].setText(db.getString());
                                    tile[i][j].setTextFill(Color.BLACK);
                                    success = true;
                                }
                            }
                            if(j+number >0 && j +number <20){
                                if(tile[i][j+number].getText() != ""){
                                    tile[i][j].setText(db.getString());
                                    tile[i][j].setTextFill(Color.BLACK);
                                    success = true;
                                }
                            }
                        }
                    }
                }

                /* let the source know whether the string was successfully
                 * transferred and used */
                if(success){
                    position_x = i;
                    position_y = j;
                    for(int value =0;value <26;value++){
                        handTiles[value].setDisable(true);
                    }
                    confirm.setDisable(false);
                    voting.setDisable(false);
                    undo.setDisable(false);
                    round++;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        tile[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!selectedTile[i][j]){
                    tile[i][j].setStyle("-fx-background-color: lightgray");
                    selectedTile[i][j] = true;
                }
                else{
                    tile[i][j].setStyle("-fx-background-color: white;" +
                            "-fx-border-width: 0.2;" +
                            "-fx-border-insets: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-border-color: black;");
                    selectedTile[i][j] = false;
                }
            }
        });
    }

    public void setBotRightBtn(){
        // confirm button operation
        confirm.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                confirm.setTextFill(Color.GREEN);
            }
        });
        confirm.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                confirm.setTextFill(Color.BLACK);
            }
        });
        confirm.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(success) {
                    String horizontal_word = tile[position_x][position_y].getText();
                    String vertical_word = tile[position_x][position_y].getText();
                    for (int i = 1;position_x+i<20;i++){
                        if(tile[position_x+i][position_y].getText() =="" ){
                            break;
                        }
                        horizontal_word = horizontal_word + tile[position_x+i][position_y].getText();
                    }
                    for (int i = 1;position_y+i<20;i++){
                        if(tile[position_x][position_y+i].getText() =="" ){
                            break;
                        }
                        vertical_word = vertical_word + tile[position_x][position_y+i].getText();
                    }
                    for (int i = 1;position_x-i>=0;i++){
                        if(tile[position_x-i][position_y].getText() =="" ){
                            break;
                        }
                        horizontal_word =tile[position_x-i][position_y].getText() +  horizontal_word;
                    }
                    for (int i = 1;position_y-i>=0;i++){
                        if(tile[position_x][position_y-i].getText() =="" ){
                            break;
                        }
                        vertical_word = tile[position_x][position_y-i].getText() + vertical_word;
                    }

                    client.setLayout(position_x,position_y,tile[position_x][position_y].getText());
                    client.setRow(position_x);
                    client.setColumn(position_y);
                    client.setChange(tile[position_x][position_y].getText());
                    client.confirm();
                    voting.setDisable(true);
                    confirm.setDisable(true);
                    undo.setDisable(true);

                }
            }
        });
        voting.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                voting.setTextFill(Color.GREEN);
            }
        });
        voting.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                voting.setTextFill(Color.BLACK);
            }
        });
        voting.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(success) {
                    boolean[][] availableSelected = new boolean[20][20];
                    for (int i=0;i<20;i++){
                        for (int j=0;j<20;j++){
                            availableSelected[i][j] = false;
                        }
                    }
                    boolean[][] rowSelected = new boolean[20][20];
                    for (int i=0;i<20;i++){
                        for (int j=0;j<20;j++){
                            availableSelected[i][j] = false;
                        }
                    }
                    boolean[][] colSelected = new boolean[20][20];
                    for (int i=0;i<20;i++){
                        for (int j=0;j<20;j++){
                            availableSelected[i][j] = false;
                        }
                    }
                    String horizontal_word = tile[position_x][position_y].getText();
                    String vertical_word = tile[position_x][position_y].getText();
                    availableSelected[position_x][position_y]=true;
                    for (int i = 1;position_x+i<20;i++){
                        if(tile[position_x+i][position_y].getText() =="" ){
                            break;
                        }
                        horizontal_word = horizontal_word + tile[position_x+i][position_y].getText();
                        availableSelected[position_x+i][position_y]=true;
                    }
                    for (int i = 1;position_y+i<20;i++){
                        if(tile[position_x][position_y+i].getText() =="" ){
                            break;
                        }
                        vertical_word = vertical_word + tile[position_x][position_y+i].getText();
                        availableSelected[position_x][position_y+i]=true;
                    }
                    for (int i = 1;position_x-i>=0;i++){
                        if(tile[position_x-i][position_y].getText() =="" ){
                            break;
                        }
                        horizontal_word =tile[position_x-i][position_y].getText() +  horizontal_word;
                        availableSelected[position_x-i][position_y]=true;

                    }
                    for (int i = 1;position_y-i>=0;i++){
                        if(tile[position_x][position_y-i].getText() =="" ){
                            break;
                        }
                        vertical_word = tile[position_x][position_y-i].getText() + vertical_word;
                        availableSelected[position_x][position_y-i]=true;
                    }

                    for (int j=0; j<20;j++){
                        if (availableSelected[position_x][j]) {
                            rowSelected[position_x][j] = availableSelected[position_x][j];
                        }
                    }
                    for (int i=0; i<20; i++){
                        if (availableSelected[i][position_y]) {
                            colSelected[i][position_y] = availableSelected[i][position_y];
                        }
                    }

                    boolean reselect1 = false;
                    boolean reselect2 = false;
                    for (int i=0; i<20; i++){
                        for (int j=0; j<20;j++){
                            if (selectedTile[i][j]!=rowSelected[i][j]) {
                                reselect1 = true;
                            }
                        }
                    }
                    for (int i=0; i<20; i++){
                        for (int j=0; j<20;j++){
                            if (selectedTile[i][j]!=colSelected[i][j]) {
                                reselect2 = true;
                            }
                        }
                    }

                    if (reselect1 && reselect2){
                        noticeSlected.setText("Invalid Selection");
                        for (int i=0; i<tile.length;i++){
                            for (int j=0; j<tile[i].length; j++){
                                tile[i][j].setStyle("-fx-background-color: white;" +
                                        "-fx-border-width: 0.2;" +
                                        "-fx-border-insets: 1;" +
                                        "-fx-border-radius: 5;" +
                                        "-fx-border-color: black;");
                            }
                        }
                        for (int i=0; i<20; i++){
                            for (int j=0; j<20;j++){
                                selectedTile[i][j]=false;
                            }
                        }
                    }else{
                        String grid_highlight_code = "";
                        for (int i = 0;i <20;i++){
                            for (int j = 0;j<20;j++) {
                                if (!selectedTile[i][j]) {
                                    grid_highlight_code = grid_highlight_code + "#";
                                } else {
                                    grid_highlight_code = grid_highlight_code + "T";
                                }
                            }
                        }
                        String votingWord;
                        if (!reselect1){
                            votingWord = vertical_word;
                        }else{
                            votingWord = horizontal_word;
                        }
                        client.setLayout(position_x,position_y,tile[position_x][position_y].getText());
                        client.setRow(position_x);
                        client.setColumn(position_y);
                        client.setChange(tile[position_x][position_y].getText());
                        client.voting(grid_highlight_code,votingWord);
                        voting.setDisable(true);
                        confirm.setDisable(true);
                        undo.setDisable(true);
                    }

                }
            }
        });


        // undo button operation
        undo.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                undo.setTextFill(Color.ORANGERED);
            }
        });
        undo.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                undo.setTextFill(Color.BLACK);
            }
        });
        undo.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if(success){
                    tile[position_x][position_y].setText("");
                    success = false;
                    for(int value =0;value <26;value++){
                        handTiles[value].setDisable(false);
                    }
                    confirm.setDisable(true);
                    voting.setDisable(true);
                    undo.setDisable(true);
                    round --;
                }
            }
        });
        // pass button operation
        pass.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pass.setTextFill(Color.DEEPSKYBLUE);
            }
        });
        pass.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pass.setTextFill(Color.BLACK);
            }
        });
        pass.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                client.pass();
            }
        });

        endGame.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                endGame.setTextFill(Color.ROSYBROWN);
            }
        });
        endGame.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                endGame.setTextFill(Color.BLACK);
            }
        });
        endGame.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ClientStart.setScene(primaryStage,GameGoGUI.getInstance().getSceneGo());
//                clear();
                client.exit();
            }
        });
    }

    public void setTopRightBtn(){
        DropShadow shadow = new DropShadow();
        // challenge button2 operation
        challengeBtn1.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                challengeBtn1.setTextFill(Color.CORAL);
            }
        });
        challengeBtn1.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                challengeBtn1.setTextFill(Color.BLACK);
            }
        });
        challengeBtn1.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ifChallenge1=false;
                challengeBtn1.setDisable(true);
            }
        });
    }

    public void setTopLeftBtn(){
        play.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                play.setTextFill(Color.CORAL);
            }
        });
        play.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                play.setTextFill(Color.BLACK);
            }
        });
        play.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                client.startGame();
                List<Invitee> invitees = InviteesList.getInstance().getInvitees();
                for (Invitee invitee: invitees){
                    invitee.setGuestBtnDis();
                }
                gameState=true;
                play.setDisable(true);
            }
        });
    }

    public void setMiddleLeftBtn(){
        DropShadow shadow = new DropShadow();
        // challenge button2 operation
        acceptBtn.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                acceptBtn.setEffect(shadow);
            }
        });
        acceptBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                acceptBtn.setEffect(null);
            }
        });
        acceptBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ackInvite=true;
                acceptBtn.setDisable(true);
            }
        });
        // challenge button1 operation
        declineBtn.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                declineBtn.setEffect(shadow);
            }
        });
        declineBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                declineBtn.setEffect(null);
            }
        });
        declineBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                ackInvite=false;
                declineBtn.setDisable(true);
            }
        });
    }

    public void updateTile(String[][] updateTile){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<20; i++){
                    for (int j=0; j<20; j++){
                        String text = updateTile[i][j];
                        if (text.equals("#")){
                            GameMainGUI.getGameMainGUI().getTile()[i][j].setText("");
                        }else{
                            GameMainGUI.getGameMainGUI().getTile()[i][j].setText(text);
                        }
                    }
                }
            }
        });
    }

    public void updateTileHightlight(String[][] updateHightlight){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<20; i++){
                    for (int j=0; j<20; j++){
                        String hightLight = updateHightlight[i][j];
                        if (hightLight.equals("T")){
                            selectedTile[i][j]=true;
                            GameMainGUI.getGameMainGUI().getTile()[i][j]
                                    .setStyle("-fx-background-color: lightgray");
                        }
                    }
                }
            }
        });
    }

}

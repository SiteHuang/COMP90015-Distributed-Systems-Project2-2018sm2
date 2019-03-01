package CLIENT;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class MessageListener extends Thread {
    private DataInputStream reader;
    private Client client;


    public MessageListener(DataInputStream reader,Client client)
    {
        this.reader=reader;
        this.client=client;

    }

    @Override
    public void run() {
        try
        {
            while(true)
            {
                if(reader.available()>0) {
                    JSONParser parser = new JSONParser();
                    JSONObject command = (JSONObject) parser.parse(reader.readUTF());
                    processData(command);
                }
            }

        }
        catch (IOException e)
        {
            System.out.println("There is IOException ! ");
        }
        catch (ParseException e)
        {
            System.out.println("There is parse exception of the Message Listener class ! ");
        }

    }
    public void processData(JSONObject command)
    {
        switch (command.get("command").toString())
        {
            case "r_register":
            {
               //invoke a method to open the game
                boolean mask=(boolean)command.get("mask");
                String username=command.get("username").toString();
                if(mask) {
                    client.setUsername(username);
                    ThreadLock.usernameLock=false;
                    ThreadLock.usernameSucc=true;
                } else{
                    ThreadLock.usernameLock=false;
                    ThreadLock.errorMsg = "The username is duplicated";
                    ThreadLock.usernameSucc = false;
                }
               break;
            }


            case "update":
            {
                String result=command.get("list").toString();
                String hostname=command.get("hostname").toString();
                String[] inviteList = result.split("#");
                ObservableList<Invitee> inviteListGUI = FXCollections.observableArrayList();
                InviteesList.getInstance().removeInvitee();
                if (client.getUsername().equals(hostname)){
                    for (String invitee: inviteList){
                        Invitee newInvitee = new Invitee(invitee,client,invitee);
                        newInvitee.resetBtn();
                        if (invitee.equals(hostname)){
                            newInvitee.setGuestBtnDis();
                        }
                        InviteesList.getInstance().addInvitee(newInvitee);
                        inviteListGUI.add(newInvitee);
                        GameMainGUI.getGameMainGUI().getPlay().setDisable(false);
                    }
                } else{
                    for (String invitee: inviteList){
                        Invitee newInvitee = new Invitee(invitee,client,invitee);
                        InviteesList.getInstance().addInvitee(newInvitee);
                        newInvitee.setGuestBtnDis();
                        inviteListGUI.add(newInvitee);
                    }
                }
                if (GameMainGUI.getGameMainGUI().getGameState()){
                    List<Invitee> invitees = InviteesList.getInstance().getInvitees();
                    for (Invitee invitee: invitees){
                        invitee.setGuestBtnDis();
                    }
                }
                GameMainGUI.getGameMainGUI().getTable().setItems(inviteListGUI);
                break;
            }

            case "invitation":
            {
                String invite_from=command.get("invite_from").toString();
                GameMainGUI.getGameMainGUI().getInvitationMsg()
                        .setText(invite_from.toUpperCase()+" invites you to join a game.");
                GameMainGUI.getGameMainGUI().getAcceptBtn().setVisible(true);
                GameMainGUI.getGameMainGUI().getDeclineBtn().setVisible(true);
                long start=System.currentTimeMillis();
                while((System.currentTimeMillis()-start)<5000)
                {
                    System.out.print("");
                }
                client.rplyofInvitation(GameMainGUI.getGameMainGUI().getAckInvite());
                GameMainGUI.getGameMainGUI().setAckInvite();
                GameMainGUI.getGameMainGUI().setAcceptBtn();
                GameMainGUI.getGameMainGUI().setDeclineBtn();
                GameMainGUI.getGameMainGUI().getAcceptBtn().setVisible(false);
                GameMainGUI.getGameMainGUI().getDeclineBtn().setVisible(false);
                break;
            }


            case "updateScores":
            {

                String result=command.get("result").toString();
                result = result.replaceAll("#","\n");
                GameMainGUI.getGameMainGUI().getScores().setText(result);
                break;
            }


            case "isyourturn":
            {
                String username=command.get("username").toString();
                GameMainGUI.getGameMainGUI().getWho().setText(username.toUpperCase()+"'s turn");
                if(client.getUsername().equals(username))
                {
                    GameMainGUI.getGameMainGUI().getWho().setFill(Color.GREEN);
                    GameMainGUI.getGameMainGUI().setUnWatch();
                }else{
                    GameMainGUI.getGameMainGUI().getWho().setFill(Color.ORANGERED);
                    GameMainGUI.getGameMainGUI().setWatch();
                }
                break;
            }
            case "confirm":
            {
                String grid=command.get("layout").toString();
                int k=0;
                for(int i=0;i<20;i++)
                {
                    for(int j=0;j<20;j++)
                    {
                        this.client.setLayout(i,j,String.valueOf(grid.toCharArray()[k++]));
                    }
                }

                GameMainGUI.getGameMainGUI().updateTile(client.getLayout());

                break;
            }

            case "voting":
            {
                String grid=command.get("layout").toString();
                String highlight = command.get("highlight").toString();
                int k=0;
                int m=0;
                String[][] highlightGrid=new String[20][20];
                for(int i=0;i<20;i++)
                {
                    for(int j=0;j<20;j++)
                    {
                        this.client.setLayout(i,j,String.valueOf(grid.toCharArray()[k++]));
                        highlightGrid[i][j]=String.valueOf(highlight.toCharArray()[m++]);

                    }
                }

                GameMainGUI.getGameMainGUI().updateTile(client.getLayout());
                GameMainGUI.getGameMainGUI().updateTileHightlight(highlightGrid);

                String word1=command.get("word").toString();

                GameMainGUI.getGameMainGUI().getChallengeWord1().setText(word1);
                String name = command.get("username").toString();
                if (!name.equals(client.getUsername())){
                    GameMainGUI.getGameMainGUI().unLockChallengeBtn();

                }
                long start=System.currentTimeMillis();
                while((System.currentTimeMillis()-start)<5000)
                {
                    System.out.print("");
                }

                client.rplyofChallenge(word1,GameMainGUI.getGameMainGUI().getIfChallengeWord1());

                GameMainGUI.getGameMainGUI().lockChallengeBtn();
                GameMainGUI.getGameMainGUI().setChallengeWord1();
                GameMainGUI.getGameMainGUI().resetHighlight();
                break;
            }

            case "gameState":
            {
                boolean gameState =Boolean.parseBoolean(command.get("gameState").toString());
                GameMainGUI.getGameMainGUI().setGameState(gameState);
                break;
            }

            case "allpass":
            {
                //end the game.
                String endGame = "All players have passed the turn ! Game Over ! ";
                GameMainGUI.getGameMainGUI().getWho().setText(endGame);
                GameMainGUI.getGameMainGUI().setWatch();
                GameMainGUI.getGameMainGUI().setRound();
//                GameMainGUI.getGameMainGUI().clear();
                break;
            }

            case "someoneexit":
            {
                //end the game.
                String endGame = "Someone Quit Game. Game Over!";
                GameMainGUI.getGameMainGUI().getWho().setText(endGame);
                GameMainGUI.getGameMainGUI().setWatch();
                GameMainGUI.getGameMainGUI().setRound();
//                GameMainGUI.getGameMainGUI().clear();
                break;
            }


        }
    }
}

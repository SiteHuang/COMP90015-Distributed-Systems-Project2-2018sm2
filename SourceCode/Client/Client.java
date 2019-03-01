package CLIENT;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.simple.*;

public class Client {
    Socket socket;
    private String username;
    private String[][]layout=new String[20][20];
    private int row;
    private int column;
    private String change;
    private DataOutputStream output;
    private DataInputStream input;


    public String getUsername() {
        return username;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getChange() {
        return change;
    }

    public void setLayout(int m,int n,String change) {
        this.layout[m][n]=change;
    }

    public String[][] getLayout(){
        return this.layout;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public Client(String host, int port)
            throws IOException
    {
        socket = new Socket(host,port);
        input=new DataInputStream(socket.getInputStream());
        output=new DataOutputStream(socket.getOutputStream());
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                layout[i][j] = "";

        MessageListener getMessage=new MessageListener(input,this);
        getMessage.start();
    }


    public void register(String name)
    {
        try{
            JSONObject send = new JSONObject();
            send.put("command", "register");
            send.put("username",name);
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("The connection of the socket failed ! ");
        }

    }


    public void quickGame()
    {
        try{
            JSONObject send = new JSONObject();
            send.put("command", "quickgame");
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("The connection of the socket failed ! ");
        }
    }


    public void invite(String inviteName)
    {
        try {
            //This is the logic of putting the info into the json object.
            JSONObject send = new JSONObject();
            send.put("command", "invite");
            String players;
            players=inviteName;
            send.put("username",players);
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("There is something wrong with the JSON sending message ! ");
        }
    }

    public void confirm()
    {
        try {
            //This is the logic of putting the info into the json object.
            JSONObject send = new JSONObject();
            send.put("command", "confirm");
            send.put("row", getRow());
            send.put("column", getColumn());
            send.put("value", getChange());
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("There is something wrong with the JSON sending message ! ");
        }
    }

    public void voting(String highLight,String votingWord){
        try {
            //This is the logic of putting the info into the json object.
            JSONObject send = new JSONObject();
            send.put("command", "voting");
            send.put("row", getRow());
            send.put("column", getColumn());
            send.put("value", getChange());
            send.put("highlight",highLight);
            send.put("word",votingWord);
            output.writeUTF(send.toJSONString());
            output.flush();


        }
        catch (IOException e)
        {
            System.out.println("There is something wrong with the JSON sending message ! ");
        }
    }
    public String transferToString(){
            String grid_code = "";
            for (int i = 0;i <20;i++){
                for (int j = 0;j<20;j++) {
                    if (getLayout()[i][j].equals("")) {
                        grid_code = grid_code + "#";
                    } else {
                        grid_code = grid_code + getLayout()[i][j];
                    }
                }
            }
            return grid_code;
    }


    public void rplyofChallenge(String word1,boolean challenge1)
    {
        try {
            JSONObject send = new JSONObject();
            send.put("command", "challenge");
            send.put("word1",word1);
            send.put("challenge1", challenge1);
            send.put("layout",transferToString());
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("There is IO exception of send_rply_of_challenge ! ");
        }
    }

    public void pass()
    {
        try {
            JSONObject send = new JSONObject();
            send.put("command", "pass");
            send.put("layout",transferToString());
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("There is exception of pass ! ");
        }

    }

    public void exit()
    {
        try {
            JSONObject send = new JSONObject();
            send.put("command", "exit");
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            System.out.println("There is exception of exit ! ");
        }
    }

    public void startGame()
    {
        try
        {
            JSONObject send=new JSONObject();
            send.put("command","start");
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //This code is to test the logic of the game .
    public void printLay()
    {
        for(int i=0;i<20;i++)
        {
            for(int j=0;j<20;j++)
                System.out.print(layout[i][j]);
            System.out.println();

        }
    }

    public void sendHeartBeats()
            throws IOException
    {

            JSONObject sendHeart = new JSONObject();
            sendHeart.put("command", "isConnected");
            output.writeUTF(sendHeart.toJSONString());
            output.flush();
    }

    public void notConnected(){
        try {
            JSONObject send = new JSONObject();
            send.put("command", "notConnected");
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e){
            System.out.println("There is something wrong with the JSON sending message ! ");
        }
    }

    public void rplyofInvitation(boolean agree){
        try {
            JSONObject send = new JSONObject();
            send.put("command", "replyinvitation");
            if (agree) {
                send.put("agree", true);
            } else {
                send.put("agree", false);
            }
            output.writeUTF(send.toJSONString());
            output.flush();
        }
        catch (IOException e){
            System.out.println("There is something wrong with the JSON sending message ! ");
        }
    }
}

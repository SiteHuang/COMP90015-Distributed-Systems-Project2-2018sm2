package CLIENT;

import java.io.IOException;

/*
* this file can be used to judge the connection with the server
* or receive the message from the server
* */
public class HeartBeats extends Thread {
    //private BufferedReader reader;
    private Client client;
    public HeartBeats(Client client)
    {
        this.client = client;
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                Thread.sleep(3 * 1000);//send a heartbeat package every 3 seconds
                client.sendHeartBeats();
            } catch (IOException se) {
                // if disconnected, showing a notice to inform user
                GameMainGUI.getGameMainGUI().getWho().setText("Can't connect to the server! Please try it again later.");
                break;
            } catch (InterruptedException ite){
                System.out.println("InterruptedException: "+ ite);
            }
        }
    }
}

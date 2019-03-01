package scrabbleServer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.simple.JSONObject;

//manages client connections
public class ClientManager {

	private static ClientManager clientInstance;

	//users pool
	private List<Connection> connectedClients;
	
	//players pool
	private List<Connection> inGameClients;

	private List<InGame> inProcessGames;
	public static ArrayList<String> UserList = new ArrayList<String>();
	public static ArrayList<String> PlayerList = new ArrayList<String>();

	private ClientManager() {
		connectedClients = new ArrayList<Connection>();
		inGameClients = new ArrayList<Connection>();
		inProcessGames = new ArrayList<InGame>();
	}

	//broadcast json message to all the clients in the users pool
	public void broadcastToAllClients(JSONObject j){
		for(Connection c : connectedClients) {
			c.write(j);
		}
	}

	//clear the players pool
	public void clearInGameList() {
		inGameClients.clear();
		PlayerList.clear();
	}
	
	//broadcast json message to all the players in the players pool
	public void broadcastToInGameClients(JSONObject j){
		for(Connection c : connectedClients) {
			c.write(j);
		}
	}

	public String printAllClients() {
		String s = "";
		for (Connection c : connectedClients) {
			s = s+c.getClientName()+"#";
		}
		return s;
	}

	public static synchronized ClientManager getClientInstance() {
		if (clientInstance == null) {
			clientInstance = new ClientManager();
		}
		return clientInstance;
	}

	//one user connects to the server
	public synchronized void clientConnected(Connection clientConnection) {
		connectedClients.add(clientConnection);
	}

	//one user closes the client
	public synchronized void clientDisconnected(Connection clientConnection) {
		connectedClients.remove(clientConnection);
	}

	//gets the list of all the connected clients
	public synchronized List<Connection> getConnectedClients() {
		return connectedClients;
	}

	//one user joins the game
	public synchronized void clientJoinGame(Connection clientConnection) {
		inGameClients.add(clientConnection);
	}

	//one user quits the game
	public synchronized void clientQuitGame(Connection clientConnection) {
		inGameClients.remove(clientConnection);
	}

	//gets the list of players in the game 
	public synchronized List<Connection> getInGameClients() {
		return inGameClients;
	}

	public synchronized void gameConnected(InGame game) {
		inProcessGames.add(game);
	}

	public synchronized void clientDisconnected(InGame game) {
		inProcessGames.remove(game);
	}

	public synchronized List<InGame> getConnectedGames() {
		return inProcessGames;
	}

	public synchronized ArrayList<String> getUserList() {
		return UserList;
	}

	public synchronized ArrayList<String> getPlayerList() {
		return PlayerList;
	}

}	




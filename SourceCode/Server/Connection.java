package scrabbleServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.StrictMath.max;

import javax.net.ServerSocketFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Each connection between client and server
public class Connection extends Thread{

	private JSONParser parser;
	private DataInputStream jsonInput;
	private  DataOutputStream jsonOutput;

	private int clientNum;
	private String clientName ="not set";
	private int score = 0;
	private boolean ifIsMyTurn = false;
	private String currentMsg = "nothing";

	private Socket clientSocket;

	public Connection(Socket clientSocket, int clientNum){
		try{

			// The JSON Parser
			parser = new JSONParser();
			// Input stream
			jsonInput = new DataInputStream(clientSocket.getInputStream());
			// Output Stream
			jsonOutput = new DataOutputStream(clientSocket.getOutputStream());

			this.clientSocket = clientSocket;
			this.clientNum = clientNum;


		} 
		catch (IOException  e) {
			e.printStackTrace();
		}
	}

	@Override
	//Any code executed within this method will be part of the same execution thread, even
	//if that means invoking methods declared on a class that extends the Thread class.
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName() 
					+ " - Reading messages from client's " + clientNum + " connection");
			while (true) {
				if (jsonInput.available() > 0){

					// read the messages from the client
					String clientMsg = null;
					JSONObject command = (JSONObject) parser.parse(jsonInput.readUTF());
					clientMsg = command.get("command").toString();

					if (!(this.currentMsg.equals(command.toJSONString()) )) {
						this.currentMsg = command.toJSONString();

						if (!clientMsg.equals("isConnected")) {
							System.out.println("receive from "+this.getClientName()+" "+command.toJSONString());
						}

						//different cases: the message received from the client
						switch (clientMsg){

						//registers the username
						case "register":
							String name = command.get("username").toString();

							//checks if the username has existed in the user pool
							checkRepetitiveName(name);
							break;

							//initites a game
						case "quickgame":
							if(ClientManager.getClientInstance().getInGameClients().size()==0) {

								System.out.println(this.getClientName()+" initites a game");
								ClientManager.getClientInstance().clientJoinGame(this);
								ClientManager.PlayerList.add(this.clientName);

								Server.gameStatus = new InGame();
								Server.gameStatus.updatePlayerList();

								Server.hostName =this.clientName;
								setScore(0);
								JSONObject jso = new JSONObject();
								jso.put("command","update");
								jso.put("list", ClientManager.getClientInstance().printAllClients());
								jso.put("hostname",Server.hostName);
								write(jso);
							}
							else {
								JSONObject jso = new JSONObject();
								jso.put("command","notHost");
								//		System.out.println(jso.toJSONString());
								write(jso);
								JSONObject js1 = new JSONObject();
								js1.put("command","watch");
								write(js1);
							}
							break;

							//invites other players
						case "invite":
							if (Server.hostName.equals(this.clientName)){
								String invitedName = command.get("username").toString();
								if (!invitedName.equals(this.clientName)) {
									inviting(invitedName);
								}
							}

							break;

							//replys to an invition
						case "replyinvitation":
							boolean inviteResult = (boolean)command.get("agree");
							if (inviteResult) {
								ClientManager.getClientInstance().clientJoinGame(this);
								ClientManager.PlayerList.add(this.clientName);
								Server.gameStatus.updatePlayerList();
								setScore(0);
								JSONObject json = new JSONObject();
								Server.gameStatus.UpdatePlayerScore(this.clientName,0);
								json.put("command","updateScores");
								json.put("result", Server.gameStatus.scoreList());
								System.out.println(json.toJSONString());
								ClientManager.getClientInstance().broadcastToAllClients(json);

							}
							break;

							// starts the game
						case "start":
							if (ClientManager.PlayerList.contains(this.clientName)) {
								Server.gameStatus.updatePlayerList();
								JSONObject js2 = new JSONObject();
								js2.put("command","isyourturn");
								js2.put("username", this.clientName);
								jsonOutput.writeUTF(js2.toJSONString());
								jsonOutput.flush();
							}
							break;

							//confirms the move, but not asks for a vote
						case "confirm":	
							if (ClientManager.PlayerList.contains(this.clientName)) {
								Server.gameStatus.setPassTurnNumber(0);
								int x = Integer.parseInt(command.get("row").toString());
								int y = Integer.parseInt(command.get("column").toString());
								String value = command.get("value").toString();
								Server.gameStatus.Update(x,y,value);

								command.put("layout", Server.gameStatus.getGameGridStatus());
								ClientManager.getClientInstance().broadcastToAllClients(command);

								Server.gameStatus.setConfirmPlayer(this);

								Connection next_client = Server.gameStatus.FindNext(this.clientName);
								Server.gameStatus.setTurn(next_client);

								JSONObject js6 = new JSONObject();
								js6.put("command","isyourturn");
								js6.put("username", Server.gameStatus.getTurn().getClientName());
								ClientManager.getClientInstance().broadcastToAllClients(js6);
							}
							break;

							//asks for a vote
						case "voting" :

							if (ClientManager.PlayerList.contains(this.clientName)) {
								Server.gameStatus.setPassTurnNumber(0);
								int x = Integer.parseInt(command.get("row").toString());
								int y = Integer.parseInt(command.get("column").toString());
								String value = command.get("value").toString();
								Server.gameStatus.Update(x,y,value);
								command.put("layout", Server.gameStatus.getGameGridStatus());
								command.put("username",this.clientName);
								ClientManager.getClientInstance().broadcastToAllClients(command);

								Server.gameStatus.setConfirmPlayer(this);

								Connection next_client = Server.gameStatus.FindNext(this.clientName);
								Server.gameStatus.setTurn(next_client);
							}
							break;

							//agrees or does not agree with the word
						case "challenge":

							if (ClientManager.PlayerList.contains(this.clientName)) {
								Server.gameStatus.updateChallengeNum(1);
								boolean a = (boolean)command.get("challenge1");
								if (!a){
									Server.gameStatus.challengeA(true);
									System.out.println("test1111");
								}

								if (Server.gameStatus.getChallengeNum() ==Server.gameStatus.getNumOfPlayer()) {
									System.out.println("test2222");
									if(!Server.gameStatus.AIsChallenged()) {
										System.out.println("test3333");
										int ascore = command.get("word1").toString().length();
										Server.gameStatus.UpdatePlayerScore(Server.gameStatus.getConfirmPlayer().getClientName(),ascore);
									}
									System.out.println("test44444");
									JSONObject js3 = new JSONObject();
									js3.put("command","updateScores");
									js3.put("result", Server.gameStatus.scoreList());
									ClientManager.getClientInstance().broadcastToAllClients(js3);

									JSONObject js4 = new JSONObject();
									js4.put("command","isyourturn");
									js4.put("username", Server.gameStatus.getTurn().getClientName());
									ClientManager.getClientInstance().broadcastToAllClients(js4);
									Server.gameStatus.challengeA(false);
									Server.gameStatus.setChallengeNum(0);
								}	
							}
							break;

							//passes this turn
						case "pass":

							if (ClientManager.PlayerList.contains(this.clientName)) {
								pass(command);
							}

							break;

							//exits the game
						case "exit":
							if (ClientManager.PlayerList.contains(this.clientName)) {
								JSONObject js6 = new JSONObject();
								js6.put("command", "someoneexit");
								ClientManager.getClientInstance().broadcastToAllClients(js6);
								ClientManager.getClientInstance().clearInGameList();
								Server.hostName = "";
							}
							break;

						case "isConnected":
							break;
							//closes the client 
						case "notConnected":
							System.out.println("someone not connected");
							System.out.println(clientName+"not connected");
							if(!ClientManager.PlayerList.contains(this.clientName)) {
								System.out.println("exception io :not connected1");
								ClientManager.UserList.remove(clientName);

							}
							else {
								System.out.println("exception io2: not connected2");
								JSONObject js6 = new JSONObject();
								js6.put("command", "someoneexit");
								ClientManager.getClientInstance().broadcastToAllClients(js6);
								ClientManager.getClientInstance().clearInGameList();
								Server.hostName = "";
								ClientManager.UserList.remove(this.clientName);
								ClientManager.PlayerList.remove(this.clientName);
							}
							ClientManager.getClientInstance().clientDisconnected(this);	
						}						
					}
				}
			}
		}catch (SocketException socketException) {
			if(!ClientManager.PlayerList.contains(this.clientName)) {
				System.out.println("exception 1");
				ClientManager.UserList.remove(clientName);
			}
			else {
				System.out.println("exception 2");
				JSONObject js6 = new JSONObject();
				js6.put("command", "someoneexit");
				ClientManager.getClientInstance().broadcastToAllClients(js6);
				ClientManager.getClientInstance().clearInGameList();
				Server.hostName = "";
				ClientManager.UserList.remove(clientName);
				ClientManager.PlayerList.remove(clientName);
			}
			ClientManager.getClientInstance().clientDisconnected(this);	   
		}catch (EOFException e){
			System.out.println("exception 3");
			ClientManager.getClientInstance().clientDisconnected(this);
			ClientManager.UserList.remove(clientName);
			ClientManager.PlayerList.remove(clientName);
		}catch(IOException e) {
			System.out.println("sdad");
			System.out.println(clientName+"sdahsdhakdhja");
			if(!ClientManager.PlayerList.contains(this.clientName)) {
				System.out.println("exception io1");
				ClientManager.UserList.remove(clientName);

			}
			else {
				System.out.println("exception io2");
				JSONObject js6 = new JSONObject();
				js6.put("command", "someoneexit");
				ClientManager.getClientInstance().broadcastToAllClients(js6);
				ClientManager.getClientInstance().clearInGameList();
				Server.hostName = "";
				ClientManager.UserList.remove(this.clientName);
				ClientManager.PlayerList.remove(this.clientName);
			}
			ClientManager.getClientInstance().clientDisconnected(this);	

		}
		catch (Exception e){
			System.out.println("exception 4");
			e.printStackTrace();
		}
	}	

	// invites someone
	public void inviting(String name) throws IOException {
		for(Connection cc : ClientManager.getClientInstance().getConnectedClients()){
			if(cc.getClientName().equals(name)){
				JSONObject js = new JSONObject();
				js.put("command","invitation");
				js.put("invite_from", this.getClientName());
				cc.write(js);
			}
		}
	}

	//transmits the json object to the clients
	synchronized void write(JSONObject j) {
		try {
			jsonOutput.writeUTF(j.toJSONString());
			System.out.println("send to "+this.getClientName()+" "+j.toJSONString());
			jsonOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getClientNum() {
		return clientNum;
	}

	public void setClientNum(int clientNum) {
		this.clientNum = clientNum;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void UpdateScore(int i){
		this.score = score + i;
	}

	public boolean isMyTurn() {
		return ifIsMyTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.ifIsMyTurn = myTurn;
	}

	//check if the username has existed in the user pool
	private void checkRepetitiveName(String name) throws IOException {

		JSONObject js = new JSONObject();

		if (!ClientManager.UserList.contains(name)){

			setClientName(name);
			ClientManager.UserList.add(name);
			ClientManager.getClientInstance().clientConnected(this);

			js.put("command","r_register");
			js.put("mask",true);
			js.put("username", name);
			jsonOutput.writeUTF(js.toJSONString());
			jsonOutput.flush();

			JSONObject js2 = new JSONObject();
			js2.put("command","update");
			js2.put("list", ClientManager.getClientInstance().printAllClients());
			js2.put("hostname",Server.hostName);
			ClientManager.getClientInstance().broadcastToAllClients(js2);

		}else{
			js.put("command","r_register");
			js.put("mask",false);
			js.put("username", name);
			jsonOutput.writeUTF(js.toJSONString());
			jsonOutput.flush();
		}
	}

	//passes the turn and checks if all the players have passed
	private void pass(JSONObject jso){
		Server.gameStatus.pass();
		JSONObject j = new JSONObject();
		switch (Server.gameStatus.passResult()){
		case "GameEnd":
			JSONObject js3 = new JSONObject();
			js3.put("command", "allpass");
			ClientManager.getClientInstance().broadcastToAllClients(js3);
			ClientManager.getClientInstance().clearInGameList();
			Server.hostName = "";
			break;
		case "Continue":
			Connection next_client = Server.gameStatus.FindNext(this.clientName);
			Server.gameStatus.setTurn(next_client);			
			JSONObject json2 = new JSONObject();
			json2.put("command","isyourturn");
			json2.put("username", Server.gameStatus.getTurn().getClientName());
			ClientManager.getClientInstance().broadcastToAllClients(json2);
			break;
		}
	}
}


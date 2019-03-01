package scrabbleServer;

import javafx.scene.control.Label;
import scrabbleServer.Connection;
import scrabbleServer.ClientManager;
import scrabbleServer.Server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONObject;

//Game Status and Game Logics
public class InGame{

	// the game grid
	private static String[][] grid = new String[20][20];

	//whose turn 
	private Connection turn;

	private Connection confirmPlayer;	
	private int challengenum = 0;
	private boolean A_is_challenged = false;
	private static int MAX = 5;

	private int numOfPlayer = 0;
	private Connection[] playerList = new Connection[MAX];

	private int votingNum = 0;
	private int spaceRemain = 400;
	private int turnNum = 0;
	private int passTurnNumber = 0;
	private int passNum = 0;
	private int totalTurn = 0;
	private String currentCommand = "Not Set";
	private String currentClient = "Not Set";

	public Connection getTurn(){
		return turn;
	}

	public void setTurn(Connection turn) {
		this.turn = turn;
	}

	public Connection getConfirmPlayer(){
		return confirmPlayer;
	}

	public void setConfirmPlayer(Connection confirmPlayer) {
		this.confirmPlayer = confirmPlayer;
	}

	public int getChallengeNum() {
		return challengenum;
	}

	public void updateChallengeNum(int i) {
		this.challengenum = this.challengenum + i;
	}

	public void challengeA(boolean boo) {
		this.A_is_challenged = boo;
	}

	public boolean AIsChallenged() {
		return this.A_is_challenged;
	}

	public void setPassTurnNumber(int passTurnNumber) {
		this.passTurnNumber = passTurnNumber;
	}

	//adds the score if someone get points
	public void UpdatePlayerScore(String username,int value) {
		for(int i = 0; i < numOfPlayer; i++) {
			System.out.println(username);
			System.out.println(playerList[i].getClientName());
			if(playerList[i].getClientName().equals(username)) {
				playerList[i].UpdateScore(value);
			}
		}
	}

	public InGame() {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				grid[i][j] = "";

			}
		}
	}

	public void Update(int x,int y, String value){
		grid[x][y] = value;
	}

	public Connection FindNext(String username) {
		int id = indexOf(username) +1;
		if(!(id <numOfPlayer)) {
			id = 0;
		}
		return playerList[id];
	}

	public int getNumOfPlayer() {
		return numOfPlayer;
	}

	public static int getMaxNumOfPlayer() {
		return MAX;
	}


	public void setSpaceRemain(int spaceRemain) {
		this.spaceRemain = spaceRemain;
	}

	public Connection[] getPlayerList() {
		return playerList;
	}

	public void setPlayerList(Connection[] playerList) {
		this.playerList = playerList;
	}

	public int getVotingNum() {
		return votingNum;
	}

	public void voting(int votingNum) {
		this.votingNum += votingNum;
		this.turnNum += 1;
	}

	public int getTurnNum() {
		return turnNum;
	}

	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}

	public int getPassNum() {
		return passNum;
	}

	public void pass() {
		this.passTurnNumber += 1;
	}
	
	public String getCurrentCommand() {
		return currentCommand;
	}
	
	public String getCurrentClient() {
		return currentClient;
	}
	
	public void setCurrentCommand(String st) {
		this.currentCommand = st;
	}
	
	public void setCurrentClient(String st) {
		this.currentClient = st;
	}

	//print the list of players with their currecnt scores
	public String scoreList(){
		Connection[] ranks = new Connection[numOfPlayer];
		System.arraycopy(playerList,0,ranks,0,numOfPlayer);
		Arrays.sort(ranks,new descComparator());
		String result ="";
		for (int i = 0; i < numOfPlayer; i++) {
			result = (result+playerList[i].getClientName()+"\t\t\t"
					+playerList[i].getScore()+"#");
		}
		return result;
	}

	//update the playerlist when someoone accepts an invitation
	public void updatePlayerList(){
		this.playerList= new Connection[MAX];
		this.numOfPlayer = 0;
		List<Connection> clients = ClientManager.getClientInstance().getInGameClients();
		for (Connection client : clients){
			this.numOfPlayer+=1;
			playerList[numOfPlayer-1] = client;
		}
	}

	private int indexOf(String username){
		for (int i = 0; i <numOfPlayer ; i++) {
			if (playerList[i].getClientName() == username){
				return i;
			}
		}
		return -1;
	}

	//get the 20*20 grid
	public String getGameGridStatus(){
		String grid_code = "";
		for (int i = 0;i <20;i++){
			for (int j = 0;j<20;j++) {
				if (this.getGrid()[i][j].equals("")) {
					grid_code = grid_code + "#";
				} else {
					grid_code = grid_code + this.getGrid()[i][j];
				}
			}
		}
		return grid_code;
	}

	//check if all memebers have passed
	public String passResult(){
		if (passTurnNumber == numOfPlayer){
			return "GameEnd";
		}
		else{
			return "Continue";
		}
	}

	private int nameCompare(String s1, String s2){  
		int bigger = 1, smaller = -1, equal = 0;
		if (s1.compareTo(s2) >0)
			return bigger;
		else if (s1.compareTo(s2) <0)
			return smaller;
		return equal;
	}

	private int scoresCompare(int sc1, int sc2){ 
		int larger = 1, smaller = -1, equal = 0;
		if (sc1 > sc2)
			return larger;
		else if (sc1 < sc2)
			return smaller;
		return equal;
	}

	class descComparator implements Comparator<Connection> {
		int equal =0, reverse = -1;
		@Override
		public int compare(Connection p1, Connection p2) {
			int value = scoresCompare(p1.getScore(),p1.getScore());
			if (value == equal)        
				return nameCompare(p1.getClientName(),p2.getClientName());
			return value*(reverse);     
		}
	}

	public void setChallengeNum(int i) {
		this.challengenum = i;
	}

	public String[][] getGrid() {

		return grid;
	}

}


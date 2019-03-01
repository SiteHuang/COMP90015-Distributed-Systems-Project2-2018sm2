package scrabbleServer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ServerSocketFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {

	// Identifies the user number connected
	private static int counter = 0;

	public static String hostName = "";

	static ArrayList<String> client=new ArrayList<String>();
	static ArrayList<Socket> clientSock=new ArrayList<Socket>();

	static InGame gameStatus;


	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		System.out.println("please enter the port number");
		int port = input.nextInt();

		ServerSocketFactory factory = ServerSocketFactory.getDefault();

		try(ServerSocket server = factory.createServerSocket(port)){
			InetAddress addr = InetAddress.getLocalHost();
			System.out.println("Local HostAddress: "+addr.getHostAddress());
			System.out.println("port: "+port);
			System.out.println("Waiting for client connection..");	
			while(true){
				Socket clientSocket = server.accept();
				counter++;
				System.out.println("Client "+counter+": Applying for connection!");
				client.add("Client"+counter);
				clientSock.add(clientSocket);	

				// Start a new thread for a connection
				//		Thread t = new Thread(() -> serveClient(clientSocket));
				Connection c = new Connection(clientSocket, counter);
				//		c.setName("Thread" + counter);

				Thread eachConnection = new Thread(c);
				eachConnection.start();

			}
		}
		catch (IOException e) {
			System.out.println("Server Exception");
			e.printStackTrace();
		}

	}

}

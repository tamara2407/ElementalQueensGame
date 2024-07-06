package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable{
	
	private Socket socket;
	private List<ClientHandler> clients;
	private BufferedReader in;
	private PrintWriter out;
	private List<Player> players;
	private Player player;
	
	
	public ClientHandler(Socket socket, List<ClientHandler> clients, List<Player> players) {
		this.socket = socket;
		this.clients = clients;
		this.players = players;
	}
	
	@Override
	public void run() {

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			String inputLine;
			while((inputLine = in.readLine())!= null) {
				System.out.println("Received: "+ inputLine);
				handleClientMessage(inputLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void handleClientMessage(String message) {
		
		String[] tokens = message.split(":");
		String command = tokens[0];
		System.out.println("command: "+ command);
		
		switch (command) {
		case "REGISTER":
			handleRegister(tokens[1], tokens[2], tokens[3]);
			break;
		case "LOGIN":
			handleLogin(tokens[1], tokens[2]);
			break;
			
		}
	}
	
	private void handleRegister(String username, String password, String email) {
		synchronized (players) {
			for(Player p : players) {
				if(p.getUsername().equals(username)) {
					out.println("REGISTER_FAILURE:failure");
					return;
				}
			}
			Player newPlayer = new Player(username, password, email);
			players.add(newPlayer);
			out.println("REGISTER_SUCCESS:success");
		}
	}
	
	private void handleLogin(String username, String password) {
		synchronized (players) {
			for(Player p : players) {
				if(p.getUsername().equals(username) && p.getPassword().equals(password)) {
					player = p;
					out.println("LOGIN_SUCCESS:success");
					return;
				}
			}
			out.println("LOGIN_FAILURE:fail");
		}
	}

}

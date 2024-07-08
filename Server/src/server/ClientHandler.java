package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileWriter;

public class ClientHandler implements Runnable{
	
	private Socket socket;
	private List<ClientHandler> clients;
	private BufferedReader in;
	private PrintWriter out;
	private List<Player> players;
	private List<Queen> queens;
	private Player player;
	private static final String CSV_FILE_PATH = "players.csv";
	private static final List<ClientHandler> waitingPlayers = Collections.synchronizedList(new ArrayList<>());
	
	
/*	public ClientHandler(Socket socket, List<ClientHandler> clients, List<Player> players) {
		this.socket = socket;
		this.clients = clients;
		this.players = players;
	}
	*/
	 public ClientHandler(Socket socket, List<ClientHandler> clients, List<Player> players, List<Queen> queens) {
	        this.socket = socket;
	        this.clients = clients;
	        this.players = players;
	        this.queens = queens;
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
		 case "QUEEN_SELECTION":
             handleSelectQueen(tokens[1]);
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
			writePlayerToCSV(newPlayer);
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
				if(p.getUsername().equals(username) && !(p.getPassword().equals(password))) {
					out.println("LOGIN_FAILURE_PASSWORD:failure");
					return;
				}
			}
			out.println("LOGIN_FAILURE:failure");
		}
	}
	
	 private void writePlayerToCSV(Player player) {
	        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
	            writer.append(player.getUsername())
	                  .append(",")
	                  .append(player.getPassword())
	                  .append(",")
	                  .append(player.getEmail())
	                  .append("\n");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 private void handleSelectQueen(String queenIdStr) {
	        int queenId = Integer.parseInt(queenIdStr);
	        synchronized (players) {
	            for (Queen queen : queens) {
	                if (queen.getId() == queenId) {
	                    player.setSelectedQueen(queen);
	                    out.println("SELECT_QUEEN_SUCCESS:success");
	                    matchPlayers();
	                    return;
	                }
	            }
	        }
	    }
	 
	 private void matchPlayers() {
	        synchronized (waitingPlayers) {
	            waitingPlayers.add(this);
	            if (waitingPlayers.size() >= 2) {
	                ClientHandler player1 = waitingPlayers.remove(0);
	                ClientHandler player2 = waitingPlayers.remove(0);
	                
	                
	                String player1QueenName = player1.player.getSelectedQueen().getName();
	                List<Spell> player1Spells = player1.player.getSelectedQueen().getSpells();
	                String player1Spell1 = player1Spells.get(0).getName();
	                String player1Spell2 = player1Spells.get(1).getName();
	                String player1Spell3 = player1Spells.get(2).getName();
	                

	                
	                String player2QueenName = player2.player.getSelectedQueen().getName();
	                List<Spell> player2Spells = player2.player.getSelectedQueen().getSpells();
	                String player2Spell1 = player2Spells.get(0).getName();
	                String player2Spell2 = player2Spells.get(1).getName();
	                String player2Spell3 = player2Spells.get(2).getName();
	                
	                player1.out.println("MATCH_FOUND:" +player1QueenName+":"+ player2QueenName+":"+player1Spell1+":"+player1Spell2+":"+player1Spell3);
	                player2.out.println("MATCH_FOUND:" +player2QueenName+":" +player1QueenName+":"+player2Spell1+":"+player2Spell2+":"+player2Spell3);
	            }
	        }
	    }
	 
	    
	    private void handleSelectSpell(String spellIdStr) {
	       
	    }
	    
	    private void handleDamageTaken(int damageAmount) {
	        
	       
	        
	    }
	    
	    
	  

}

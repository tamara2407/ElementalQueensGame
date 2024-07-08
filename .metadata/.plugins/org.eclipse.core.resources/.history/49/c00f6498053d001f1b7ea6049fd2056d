package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

public class Client {
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private LoginWindow loginWindow;
	private RegisterWindow registerWindow;
	private SelectWindow selectWindow;
	private BattleWindow battleWindow;
	private WaitingForTheOpponentWindow waitingForTheOpponentWindow;
	
	
	 public void sendToServer(String message) {
	        out.println(message);
	    }

	
	public Client(String serverAdress, int serverPort) {
		
		try {
			socket = new Socket(serverAdress, serverPort);
			out = new PrintWriter(socket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			new Thread(new ServerListener()).start();
			
			loginWindow = new LoginWindow(this);
			registerWindow = new RegisterWindow(this);
			selectWindow = new SelectWindow(this);
			//battleWindow = new BattleWindow(this);
			waitingForTheOpponentWindow = new WaitingForTheOpponentWindow(this);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private class ServerListener implements Runnable{
		@Override
		public void run() {
			
			try {
				String message;
				while ((message = in.readLine()) != null) {
					
					processServerMessage(message);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void processServerMessage(String message) {
		String [] parts = message.split(":");
		String command = parts[0];
		
		switch (command) {
		case "LOGIN_SUCCESS":
			loginWindow.setVisible(false);
			showSelectWindow();
			break;
		case "LOGIN_FAILURE":
			loginWindow.showErrorMessage();
			break;
		case "LOGIN_FAILURE_PASSWORD":
			loginWindow.showErrorMessagePassword();
			break;
		case "REGISTER_SUCCESS":
			registerWindow.setVisible(false);
			showLoginWindow();
			break;
		case "REGISTER_FAILURE":
			registerWindow.showErrorMessage();
			break;
		case "SELECT_QUEEN_SUCCESS":
            selectWindow.setVisible(false);
            showWaitingForOpponentWindow();
            break;
		case "MATCH_FOUND":
		    String opponentQueenName = parts[2]; 
		    String playerQueenName=parts[1];
		    String spell1 = removeSpaces(parts[3]);
		    String spell2 = removeSpaces(parts[4]);
		    String spell3 = removeSpaces(parts[5]);
		    waitingForTheOpponentWindow.setVisible(false);
		    showBattleWindow(playerQueenName, opponentQueenName,spell1,spell2,spell3);
		    break;
		}
		
	}
	
	
	public void showLoginWindow() {
		loginWindow.setVisible(true);
	}
	
	public void showRegisterWindow() {
		registerWindow.setVisible(true);
	}
	
	public void showSelectWindow() {
		selectWindow.setVisible(true);
	}
	
	/*public void showBattleWindow() {
		battleWindow.setVisible(true);
	}*/
	
	public void showBattleWindow(String playerQueenName, String opponentQueenName, String spell1, String spell2, String spell3) {
	    BattleWindow battleWindow = new BattleWindow(this, playerQueenName, opponentQueenName, spell1, spell2,spell3);
	    battleWindow.setVisible(true);
	}
	
	
	public void showWaitingForOpponentWindow(){
		waitingForTheOpponentWindow.setVisible(true);
	}
	
	public static String removeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", "");
    }
}
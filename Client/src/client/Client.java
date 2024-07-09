package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
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
	private ResultWindow resultWindow;
	private String username;

	public void sendToServer(String message) {
		out.println(message);
	}

	public Client(String serverAdress, int serverPort) {

		try {
			socket = new Socket(serverAdress, serverPort);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			new Thread(new ServerListener()).start();

			loginWindow = new LoginWindow(this);
			registerWindow = new RegisterWindow(this);
			selectWindow = new SelectWindow(this);
			// battleWindow = new BattleWindow(this);
			waitingForTheOpponentWindow = new WaitingForTheOpponentWindow(this);

		} catch (UnknownHostException e) {
			handleException("Unknown host: "+serverAdress+"!");
		} catch (IOException e) {
			handleException("Error while connecting to server!");
		}

	}

	public String getUsername() {
		return username;
	}

	private class ServerListener implements Runnable {
		@Override
		public void run() {

			try {
				String message;
				while ((message = in.readLine()) != null) {

					processServerMessage(message);
				}
			}catch (IOException e) {
				handleException("Connection lost!");
			}

		}
	}

	private void processServerMessage(String message) {
		String[] parts = message.split(":");
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
			String playerQueenName = parts[1];
			String spell1 = removeSpaces(parts[3]);
			String spell2 = removeSpaces(parts[4]);
			String spell3 = removeSpaces(parts[5]);
			String desc1 = parts[6];
			String desc2 = parts[7];
			String desc3 = parts[8];
			waitingForTheOpponentWindow.setVisible(false);
			battleWindow = new BattleWindow(this, playerQueenName, opponentQueenName, spell1, spell2, spell3, desc1,
					desc2, desc3);
			showBattleWindow(battleWindow, playerQueenName, opponentQueenName, spell1, spell2, spell3, desc1, desc2,
					desc3);
			break;

		case "SPELL_CAST_SUCCESS":

			String spellName = parts[1];
			int playerMana = Integer.parseInt(parts[2]);
			int opponentHealth = Integer.parseInt(parts[3]);
			battleWindow.updatePlayerMana(playerMana);
			battleWindow.updateOpponentHealth(opponentHealth);
			break;

		case "SPELL_CAST_TAKEN":
			String opponentSpellName = parts[1];

			int playerHealth = Integer.parseInt(parts[2]);
			int opponentMana = Integer.parseInt(parts[3]);
			battleWindow.updatePlayerHealth(playerHealth);
			battleWindow.updateOpponentMana(opponentMana);
			break;

		case "SPELL_CAST_SUCCESS_H":

			int playerHealth1 = Integer.parseInt(parts[3]);
			int playerMana1 = Integer.parseInt(parts[2]);
			battleWindow.updatePlayerHealth(playerHealth1);
			battleWindow.updatePlayerMana(playerMana1);
			break;

		case "SPELL_CAST_SUCCESS_HO":

			int opponentHealth1 = Integer.parseInt(parts[3]);
			int opponentMana1 = Integer.parseInt(parts[2]);
			battleWindow.updateOpponentHealth(opponentHealth1);
			battleWindow.updateOpponentMana(opponentMana1);
			break;

		case "SPELL_CAST_FAIL":
			battleWindow.showErrorMessage("Not enough mana to cast the spell!");
			break;

		case "YOUR_TURN":

			battleWindow.setTurn(true);
			break;
		case "WAIT_YOUR_TURN":
			battleWindow.setTurn(false);
			break;
		case "RESULT":
			String result = parts[1];
			boolean isWinner;
			if (result.equals("won")) {
				isWinner = true;
				resultWindow = new ResultWindow(this, isWinner, 6, 6);
				battleWindow.setVisible(false);
				showResultWindow(resultWindow, isWinner, 6, 6);
			} else {
				isWinner = false;
				resultWindow = new ResultWindow(this, isWinner, 5, 5);
				battleWindow.setVisible(false);
				showResultWindow(resultWindow, isWinner, 5, 5);
			}
			break;
		}

	}
	
	private void handleException(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        cleanupResources();
    }

    private void cleanupResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
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

	/*
	 * public void showBattleWindow() { battleWindow.setVisible(true); }
	 */

	public void showBattleWindow(BattleWindow bw, String playerQueenName, String opponentQueenName, String spell1,
			String spell2, String spell3, String desc1, String desc2, String desc3) {

		bw.setVisible(true);
	}

	public void showResultWindow(ResultWindow rw, Boolean isWinner, int wins, int losses) {
		rw.setVisible(true);
	}

	public void showWaitingForOpponentWindow() {
		waitingForTheOpponentWindow.setVisible(true);
	}

	public static String removeSpaces(String input) {
		if (input == null) {
			return null;
		}
		return input.replaceAll("\\s+", "");
	}
}
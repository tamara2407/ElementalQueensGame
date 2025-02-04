package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.io.FileWriter;
import java.util.TimerTask;

public class ClientHandler implements Runnable {

	private Socket socket;
	private List<ClientHandler> clients;
	private BufferedReader in;
	private PrintWriter out;
	private List<Player> players;
	private List<Queen> queens;
	private Player player;
	private String username;
	private static final String CSV_FILE_PATH = "players.csv";
	private static final List<ClientHandler> waitingPlayers = Collections.synchronizedList(new ArrayList<>());

	private static Map<ClientHandler, ClientHandler> opponentMap = new HashMap<>();

	private Timer turnTimer;
	private static final int TURN_TIME_LIMIT = 7000;

	private boolean isTurn;
	private static final List<String> loggedInUsers = Collections.synchronizedList(new ArrayList<>());

	public ClientHandler(Socket socket, List<ClientHandler> clients, List<Player> players, List<Queen> queens) {
		this.socket = socket;
		this.clients = clients;
		this.players = players;
		this.queens = queens;
		this.isTurn = false;
	}

	@Override
	public void run() {

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println("Received: " + inputLine);
				handleClientMessage(this, inputLine);
			}
		} catch (SocketException e) {
			System.out.println("Socket closed by client");
			synchronized (waitingPlayers) {
				waitingPlayers.remove(this);
			}

		} catch (IOException e) {
			System.out.println("AN ERROR OCCURRED!");
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				socket.close();
				clients.remove(this);
				System.out.println("Client disconnected!");
			} catch (IOException e) {
				System.out.println("Error closing resources");
			}
		}
	}

	private void handleClientMessage(ClientHandler clientHandler, String message) {

		String[] tokens = message.split(":");
		String command = tokens[0];
		System.out.println("command: " + command);

		switch (command) {

		case "REGISTER":
			if (tokens.length >= 4) {
				handleRegister(tokens[1], tokens[2], tokens[3]);
			} else {
				out.println("REGISTER_FAILURE:Invalid number of arguments");
			}
			break;
		case "LOGIN":
			if (tokens.length >= 3) {
				handleLogin(tokens[1], tokens[2]);
			} else {
				out.println("LOGIN_FAILURE:Invalid number of arguments");
			}
			break;
		case "QUEEN_SELECTION":
			if (tokens.length >= 2) {
				handleSelectQueen(tokens[1]);
			} else {
				out.println("QUEEN_SELECTION_FAILURE:Invalid number of arguments");
			}
			break;
		case "BACK_TO_SELECT":
			handleBackToSelect();
			break;
		case "SPELL_CAST":
			if (tokens.length >= 4) {
				if (isTurn) {
					String spellName = tokens[3];
					handleSpellCast(this, spellName);
				} else {
					out.println("NOT_YOUR_TURN");
				}
			} else {
				out.println("SPELL_CAST_FAILURE:Invalid number of arguments");
			}
			break;
		case "LOGOUT":
			if (tokens.length >= 2) {
				String username2 = tokens[1];
				handleLogout(username2);
			} else {
				out.println("LOGOUT_FAILURE:Invalid number of arguments");
			}
			break;
		case "EXIT":
			if (tokens.length >= 2) {
				String username = tokens[1];
				handleExit(username);
			} else {
				out.println("EXIT_FAILURE:Invalid number of arguments");
			}
			break;
		case "EXIT_BATTLE":
			if (tokens.length >= 2) {
				handleExitBattle(this, tokens[1]);
			} else {
				out.println("EXIT_BATTLE_FAILURE:Invalid number of arguments");
			}
			break;
		}
	}

	private void handleExitBattle(ClientHandler clientHandler, String username) {

		ClientHandler opponentHandler = opponentMap.get(clientHandler);
		if (opponentHandler != null) {
			updatePlayerStats(opponentHandler, true);
			updatePlayerStats(clientHandler, false);
			opponentHandler.out.println("OPPONENT_EXIT_BATTLE:" + opponentHandler.player.getWins() + ":"
					+ opponentHandler.player.getLosses());
		}

		loggedInUsers.remove(username);
	}

	private void handleRegister(String username, String password, String email) {
		synchronized (players) {
			for (Player p : players) {
				if (p.getUsername().equals(username)) {
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

//			if (loggedInUsers != null && loggedInUsers.contains(username)) {
//				out.println("LOGIN_FAILURE_ALREADY_LOGGEDIN:login");
//				return;
//			}

			for (Player p : players) {
				if (loggedInUsers != null && loggedInUsers.contains(username) && p.checkPassword(password)) {
					out.println("LOGIN_FAILURE_ALREADY_LOGGEDIN:login");
					return;
				}

				if (p.getUsername().equals(username)) {
					if (p.checkPassword(password)) {
						player = p;
						this.username = username;
						loggedInUsers.add(username);
						double winRate = ((double) p.getWins() / (p.getWins() + p.getLosses()) * 100);

						out.println("LOGIN_SUCCESS:" + username + ":" + winRate);
						return;
					} else {
						out.println("LOGIN_FAILURE_PASSWORD:failure");
						return;
					}
				}
			}
			out.println("LOGIN_FAILURE:failure");
		}
	}

	private void writePlayerToCSV(Player player) {
		try (FileWriter writer = new FileWriter(CSV_FILE_PATH, true)) {
			writer.append(player.getUsername()).append(",").append(player.getHashedPassword()).append(",")
					.append(player.getEmail()).append(",").append(String.valueOf(player.getWins())).append(",")
					.append(String.valueOf(player.getLosses())).append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleSelectQueen(String queenIdStr) {
		int queenId = Integer.parseInt(queenIdStr);
		synchronized (players) {
			for (Queen queen : queens) {
				if (queen.getId() == queenId) {
					player.setSelectedQueen(new Queen(queen));
					resetQueenStats(player.getSelectedQueen());

					List<Spell> spells = player.getSelectedQueen().getSpells();
					for (Spell s : spells) {
						s.resertUsageCount();
					}

					out.println("SELECT_QUEEN_SUCCESS:success");
					matchPlayers();
					return;
				}
			}
		}
	}

	private void resetQueenStats(Queen queen) {
		queen.resetMana();
		queen.resetHealth();
	}

	private void matchPlayers() {
		synchronized (waitingPlayers) {
			waitingPlayers.add(this);
			if (waitingPlayers.size() >= 2) {
				ClientHandler player1 = waitingPlayers.remove(0);
				ClientHandler player2 = waitingPlayers.remove(0);

				opponentMap.put(player1, player2);
				opponentMap.put(player2, player1);

				player1.isTurn = true;
				player2.isTurn = false;

				String player1QueenName = player1.player.getSelectedQueen().getName();
				List<Spell> player1Spells = player1.player.getSelectedQueen().getSpells();
				String player1Spell1 = player1Spells.get(0).getName();
				String player1Spell2 = player1Spells.get(1).getName();
				String player1Spell3 = player1Spells.get(2).getName();

				String player1DescSpell1 = player1Spells.get(0).getDescription();
				String player1DescSpell2 = player1Spells.get(1).getDescription();
				String player1DescSpell3 = player1Spells.get(2).getDescription();

				String player1Username = player1.player.getUsername();

				String player2QueenName = player2.player.getSelectedQueen().getName();
				List<Spell> player2Spells = player2.player.getSelectedQueen().getSpells();
				String player2Spell1 = player2Spells.get(0).getName();
				String player2Spell2 = player2Spells.get(1).getName();
				String player2Spell3 = player2Spells.get(2).getName();

				String player2DescSpell1 = player2Spells.get(0).getDescription();
				String player2DescSpell2 = player2Spells.get(1).getDescription();
				String player2DescSpell3 = player2Spells.get(2).getDescription();

				String player2Username = player2.player.getUsername();

				player1.out.println("MATCH_FOUND:" + player1QueenName + ":" + player2QueenName + ":" + player1Spell1
						+ ":" + player1Spell2 + ":" + player1Spell3 + ":" + player1DescSpell1 + ":" + player1DescSpell2
						+ ":" + player1DescSpell3 + ":" + player1Username + ":" + player2Username);

				player2.out.println("MATCH_FOUND:" + player2QueenName + ":" + player1QueenName + ":" + player2Spell1
						+ ":" + player2Spell2 + ":" + player2Spell3 + ":" + player2DescSpell1 + ":" + player2DescSpell2
						+ ":" + player2DescSpell3 + ":" + player2Username + ":" + player1Username);

				player1.out.println("YOUR_TURN");
				player2.out.println("WAIT_YOUR_TURN");
				startTurnTimer(player1);
			}
		}
	}

//	private void handleSpellCast(ClientHandler clientHandler, String spellName) {
//
//		Queen castingQueen = clientHandler.player.getSelectedQueen();
//
//		if (castingQueen != null) {
//			Spell spellToCast = findSpellInQueen(spellName, castingQueen);
//			if (spellToCast != null) {
//
//				int manaCost = spellToCast.getManaCost();
//				int spellEffect = spellToCast.getEffect();
//
//				if (castingQueen.getMana() >= manaCost) {
//					castingQueen.reduceMana(manaCost);
//					ClientHandler opponentHandler = opponentMap.get(clientHandler);
//
//					Queen opponentQueen = opponentHandler.player.getSelectedQueen();
//					if (spellEffect > 0) {
//						if (spellToCast.getUsageCount() < 2) {
//							spellToCast.incrementUsageCount();
//
//							castingQueen.setHealth(castingQueen.getHealth() + spellEffect);
//
//							if (castingQueen.getHealth() > 100) {
//								castingQueen.setHealth(100);
//							}
//
//							out.println("SPELL_CAST_SUCCESS_H:" + spellName + ":" + castingQueen.getMana() + ":"
//									+ castingQueen.getHealth());
//							opponentHandler.out.println("SPELL_CAST_SUCCESS_HO:" + spellName + ":"
//									+ castingQueen.getMana() + ":" + castingQueen.getHealth());
//							toggleTurn();
//							return;
//						} else {
//							out.println("LIMIT_REACHED: limit");
//						}
//					}
//
//					if (opponentQueen != null && spellEffect < 0) {
//
//						opponentQueen.reduceHealth(spellEffect);
//						if (opponentQueen.getHealth() <= 0) {
//							out.println("RESULT:" + "won" + ":" + String.valueOf(clientHandler.player.getWins() + 1)
//									+ ":" + String.valueOf(clientHandler.player.getLosses()));
//							opponentHandler.out
//									.println("RESULT:" + "lost" + ":" + String.valueOf(opponentHandler.player.getWins())
//											+ ":" + String.valueOf(opponentHandler.player.getLosses() + 1));
//							updatePlayerStats(clientHandler, true);
//							updatePlayerStats(opponentHandler, false);
//						}
//
//						opponentHandler.out.println("SPELL_CAST_TAKEN:" + spellName + ":" + opponentQueen.getHealth()
//								+ ":" + castingQueen.getMana());
//						out.println("SPELL_CAST_SUCCESS:" + spellName + ":" + castingQueen.getMana() + ":"
//								+ opponentQueen.getHealth());
//						toggleTurn();
//					}
//
//				} else {
//					out.println("SPELL_CAST_FAIL:INSUFFICIENT_MANA");
//				}
//			}
//		}
//	}

	private boolean lessThenEverything(int mana, List<Spell> spells) {

		int minn = Math.min(Math.min(spells.get(0).getManaCost(), spells.get(1).getManaCost()),
				spells.get(2).getManaCost());
		if (mana < (minn)) {
			return true;
		}
		return false;
	}

	private void handleSpellCast(ClientHandler clientHandler, String spellName) {

		Queen castingQueen = clientHandler.player.getSelectedQueen();
		ClientHandler opponentHandler = opponentMap.get(clientHandler);
		Queen opponentQueen = opponentHandler.player.getSelectedQueen();


		if (castingQueen != null) {
			Spell spellToCast = findSpellInQueen(spellName, castingQueen);
			if (spellToCast != null) {

				int manaCost = spellToCast.getManaCost();
				int spellEffect = spellToCast.getEffect();

				if (castingQueen.getMana() >= manaCost) {
					castingQueen.reduceMana(manaCost);

					if (spellEffect > 0) {
						if (spellToCast.getUsageCount() < 2) {
							spellToCast.incrementUsageCount();

							castingQueen.setHealth(castingQueen.getHealth() + spellEffect);

							if (castingQueen.getHealth() > 100) {
								castingQueen.setHealth(100);
							}

							out.println("SPELL_CAST_SUCCESS_H:" + spellName + ":" + castingQueen.getMana() + ":"
									+ castingQueen.getHealth());
							opponentHandler.out.println("SPELL_CAST_SUCCESS_HO:" + spellName + ":"
									+ castingQueen.getMana() + ":" + castingQueen.getHealth());
							toggleTurn();
							return;
						} else {
							out.println("LIMIT_REACHED: limit");
						}
					}

					if (opponentQueen != null && spellEffect < 0) {

						opponentQueen.reduceHealth(spellEffect);
						if (opponentQueen.getHealth() <= 0) {
							out.println("RESULT:" + "won" + ":" + String.valueOf(clientHandler.player.getWins() + 1)
									+ ":" + String.valueOf(clientHandler.player.getLosses()));
							opponentHandler.out
									.println("RESULT:" + "lost" + ":" + String.valueOf(opponentHandler.player.getWins())
											+ ":" + String.valueOf(opponentHandler.player.getLosses() + 1));
							updatePlayerStats(clientHandler, true);
							updatePlayerStats(opponentHandler, false);
						}

						opponentHandler.out.println("SPELL_CAST_TAKEN:" + spellName + ":" + opponentQueen.getHealth()
								+ ":" + castingQueen.getMana());
						out.println("SPELL_CAST_SUCCESS:" + spellName + ":" + castingQueen.getMana() + ":"
								+ opponentQueen.getHealth());
						toggleTurn();
					}

				} else {
					out.println("SPELL_CAST_FAIL:INSUFFICIENT_MANA");
				}
				
				if (lessThenEverything(castingQueen.getMana(), castingQueen.getSpells())
						&& lessThenEverything(opponentQueen.getMana(), opponentQueen.getSpells())) {
					if (castingQueen.getHealth() > opponentQueen.getHealth()) {
						out.println("RESULT:" + "won" + ":" + String.valueOf(clientHandler.player.getWins() + 1) + ":"
								+ String.valueOf(clientHandler.player.getLosses()));
						opponentHandler.out.println("RESULT:" + "lost" + ":" + String.valueOf(opponentHandler.player.getWins())
								+ ":" + String.valueOf(opponentHandler.player.getLosses() + 1));
						updatePlayerStats(clientHandler, true);
						updatePlayerStats(opponentHandler, false);

					} else if (castingQueen.getHealth() == opponentQueen.getHealth()) {
						out.println("RESULT:" + "lost" + ":" + String.valueOf(clientHandler.player.getWins()) + ":"
								+ String.valueOf(clientHandler.player.getLosses() + 1));
						opponentHandler.out.println("RESULT:" + "lost" + ":" + String.valueOf(opponentHandler.player.getWins())
								+ ":" + String.valueOf(opponentHandler.player.getLosses() + 1));
						updatePlayerStats(clientHandler, false);
						updatePlayerStats(opponentHandler, false);

					}
				}
			}
		}
	}

	private void updatePlayerStats(ClientHandler clientHandler, boolean won) {
		Player player = clientHandler.player;
		if (won) {
			player.incrementWins();
		} else {
			player.incrementLosses();
		}
		List<Player> playerList = readPlayersFromCSV();
		updatePlayerStatsInList(playerList, player);
		writePlayersToCSV(playerList);
	}

	private List<Player> readPlayersFromCSV() {
		List<Player> playerList = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 5) {
					Player p = new Player(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]),
							Integer.parseInt(parts[4]));
					playerList.add(p);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return playerList;
	}

	private void updatePlayerStatsInList(List<Player> playerList, Player updatedPlayer) {
		for (Player player : playerList) {
			if (player.getUsername().equals(updatedPlayer.getUsername())) {
				player.setWins(updatedPlayer.getWins());
				player.setLosses(updatedPlayer.getLosses());
				break;
			}
		}
	}

	private void writePlayersToCSV(List<Player> playerList) {
		File csvFile = new File(CSV_FILE_PATH);

		try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
			writer.println("username,hashedPassword,email,wins,losses");

			for (Player player : playerList) {
				writer.println(player.getUsername() + "," + player.getHashedPassword() + "," + player.getEmail() + ","
						+ player.getWins() + "," + player.getLosses());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleLogout(String username) {
		synchronized (waitingPlayers) {
			waitingPlayers.remove(this);
		}
		System.out.println(username + " has logged out.");
		handleExit(username);

	}

	private void handleBackToSelect() {
		synchronized (waitingPlayers) {
			waitingPlayers.remove(this);
		}
	}

	private void handleExit(String username) {
		loggedInUsers.remove(username);
	}

	private Spell findSpellInQueen(String spellName, Queen queen) {

		for (Spell spell : queen.getSpells()) {
			if (removeSpaces(spell.getName()).equals(spellName)) {
				return spell;
			}
		}
		return null;
	}

	private void toggleTurn() {
		ClientHandler opponent = opponentMap.get(this);
		if (opponent != null) {
			this.isTurn = !isTurn;
			opponent.isTurn = !opponent.isTurn;
			if (opponent.isTurn) {
				opponent.out.println("YOUR_TURN");
				this.out.println("WAIT_YOUR_TURN");
				startTurnTimer(opponent);
			} else {
				this.out.println("YOUR_TURN");
				opponent.out.println("WAIT_YOUR_TURN");
				startTurnTimer(this);
			}
		}
	}

	private void startTurnTimer(ClientHandler currentTurnPlayer) {
		if (turnTimer != null) {
			turnTimer.cancel();
		}
		turnTimer = new Timer();
		turnTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (currentTurnPlayer.isTurn) {
					currentTurnPlayer.isTurn = false;
					ClientHandler opponent = opponentMap.get(currentTurnPlayer);
					if (opponent != null) {
						opponent.isTurn = true;
						opponent.out.println("YOUR_TURN");
						currentTurnPlayer.out.println("WAIT_YOUR_TURN");
						startTurnTimer(opponent);
					}
				}
			}
		}, TURN_TIME_LIMIT);
	}

	public static String removeSpaces(String input) {
		if (input == null) {
			return null;
		}
		return input.replaceAll("\\s+", "");
	}

}

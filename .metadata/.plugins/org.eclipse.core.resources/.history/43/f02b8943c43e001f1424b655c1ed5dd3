package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

public class Server {

	private static final int PORT = 13245;
	private static List<ClientHandler> clients = new ArrayList<>();
	private static List<Player> players = new ArrayList<Player>();
	private static List<Queen> queens = new ArrayList<>();
	private static final String CSV_FILE_NAME = "players.csv";

	public static void main(String[] args) {

		String currentDirectory = Paths.get("").toAbsolutePath().toString();
		String csvFilePath = currentDirectory + File.separator + CSV_FILE_NAME;

		createCSVFile(csvFilePath);

		loadPlayersFromCSV(csvFilePath);

		queens = createQueens();

		try (ServerSocket serverSoket = new ServerSocket(PORT)) {
			System.out.println("Server started on port " + PORT);

			while (true) {
				Socket socket = serverSoket.accept();
				System.out.println("New client connected");
				ClientHandler clientHandler = new ClientHandler(socket, clients, players, queens);
				System.out.println("New client initialized");
				clients.add(clientHandler);
				System.out.println("New client added to clients");
				new Thread(clientHandler).start();
				System.out.println("New client thread created");
			}
		} catch (SocketException e) {
			if (e.getMessage().equals("Socket closed")) {
				System.out.println("The socket has been closed on the client side!");
			} else if (e.getMessage().equals("Connection reset")) {
				System.out.println("The connection with the client side has been lost!");
			} else {
				System.out.println("There was an error while working with the socket!");

			}
		} catch (IOException e) {
			System.out.println("There was an error while starting the server!");

		} catch (Exception e) {
			System.out.println("AN ERROR OCCURRED!");

		}
	}

	private static void createCSVFile(String filePath) {
		File csvFile = new File(filePath);
		if (!(csvFile.exists())) {
			try (FileWriter writer = new FileWriter(csvFile)) {

				writer.append("username,hashedPassword,email,wins,losses\n");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void loadPlayersFromCSV(String filePath) {
		File csvFile = new File(filePath);
		if (csvFile.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line;
				br.readLine();
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");
					if (values.length == 5) {
						Player player = new Player(values[0], values[1], values[2], Integer.parseInt(values[3]),
								Integer.parseInt(values[4]));
						players.add(player);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("CSV file not found, skipping loading players.");
		}
	}

	public static List<Queen> createQueens() {
		List<Queen> queens = new ArrayList<>();

		Queen blaze = new Queen(1, "Blaze", createBlazeSpells(), 100, 100);
		Queen aqua = new Queen(2, "Aqua", createAquaSpells(), 100, 100);
		Queen frost = new Queen(3, "Frost", createFrostSpells(), 100, 100);
		Queen ivy = new Queen(4, "Ivy", createIvySpells(), 100, 100);
		Queen terra = new Queen(5, "Terra", createTerraSpells(), 100, 100);

		queens.add(blaze);
		queens.add(aqua);
		queens.add(frost);
		queens.add(ivy);
		queens.add(terra);

		return queens;
	}

	private static List<Spell> createBlazeSpells() {
		List<Spell> spells = new ArrayList<>();
		spells.add(new Spell("Inferno Burst", -20, "20 damage,10 mana", 10));
		spells.add(new Spell("Ember Storm", -15, "15 damage,5 mana", 5));
		spells.add(new Spell("Flame Eruption", -30, "30 damage,15 mana", 15));
		return spells;
	}

	private static List<Spell> createAquaSpells() {
		List<Spell> spells = new ArrayList<>();
		spells.add(new Spell("Tsunami Wave", -20, "20 damage,10 mana", 10));
		spells.add(new Spell("Aqua Surge", -30, "30 damage,15 mana", 15));
		spells.add(new Spell("Healing Waters", 50, "50 heal,10 mana", 10));
		return spells;
	}

	private static List<Spell> createFrostSpells() {
		List<Spell> spells = new ArrayList<>();
		spells.add(new Spell("Blizzard Blast", -10, "10 damage,5 mana", 5));
		spells.add(new Spell("Icicle Spear", -15, "15 damage,7 mana", 7));
		spells.add(new Spell("Frozen Touch", -30, "30 damage,15 mana", 15));
		return spells;
	}

	private static List<Spell> createIvySpells() {
		List<Spell> spells = new ArrayList<>();
		spells.add(new Spell("Thorn Whip", -25, "25 damage,12 mana", 12));
		spells.add(new Spell("Bloom Heal", 50, "50 heal,10 mana", 10));
		spells.add(new Spell("Poison Spore", -15, "15 damage,7 mana", 7));
		return spells;
	}

	private static List<Spell> createTerraSpells() {
		List<Spell> spells = new ArrayList<>();
		spells.add(new Spell("Earthquake Smash", -15, "15 damage,7 mana", 7));
		spells.add(new Spell("Boulder Throw", -20, "20 damage,13 mana", 13));
		spells.add(new Spell("Root Strike", -25, "25 damage,15 mana", 15));
		return spells;
	}

}

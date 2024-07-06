package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private static final int PORT = 13245;
	private static List<ClientHandler> clients = new ArrayList<>();
	
	public static void main(String[] args) {
		try(ServerSocket serverSoket = new ServerSocket(PORT)){
			System.out.println("Server started on port "+PORT );
			
			while(true) {
				Socket socket = serverSoket.accept();
				System.out.println("New client connected");
				ClientHandler clientHandler = new ClientHandler(socket, clients);
				System.out.println("New client initialized");
				clients.add(clientHandler);
				System.out.println("New client added to clients");
				new Thread(clientHandler).start();
				System.out.println("New client thread created");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

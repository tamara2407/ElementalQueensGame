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
	
	
	public ClientHandler(Socket socket, List<ClientHandler> clients) {
		this.socket = socket;
		this.clients = clients;
		
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
		
		//implementacija login, register...
	}

}

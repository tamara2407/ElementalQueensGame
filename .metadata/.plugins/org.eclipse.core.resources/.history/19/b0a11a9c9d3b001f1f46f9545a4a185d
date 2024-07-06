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
		
		//treba implementirati login, register...
		
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
	

}
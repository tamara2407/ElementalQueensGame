package client;

import javax.swing.SwingUtilities;

public class ClientMain {

	public static void main(String[] args) {
		String serverAddress = "localhost";
		int serverPort = 13245;

		Client client = new Client(serverAddress, serverPort);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				client.showLoginWindow();
			}
		});
	}

}

package client;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegisterWindow extends JFrame{
	
	private Client client;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField emailField;
	
	public RegisterWindow(Client client) {
		this.client = client;
		setTitle("Register");
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                client.sendToServer("REGISTER:" + username + ":" + password + ":" + email);
                setVisible(false);
                client.showLoginWindow();
            }
        });
        panel.add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 setVisible(false);
                 client.showLoginWindow();
            }
        });
        panel.add(backButton);

        add(panel);
        add(panel);
	}
	
	public void showErrorMessage() {
        JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
    }

}

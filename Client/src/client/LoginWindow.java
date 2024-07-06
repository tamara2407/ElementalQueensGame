package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginWindow extends JFrame {
	
    private Client client;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow(Client client) {
        this.client = client;
        setTitle("Login");
        setSize(500, 400); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Elemental Queens", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30)); 
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Username: "), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(5, 0, 5, 0);
        gbcButton.gridx = 0;
        gbcButton.gridy = 0;
        gbcButton.fill = GridBagConstraints.HORIZONTAL;

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                client.sendToServer("LOGIN:" + username + ":" + password);
            }
        });
        buttonPanel.add(loginButton, gbcButton);

        gbcButton.gridy = 1;

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                client.showRegisterWindow();
            }
        });
        buttonPanel.add(registerButton, gbcButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    public void showErrorMessage() {
        JOptionPane.showMessageDialog(this, "You have to register first.");
    }
}

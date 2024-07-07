package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WaitingForTheOpponentWindow extends JFrame {

    private Client client;

    public WaitingForTheOpponentWindow(Client client) {
        this.client = client;
        setTitle("Elemental Queens");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel mainPanel = new BackgroundPanel(new ImageIcon("images/background/bg5Original.jpg").getImage());
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JLabel titleLabel = new JLabel("Elemental Queens", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel waitingLabel = new JLabel("Waiting for the opponent...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Serif", Font.PLAIN, 40));
        waitingLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(waitingLabel, gbc);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Serif", Font.PLAIN, 20));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setOpaque(false); 
        logoutButton.setBorderPainted(false);
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendToServer("LOGOUT:" + "logout");
                dispose(); 
                client.showLoginWindow();
            }
        });

        JPanel bottomLeftPanel = new JPanel(new BorderLayout());
        bottomLeftPanel.setOpaque(false);
        bottomLeftPanel.add(logoutButton, BorderLayout.SOUTH);
        mainPanel.add(bottomLeftPanel, BorderLayout.WEST);
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

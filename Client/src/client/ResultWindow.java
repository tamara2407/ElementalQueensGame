package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String fileName) {
        backgroundImage = new ImageIcon(fileName).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

public class ResultWindow extends JFrame {

    private JLabel resultLabel;
    private JLabel winsLabel;
    private JLabel lossesLabel;
    private JLabel usernameLabel;
    private JButton playAgainButton;
    private JButton logOutButton;
    private Client client;

    public ResultWindow(Client client, boolean isWinner, int wins, int losses, String username) {

        this.client = client;
        setTitle("Game Result");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        double winRate = ((double) wins / (wins + losses) * 100);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.sendToServer("EXIT:" + client.getUsername());
                System.exit(0);
            }
        });

        BackgroundPanel backgroundPanel = new BackgroundPanel("images/background/bg7.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JLabel titleLabel = new JLabel("Elemental Queens", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        resultLabel = new JLabel(isWinner ? "You won!" : "You lost!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Serif", Font.BOLD, 28));
        resultLabel.setForeground(Color.WHITE);
        backgroundPanel.add(resultLabel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        statsPanel.setOpaque(false);
        usernameLabel = new JLabel("Username: "+username, SwingConstants.CENTER);
        winsLabel = new JLabel("Wins: " + wins, SwingConstants.CENTER);
        lossesLabel = new JLabel("Losses: " + losses, SwingConstants.CENTER);
        usernameLabel.setForeground(Color.WHITE);
        winsLabel.setForeground(Color.WHITE);
        lossesLabel.setForeground(Color.WHITE);
        statsPanel.add(usernameLabel);
        statsPanel.add(winsLabel);
        statsPanel.add(lossesLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        playAgainButton = new JButton("Play Again");
        logOutButton = new JButton("Log Out");
        buttonPanel.add(playAgainButton);
        buttonPanel.add(logOutButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(statsPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(southPanel, BorderLayout.SOUTH);

        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                client.showSelectWindow(winRate);

            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendToServer("EXIT:" + client.getUsername());
                setVisible(false);
                client.showLoginWindow();
            }
        });

        setVisible(true);
    }

}

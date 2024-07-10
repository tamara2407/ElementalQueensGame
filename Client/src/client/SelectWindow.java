package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SelectWindow extends JFrame {

    private Client client;

    public SelectWindow(Client client) {
        this.client = client;
        setTitle("Elemental Queens");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.sendToServer("EXIT:"+client.getUsername());
                System.exit(0);
            }
        });
        

        BackgroundPanel mainPanel = new BackgroundPanel(new ImageIcon("images/background/bg5.jpg").getImage());
        mainPanel.setLayout(new GridBagLayout());
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel titleLabel = new JLabel("Elemental Queens", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48)); 
        titleLabel.setForeground(Color.BLACK); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        mainPanel.add(titleLabel, gbc);

        JPanel selectionPanel = new JPanel(new BorderLayout());
        selectionPanel.setOpaque(false); 

        JLabel chooseLabel = new JLabel("Choose your Queen:", SwingConstants.LEFT);
        chooseLabel.setFont(new Font("Serif", Font.PLAIN, 28)); 
        chooseLabel.setForeground(Color.BLACK); 
        chooseLabel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 0)); 
        selectionPanel.add(chooseLabel, BorderLayout.NORTH);

        JPanel choosePanel = new JPanel();
        choosePanel.setLayout(new GridBagLayout());
        choosePanel.setOpaque(false); 
        GridBagConstraints gbcInner = new GridBagConstraints();
        gbcInner.insets = new Insets(1, 1, 1, 1);
        gbcInner.fill = GridBagConstraints.BOTH;
        gbcInner.gridx = 0;
        gbcInner.gridy = 0;

        String[] queenNames = {"Blaze", "Aqua", "Frost", "Ivy", "Terra"};
        String[] imagePaths = {
            "images/queens/Blaze.png",
            "images/queens/Aqua.png",
            "images/queens/Frost.png",
            "images/queens/Ivy.png",
            "images/queens/Terra.png"
        };

        
        for (int i = 0; i < queenNames.length; i++) {
            JPanel queenPanel = new JPanel(new BorderLayout());
            queenPanel.setOpaque(false); 

            JLabel queenLabel = new JLabel(queenNames[i], SwingConstants.CENTER);
            queenLabel.setFont(new Font("Serif", Font.BOLD, 22)); 
            queenLabel.setForeground(Color.BLACK); 
            queenPanel.add(queenLabel, BorderLayout.NORTH);

            JButton queenButton = new JButton();
            queenButton.setPreferredSize(new Dimension(235, 235));
            queenButton.setMaximumSize(new Dimension(235, 235));
            queenButton.setMinimumSize(new Dimension(235, 235));
            queenButton.setIcon(new ImageIcon(imagePaths[i]));
            
            int queenIndex = i + 1; 
            queenButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.sendToServer("QUEEN_SELECTION:" + queenIndex);
                   
                }
            });
          
            queenPanel.add(queenButton, BorderLayout.CENTER);

            gbcInner.gridx = i;
            choosePanel.add(queenPanel, gbcInner);
        }

        selectionPanel.add(choosePanel, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        mainPanel.add(selectionPanel, gbc);
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
    
    
    //za probu
    public static void main(String[] args) {

        Client client = new Client("localhost", 13245);
        SelectWindow window = new SelectWindow(client);
        window.setVisible(true);
    }

}

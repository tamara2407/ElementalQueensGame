package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleWindow extends JFrame {
	
	private Client client;

    public BattleWindow(Client client) {
    	this.client = client;
        setTitle("Elemental Queens");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Elemental Queens", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel leftCharacter = new JLabel();
      //stavila sam sliku random kraljice za probu
        leftCharacter.setIcon(new ImageIcon(
                new ImageIcon("images/queens/Blaze.png").getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH)));
        JPanel leftCharacterPanel = new JPanel(new BorderLayout());
        leftCharacterPanel.add(leftCharacter, BorderLayout.WEST);
        leftCharacterPanel.setOpaque(false);
        centerPanel.add(leftCharacterPanel, BorderLayout.WEST);

        JLabel middleText = new JLabel("My Turn", SwingConstants.CENTER);
        middleText.setFont(new Font("Serif", Font.BOLD, 24));
        centerPanel.add(middleText, BorderLayout.CENTER);

        JLabel rightCharacter = new JLabel();
        rightCharacter.setIcon(new ImageIcon(
                new ImageIcon("images/queens/Ivy.png").getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH)));
        JPanel rightCharacterPanel = new JPanel(new BorderLayout());
        rightCharacterPanel.add(rightCharacter, BorderLayout.EAST);
        rightCharacterPanel.setOpaque(false);
        centerPanel.add(rightCharacterPanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton spell1 = new JButton("Spell 1");
        spell1.setPreferredSize(new Dimension(100, 100));
        buttonPanel.add(spell1, gbc);

        gbc.gridy = 1;
        JTextArea spell1Description = new JTextArea("Ovo je Spell 1");
        spell1Description.setLineWrap(true);
        spell1Description.setWrapStyleWord(true);
        spell1Description.setOpaque(false);
        spell1Description.setEditable(false);
        spell1Description.setFocusable(false);
        buttonPanel.add(spell1Description, gbc);
        
        spell1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //slanje serveru id kliknute magije
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        JButton spell2 = new JButton("Spell 2");
        spell2.setPreferredSize(new Dimension(100, 100));
        buttonPanel.add(spell2, gbc);
        

        gbc.gridy = 1;
        JTextArea spell2Description = new JTextArea("Ovo je Spell 2");
        spell2Description.setLineWrap(true);
        spell2Description.setWrapStyleWord(true);
        spell2Description.setOpaque(false);
        spell2Description.setEditable(false);
        spell2Description.setFocusable(false);
        buttonPanel.add(spell2Description, gbc);
        
        spell1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            	//slanje serveru id kliknute magije
            	
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        JButton spell3 = new JButton("Spell 3");
        spell3.setPreferredSize(new Dimension(100, 100));
        buttonPanel.add(spell3, gbc);

        gbc.gridy = 1;
        //stavila sam random spell za probu
        JTextArea spell3Description = new JTextArea("Inferno Burst\nA powerful wave of fire that deals significant damage.\r\n"
        		+ "");
        spell3Description.setLineWrap(true);
        spell3Description.setWrapStyleWord(true);
        spell3Description.setOpaque(false);
        spell3Description.setEditable(false);
        spell3Description.setFocusable(false);
        buttonPanel.add(spell3Description, gbc);
        
        spell1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            	//slanje serveru id kliknute magije
            }
        });

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        topLeftPanel.setOpaque(false);

        JLabel leftHealthBar = new JLabel();
        leftHealthBar.setOpaque(true);
        leftHealthBar.setBackground(Color.GREEN);
        leftHealthBar.setPreferredSize(new Dimension(150, 20));
        topLeftPanel.add(leftHealthBar);

        JLabel leftManaBar = new JLabel();
        leftManaBar.setOpaque(true);
        leftManaBar.setBackground(Color.BLUE);
        leftManaBar.setPreferredSize(new Dimension(150, 20));
        topLeftPanel.add(leftManaBar);

        mainPanel.add(topLeftPanel, BorderLayout.WEST);

       
        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new BoxLayout(topRightPanel, BoxLayout.Y_AXIS));
        topRightPanel.setOpaque(false);

        JLabel rightHealthBar = new JLabel();
        rightHealthBar.setOpaque(true);
        rightHealthBar.setBackground(Color.GREEN);
        rightHealthBar.setPreferredSize(new Dimension(150, 20));
        topRightPanel.add(rightHealthBar);

        JLabel rightManaBar = new JLabel();
        rightManaBar.setOpaque(true);
        rightManaBar.setBackground(Color.BLUE);
        rightManaBar.setPreferredSize(new Dimension(150, 20));
        topRightPanel.add(rightManaBar);

        mainPanel.add(topRightPanel, BorderLayout.EAST);
    }
    
    
    //za probu
    public static void main(String[] args) {

        Client client = new Client("localhost", 13245);
        BattleWindow window = new BattleWindow(client);
        window.setVisible(true);
    }
}

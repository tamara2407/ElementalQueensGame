package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BattleWindow extends JFrame {

	private Client client;
	private String playerQueenName;
	private String opponentQueenName;
	private String spell1Name;
	private String spell2Name;
	private String spell3Name;
	private String desc1;
	private String desc2;
	private String desc3;
	private String username1;
	private String username2;
	private JProgressBar playerHealthBar;
	private JProgressBar playerManaBar;
	private JProgressBar opponentHealthBar;
	private JProgressBar opponentManaBar;
	private boolean isTurn;
	JLabel middleText;

	public BattleWindow(Client client, String playerQueenName, String opponentQueenName, String spell1N, String spell2N,
			String spell3N, String desc1, String desc2, String desc3, String username1, String username2) {
		this.client = client;
		this.playerQueenName = playerQueenName;
		this.opponentQueenName = opponentQueenName;
		this.spell1Name = spell1N;
		this.spell2Name = spell2N;
		this.spell3Name = spell3N;
		this.desc1 = desc1;
		this.desc2 = desc2;
		this.desc3 = desc3;
		this.username1 = username1;
		this.username2 = username2;
		setTitle("Elemental Queens");
		setSize(1200, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int response = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit? This will count as a loss.", "Exit Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
					client.sendToServer("EXIT_BATTLE:" + client.getUsername());
					System.exit(0);
				} else {
					((JFrame) e.getSource()).setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				}
			}
		});

		JPanel mainPanel = new BackgroundPanel("images/background/bg7.jpg");
		mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);

		JLabel titleLabel = new JLabel("Elemental Queens", SwingConstants.CENTER);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
		mainPanel.add(titleLabel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(false);

		JPanel playerCharacterPanel = new JPanel();
		playerCharacterPanel.setLayout(new BoxLayout(playerCharacterPanel, BoxLayout.Y_AXIS));
		playerCharacterPanel.setOpaque(false);
		playerCharacterPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

		playerHealthBar = new JProgressBar(0, 100);
		playerHealthBar.setValue(100);
		playerHealthBar.setForeground(Color.GREEN);
		playerHealthBar.setStringPainted(true);
		playerHealthBar.setPreferredSize(new Dimension(200, 20));

		playerManaBar = new JProgressBar(0, 100);
		playerManaBar.setValue(100);
		playerManaBar.setForeground(Color.BLUE);
		playerManaBar.setStringPainted(true);
		playerManaBar.setPreferredSize(new Dimension(200, 20));

		JLabel playerCharacter = new JLabel();
		playerCharacter.setIcon(new ImageIcon(new ImageIcon("images/queens/" + playerQueenName + ".png").getImage()
				.getScaledInstance(170, 170, Image.SCALE_SMOOTH)));

		JLabel playerNameLabel = new JLabel(username1, SwingConstants.CENTER);
		playerNameLabel.setForeground(Color.WHITE);
		playerNameLabel.setFont(new Font("Serif", Font.BOLD, 18));

		playerCharacterPanel.add(playerHealthBar);
		playerCharacterPanel.add(playerManaBar);
		playerCharacterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		playerCharacterPanel.add(playerCharacter);
		playerCharacterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		playerCharacterPanel.add(playerNameLabel);

		JPanel leftWrapper = new JPanel(new BorderLayout());
		leftWrapper.setOpaque(false);
		leftWrapper.add(playerCharacterPanel, BorderLayout.WEST);

		centerPanel.add(leftWrapper, BorderLayout.WEST);

		middleText = new JLabel(isTurn ? "Your turn" : "Opponents turn", SwingConstants.CENTER);
		middleText.setForeground(Color.WHITE);
		middleText.setFont(new Font("Serif", Font.BOLD, 24));
		centerPanel.add(middleText, BorderLayout.CENTER);

		JPanel opponentCharacterPanel = new JPanel();
		opponentCharacterPanel.setLayout(new BoxLayout(opponentCharacterPanel, BoxLayout.Y_AXIS));
		opponentCharacterPanel.setOpaque(false);
		opponentCharacterPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

		opponentHealthBar = new JProgressBar(0, 100);
		opponentHealthBar.setValue(100);
		opponentHealthBar.setForeground(Color.GREEN);
		opponentHealthBar.setStringPainted(true);
		opponentHealthBar.setPreferredSize(new Dimension(200, 20));

		opponentManaBar = new JProgressBar(0, 100);
		opponentManaBar.setValue(100);
		opponentManaBar.setForeground(Color.BLUE);
		opponentManaBar.setStringPainted(true);
		opponentManaBar.setPreferredSize(new Dimension(200, 20));

		JLabel opponentCharacter = new JLabel();
		opponentCharacter.setIcon(new ImageIcon(new ImageIcon("images/queens/" + opponentQueenName + ".png").getImage()
				.getScaledInstance(170, 170, Image.SCALE_SMOOTH)));

		JLabel opponentNameLabel = new JLabel(username2, SwingConstants.CENTER);
		opponentNameLabel.setForeground(Color.WHITE);
		opponentNameLabel.setFont(new Font("Serif", Font.BOLD, 18));

		opponentCharacterPanel.add(opponentHealthBar);
		opponentCharacterPanel.add(opponentManaBar);
		opponentCharacterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		opponentCharacterPanel.add(opponentCharacter);
		opponentCharacterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		opponentCharacterPanel.add(opponentNameLabel);

		JPanel rightWrapper = new JPanel(new BorderLayout());
		rightWrapper.setOpaque(false);
		rightWrapper.add(opponentCharacterPanel, BorderLayout.EAST);

		centerPanel.add(rightWrapper, BorderLayout.EAST);

		mainPanel.add(centerPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;

		JLabel spell1Label = new JLabel(spell1Name, SwingConstants.CENTER);
		spell1Label.setForeground(Color.WHITE);
		buttonPanel.add(spell1Label, gbc);

		gbc.gridy = 1;
		JButton spell1 = new JButton(new ImageIcon("images/spells/" + spell1Name + ".png"));
		spell1.setPreferredSize(new Dimension(100, 100));
		buttonPanel.add(spell1, gbc);

		gbc.gridy = 2;
		JTextArea spell1Description = new JTextArea(desc1);
		spell1Description.setForeground(Color.WHITE);
		spell1Description.setLineWrap(true);
		spell1Description.setWrapStyleWord(true);
		spell1Description.setOpaque(false);
		spell1Description.setEditable(false);
		spell1Description.setFocusable(false);
		buttonPanel.add(spell1Description, gbc);

		spell1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isTurn) {
					client.sendToServer("SPELL_CAST:" + playerQueenName + ":" + opponentQueenName + ":" + spell1Name);
				} else {
					showErrorMessage("It's not your turn!");
				}
			}
		});

		gbc.gridx = 1;
		gbc.gridy = 0;

		JLabel spell2Label = new JLabel(spell2Name, SwingConstants.CENTER);
		spell2Label.setForeground(Color.WHITE);
		buttonPanel.add(spell2Label, gbc);

		gbc.gridy = 1;
		JButton spell2 = new JButton(new ImageIcon("images/spells/" + spell2Name + ".png"));
		spell2.setPreferredSize(new Dimension(100, 100));
		buttonPanel.add(spell2, gbc);

		gbc.gridy = 2;
		JTextArea spell2Description = new JTextArea(desc2);
		spell2Description.setForeground(Color.WHITE);
		spell2Description.setLineWrap(true);
		spell2Description.setWrapStyleWord(true);
		spell2Description.setOpaque(false);
		spell2Description.setEditable(false);
		spell2Description.setFocusable(false);
		buttonPanel.add(spell2Description, gbc);

		spell2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isTurn) {
					client.sendToServer("SPELL_CAST:" + playerQueenName + ":" + opponentQueenName + ":" + spell2Name);
				} else {
					showErrorMessage("It's not your turn!");
				}
			}
		});

		gbc.gridx = 2;
		gbc.gridy = 0;

		JLabel spell3Label = new JLabel(spell3Name, SwingConstants.CENTER);
		spell3Label.setForeground(Color.WHITE);
		buttonPanel.add(spell3Label, gbc);

		gbc.gridy = 1;
		JButton spell3 = new JButton(new ImageIcon("images/spells/" + spell3Name + ".png"));
		spell3.setPreferredSize(new Dimension(100, 100));
		buttonPanel.add(spell3, gbc);

		gbc.gridy = 2;
		JTextArea spell3Description = new JTextArea(desc3);
		spell3Description.setForeground(Color.WHITE);
		spell3Description.setLineWrap(true);
		spell3Description.setWrapStyleWord(true);
		spell3Description.setOpaque(false);
		spell3Description.setEditable(false);
		spell3Description.setFocusable(false);
		buttonPanel.add(spell3Description, gbc);

		spell3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isTurn) {
					client.sendToServer("SPELL_CAST:" + playerQueenName + ":" + opponentQueenName + ":" + spell3Name);
				} else {
					showErrorMessage("It's not your turn!");
				}
			}
		});

		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;

		if (isTurn) {
			middleText.setText("Your turn");
		} else {
			middleText.setText("Opponent's Turn");
		}
	}

	private void updateBarColor(JProgressBar bar, int value) {
		if (value > 50) {
			bar.setForeground(Color.GREEN);
		} else if (value > 15) {
			bar.setForeground(Color.YELLOW);
		} else {
			bar.setForeground(Color.RED);
		}
	}

	public void updatePlayerHealth(int value) {
		playerHealthBar.setValue(value);
		updateBarColor(playerHealthBar, value);
	}

	public void updatePlayerMana(int value) {
		playerManaBar.setValue(value);
	}

	public void updateOpponentHealth(int value) {
		opponentHealthBar.setValue(value);
		updateBarColor(opponentHealthBar, value);
	}

	public void updateOpponentMana(int value) {
		opponentManaBar.setValue(value);
	}

	public void showErrorMessageHealReachedLimit() {
		JOptionPane.showMessageDialog(this, "Maximum number of healing has been reached !");
	}

	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	public class BackgroundPanel extends JPanel {
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
}

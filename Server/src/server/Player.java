package server;

public class Player {
	
	private int id;
	private String username;
	private String password;
	private String email;
	private int health;
	private int mana;
	private int queenId;
	
	public Player(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.health = 100;
		this.mana = 100;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getHealth() {
		return health;
	}

	public int getMana() {
		return mana;
	}

	public void setQueenId(int queenId) {
		this.queenId = queenId;
	}

	

	
	

}

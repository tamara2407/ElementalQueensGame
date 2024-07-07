package server;

public class Player {
    
    private int id;
    private String username;
    private String password;
    private String email;
    private int wins;
    private int losses;

    public Player(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.wins = 0; 
        this.losses = 0; 
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

    public String getEmail() {
        return email;
    }
    
    public int getWins() {
        return wins;
    }
    
    public int getLosses() {
        return losses;
    }

    public void incrementWins() {
        this.wins++;
    }

    public void incrementLosses() {
        this.losses++;
    }

    public void resetStats() {
        this.wins = 0;
        this.losses = 0;
    }
}


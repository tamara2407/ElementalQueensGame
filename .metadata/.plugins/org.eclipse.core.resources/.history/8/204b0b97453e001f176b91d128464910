package server;

import org.mindrot.jbcrypt.BCrypt;



public class Player {
    
    private int id;
    private String username;
    private String hashedPassword;
    private String email;
    private int wins;
    private int losses;
    private Queen selectedQueen;
   
    public Player(String username, String password, String email) {
        this.username = username;
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        this.email = email;
        this.wins = 0;
        this.losses = 0;
    }
    
//    public Player(String username, String hashedPassword, String email, int wins, int losses) {
//        this.username = username;
//        this.hashedPassword = hashedPassword;
//        this.email = email;
//        this.wins = wins;
//        this.losses = losses;
//    }
    
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
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
    
    public void setWins(int wins) {
        this.wins=wins;
    }
    
    public void setLosses(int losses) {
        this.losses=losses;
    }


    public void incrementWins() {
        this.wins++;
    }

    public void incrementLosses() {
        this.losses++;
    }

   
    public Queen getSelectedQueen() {
        return selectedQueen;
    }

    public void setSelectedQueen(Queen selectedQueen) {
        this.selectedQueen = selectedQueen;
    }
    
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}


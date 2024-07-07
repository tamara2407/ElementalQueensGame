package server;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Queen {
	
	

	    private int id;
	    private String name;
	    private int health;
	    private int mana;
	    private List<Spell> spells;

	    
	    public Queen(int id, String name) {
	        this.id = id;
	        this.name = name;
	        this.health = 100; 
	        this.mana = 100; 
	        this.spells = new ArrayList<>();
	    }
	    
	    public Queen() {
	    }
	    
	    public Queen(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("spells") List<Spell> spells,
                @JsonProperty("health") int health,
                @JsonProperty("mana") int mana) {
       this.id = id;
       this.name = name;
       this.spells = spells;
       this.health = health;
       this.mana = mana;
   }

	    
	    public int getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public int getHealth() {
	        return health;
	    }

	    public int getMana() {
	        return mana;
	    }
	    
	    public List<Spell> getSpells() {
	        return spells;
	    }

	    
	    public void addSpell(Spell spell) {
	        if (spells.size() < 3) {
	            spells.add(spell);
	        } else {
	            throw new IllegalStateException("A Queen can only have 3 spells.");
	        }
	    }

	    
	    public void setHealth(int health) {
	        this.health = health;
	    }

	    public void setMana(int mana) {
	        this.mana = mana;
	    }

	    
	    public void reduceHealth(int amount) {
	        this.health -= amount;
	        if (this.health < 0) {
	            this.health = 0; 
	        }
	    }

	    
	    public void reduceMana(int amount) {
	        this.mana -= amount;
	        if (this.mana < 0) {
	            this.mana = 0; 
	        }
	    }

	   
	    public void restoreHealth(int amount) {
	        this.health += amount;
	        if (this.health > 100) {
	            this.health = 100; 
	        }
	    }

	    
	    public void restoreMana(int amount) {
	        this.mana += amount;
	        if (this.mana > 100) {
	            this.mana = 100; 
	        }
	    }
	    
	    public void castSpell(Spell spell, Queen target) {
	        if (this.spells.contains(spell)) {
	            int effect = spell.getEffect();
	            if (effect < 0) {
	                target.reduceHealth(-effect);
	            } else {
	                this.restoreHealth(effect);
	            }
	        } else {
	            throw new IllegalArgumentException("This Queen does not know this spell.");
	        }
	    }
	    
	    public String toString() {
	        return "Queen{" +
	                "id=" + id +
	                ", name='" + name + '\'' +
	                ", health=" + health +
	                ", mana=" + mana +
	                ", spells=" + spells +
	                '}';
	    }
	


}

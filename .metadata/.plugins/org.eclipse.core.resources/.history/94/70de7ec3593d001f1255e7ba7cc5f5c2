package server;

import java.util.ArrayList;
import java.util.List;


public class Queen {
	
	

	    private int id;
	    private String name;
	    private int health;
	    private int mana;
	    private List<Spell> spells= new ArrayList<>();

	    
	    public Queen(int id, String name, List<Spell> spells, int health,int mana) {
	        this.id = id;
	        this.name = name;
	        this.health = health; 
	        this.mana = mana; 
	        this.spells = spells;
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
	    
	    public Spell getSpell(int i) {
	    	return spells.get(i);
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
	                ", spells=" + getSpell(0)+getSpell(1)+getSpell(2)+
	                '}';
	    }

	


}

package server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Spell {
	
	private String name;
    private int effect;
    private String description;
    private int manaCost;

    public Spell(String name, int effect, String description, int manaCost) {
        this.name = name;
        this.effect = effect;
        this.description=description;
        this.manaCost=manaCost;
    }
    
    public Spell() {
    }
    
    public Spell(@JsonProperty("name") String name,
            @JsonProperty("effect") int effect,
          
            @JsonProperty("manaCost") int manaCost) {
   this.name = name;
   this.effect=effect;
   this.manaCost = manaCost;
}

    
    public String getName() {
        return name;
    }

    public int getEffect() {
        return effect;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public int getManaCost() {
    	return manaCost;
    }
    
    public String toString() {
        return "Spell{" +
                "name='" + name + '\'' +
                ", effect=" + effect +
                ", manaCost=" + manaCost +
                '}';
    }

}

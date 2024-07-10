package server;



public class Spell {
	
	private String name;
    private int effect;
    private String description;
    private int manaCost;
    private int usageCount;

    public Spell(String name, int effect, String description, int manaCost) {
        this.name = name;
        this.effect = effect;
        this.description=description;
        this.manaCost=manaCost;
        this.usageCount = 0;
    }
    
    public Spell(Spell other) {
        this.name = other.name;
        this.description = other.description;
        this.manaCost = other.manaCost;
        this.effect = other.effect;
        this.usageCount = other.usageCount;
    }
    
    public int getUsageCount() {
        return usageCount;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }
    
    public void resertUsageCount(){
    	usageCount=0;
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
    

}

package actors;

public abstract class Doctor {
	public static String[] names = {"Bernard", "Dubois", "Lepetit", "Mercier", "Fontaine",
		"Robin", "Muller", "Dupont", "Dupond", "Perrin",
		"Boyer", "Duval", "Gautier", "Roger", "Noel",
		"Brun", "Charles", "Berger", "Schmitt", "Rodriguez"};
    protected Doctor next;
    private String name;
    
    public Doctor(String title, int n) {
        name = title + " " + names[n];
    }
    
    public Doctor(String title, int n, Doctor d) {
        this(title, n);
        next = d;
    }
    
    public void setNext(Doctor d) {
        if (!equals(d)) {
            next = d;
        }
    }
    
    //public abstract boolean canTreat(Victim v);
    public abstract void heal(Victim v);
    
    public boolean treat(Victim v) {
        heal(v);
        v.incTime();
        
        if (v.isDead()) return false;
        if (v.isHealed()) return true;
        
        if (next != null) {
            next.treat(v);
        }
        
        return false;
    }
}
  package game;

import actors.Victim;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KillerVsHealer {

	// D�marre le jeu
	public static void main(String[] args) {
            Victim victim = new Victim();
            Killer killer = new Killer(victim);
            killer.setNextRound(new Healer(victim));
		//killer.setNextRound(healer);
		//healer.setNextRound(killer);
	}

}

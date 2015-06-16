package interfaces;
import java.util.List;

// On considère que les liste d'entiers donnent dans l'ordre les 4 damages suivants :
// Tête - Bras - Corps- Jambes
public interface Round {
	void setDamages(List<Integer> damages);
	List<Integer> getDamages();
	void begin();
	void end();
	void setNextRound(Round round);
}

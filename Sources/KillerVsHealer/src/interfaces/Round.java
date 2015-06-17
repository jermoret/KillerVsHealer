package interfaces;
import java.util.List;

// On consid�re que les liste d'entiers donnent dans l'ordre les 4 damages suivants :
// T�te - Bras - Corps- Jambes
public interface Round {
	//void setDamages(List<Integer> damages);
	//List<Integer> getDamages();
	void begin();
	void end();
	void setNextRound(Round round);
}

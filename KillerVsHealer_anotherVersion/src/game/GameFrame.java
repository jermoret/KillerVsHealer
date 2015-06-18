package game;

import interfaces.Round;

import javax.swing.JFrame;
import javax.swing.Timer;

public abstract class GameFrame implements Round {
	protected JFrame frmKillerVsHealer;
	private Round nextRound; // Prochain round 
	protected boolean inProgress = false; // Jeu en cours
	protected Timer countdown;
	protected int countdownValue = 10; // Compte à rebours, en secondes
	
	@Override
	public void begin() {
		frmKillerVsHealer.setVisible(true);
	}
	
	@Override
	public void end() {
		frmKillerVsHealer.setVisible(false);
         		nextRound.begin(); 
	}
	
	@Override
	public void setNextRound(Round round) {
		nextRound = round;
	}
}
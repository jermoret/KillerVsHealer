package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import actors.Doctor;
import actors.Victim;
import interfaces.Round;

public class Healer extends GameFrame {
	private JPanel healerList;
	private JPanel healerChain;
	private List<Doctor> doctors;
	private List<Doctor> docChain;
	private Round nextRound; // fenetre Killer
	private Timer healTimer;
	
	private final int NUMCHAIN = 10;
	private final int NUMLIST = 4;
	
	public Healer() {
		initialize();
	}
	
private void initialize() {
		
		// Utiliser le look & feel de l'OS
		try {
			UIManager.setLookAndFeel(
			UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			    e.printStackTrace();
		}
		
		frmKillerVsHealer = new JFrame();
		frmKillerVsHealer.setResizable(false);
		frmKillerVsHealer.setTitle("Killer Vs Healer - Healer's turn");
		frmKillerVsHealer.setBounds(100, 100, 659, 659);
		frmKillerVsHealer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
		// ================= Generation + Panel des medecins =================
		
		Random r = new Random();
		doctors = new ArrayList<Doctor>();
		int dnl = Doctor.names.length;
		
		for (int i = 0; i < NUMLIST; i++) {
			int type = r.nextInt(4);
			int zone = r.nextInt(4);
			
			switch (type) {
			case 0:
				doctors.add(new Doctor("Médecin", r.nextInt(dnl)) {
					@Override
					public void heal(Victim v) {
						v.heal(r.nextInt(2) + 2, zone);
					}
				});
				break;
			case 1:
				doctors.add(new Doctor("Chirurgien", r.nextInt(dnl)) {
					@Override
					public void heal(Victim v) {
						v.heal(v.getDamage(zone) / (r.nextInt(2) + 2), zone);
					}
				});
				break;
			case 2:
				doctors.add(new Doctor("Sauveteur", r.nextInt(dnl)) {
					@Override
					public void heal(Victim v) {
						v.heal(v.getDamage(zone) % (r.nextInt(3) + 3), zone);
					}
				});
				break;
			case 3:
				doctors.add(new Doctor("Réanimateur", r.nextInt(dnl)) {
					@Override
					public void heal(Victim v) {
						v.addTime(r.nextInt(4) + 1);
					}
				});
				break;
			}
		}
		
		// ===================================================================
		
    	// ================= Panel de la chaine ==============================
		
		// ===================================================================
		
		// =================== Compte à rebours =======================
			countdown = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(--countdownValue == 0) {
					countdown.stop();
					end();
				}
			}
		});
		// ============================================================
		
		
		// =============  Bouton pr�t ======================================
		JButton btnPrt = new JButton("Pr\u00EAt !");
		btnPrt.setFocusable(false);
		btnPrt.setFocusTraversalKeysEnabled(false);
		btnPrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inProgress = true;
				countdown.start();
			}
		});
		// =================================================================
		
		
		frmKillerVsHealer.setVisible(true);
	}
	
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

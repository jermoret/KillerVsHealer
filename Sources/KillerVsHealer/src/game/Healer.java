package game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
	private List<JButton> bChain;
	private List<JButton> bList;
	private Round nextRound; // fenetre Killer
	private Timer healTimer;
	
	private final int NUMCHAIN = 4;
	private final int NUMLIST = 10;
	
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
		
		// ================= Panel de la chaine ==============================
		
		//TODO: vérifier que j'ai rien oublié dans cette section
		bChain = new ArrayList<JButton>();
		for (int i=0; i<NUMCHAIN; i++) {
			bChain.add(new JButton(""));
		}
		
		GridLayout gridChain = new GridLayout(1,NUMCHAIN);
		healerChain.setLayout(gridChain);
		for (JButton b: bChain) {
			healerChain.add(b);
		}
				
		// ===================================================================
    	
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
					{
						desc = "baisse de";
						healVal = r.nextInt(2) + 2;
					}
					@Override
					public void heal(Victim v) {
						v.heal(healVal, zone);
					}
				});
				break;
			case 1:
				doctors.add(new Doctor("Chirurgien", r.nextInt(dnl)) {
					{
						desc = "divise de";
						healVal = r.nextInt(2) + 2;
					}
					@Override
					public void heal(Victim v) {
						v.heal(v.getDamage(zone) / healVal, zone);
					}
				});
				break;
			case 2:
				doctors.add(new Doctor("Sauveteur", r.nextInt(dnl)) {
					{
						desc = "garde en-dessous de";
						healVal = r.nextInt(3) + 3;
					}
					@Override
					public void heal(Victim v) {
						v.heal(v.getDamage(zone) % healVal, zone);
					}
				});
				break;
			case 3:
				doctors.add(new Doctor("Réanimateur", r.nextInt(dnl)) {
					{
						desc = "réanime de";
						healVal = r.nextInt(4) + 1;
					}
					@Override
					public void heal(Victim v) {
						v.addTime(healVal);
					}
				});
				break;
			}
		}
		
		bList = new ArrayList<JButton>();
		for (Doctor d : doctors) {
			healerList.add(new JButton(d.toString()));
			//TODO: associer les boutons de bList à leurs docteurs un peu mieux
		}
		
		GridLayout gridList = new GridLayout(3,3);
		healerList.setLayout(gridList);
		
		//TODO: Complèter actionPerformed
		for (JButton b: bList) {
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//TODO: quand on clique, on veut que le docteur associé à ce bouton
					//		soit enlevé de bList et ajouté à bChain (ou vice-versa)
				}
			});
			healerList.add(b);
		}
		
		// ===================================================================
		
		// =================== Compte à rebours =======================
		countdown = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(--countdownValue == 0) {
					countdown.stop();
					end();
				}
				//TODO: Label.setText pour un label pour afficher le compte a rebours
				//		voir Killer.java
			}
		});
		// ============================================================
		
		// =============  Bouton pr�t ======================================
		JButton btnPrt = new JButton("Pr\u00EAt !");
		btnPrt.setFocusable(false);
		btnPrt.setFocusTraversalKeysEnabled(false);
		btnPrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO: rendre visible panels de chaine et liste
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
		
		for (int i=0; i<docChain.size()-1; i++) {
			docChain.get(i).setNext(docChain.get(i+1));
		}
		docChain.get(docChain.size()-1).setNext(docChain.get(0));
		
		nextRound.begin(); 
	}

	@Override
	public void setNextRound(Round round) {
		nextRound = round;
	}
}

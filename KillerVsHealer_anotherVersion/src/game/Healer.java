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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

public class Healer extends GameFrame {
	private JPanel healerList;
	private JPanel healerChain;
        private Container printZone;
	private List<Doctor> doctors;
	private List<Doctor> docChain;
	private List<JButton> bChain;
	private List<JButton> bList;
	private Round nextRound; // fenetre Killer
	private Timer healTimer;
        private JLabel lblTimer;
	
	private final int NUMCHAIN = 4;
	private final int NUMLIST = 10;
        private JPanel panelInfo;
        
        private Victim victim;
	
	public Healer(Victim victim) {
                this.victim = victim;
		initialize();
	}
	
private void initialize() {
		
                doctors = new ArrayList<>();
                docChain = new ArrayList<>();
                healerList = new JPanel();
                healerChain = new JPanel();
                bChain = new ArrayList<>();
                bList = new ArrayList<>();
                printZone = new JPanel();
                panelInfo = new JPanel();
                frmKillerVsHealer = new JFrame();
                
		// Utiliser le look & feel de l'OS
		try {
			UIManager.setLookAndFeel(
			UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			    e.printStackTrace();
		}
		
		frmKillerVsHealer.setResizable(false);
		frmKillerVsHealer.setTitle("Killer Vs Healer - Healer's turn");
		frmKillerVsHealer.setBounds(100, 100, 659, 659);
		frmKillerVsHealer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// ================= Panel de la chaine ==============================
		
		//TODO: vérifier que j'ai rien oublié dans cette section
		/*for (int i=0; i<NUMCHAIN; i++) {
			bChain.add(new JButton(""));
		}*/
						
		// ===================================================================
    	
		// ================= Generation + Panel des medecins =================
		
		Random r = new Random();
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
		int i = 0;
		for (Doctor d : doctors) {
			bList.add(new JButton(d.toString()));
                        healerList.add(bList.get(i++));
			//TODO: associer les boutons de bList à leurs docteurs un peu mieux
		}
		
		GridLayout gridList = new GridLayout(3,3);
		healerList.setLayout(gridList);
		
		//TODO: Complèter actionPerformed
		for (JButton b: bList) {
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
                                    bChain.add(b);
                                    bList.remove(b);
					//TODO: quand on clique, on veut que le docteur associé à ce bouton
					//		soit enlevé de bList et ajouté à bChain (ou vice-versa)
				}
			});
			healerChain.add(b);
		}
                
                GridLayout gridChain = new GridLayout(1,NUMCHAIN);
		healerChain.setLayout(gridChain);
                
                //TODO : Lorsqu'on clique sur un élément 
                for (JButton b: bChain) {
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
                                    bList.add(b);
                                    bChain.remove(b);
					//TODO: quand on clique, on veut que le docteur associé à ce bouton
					//		soit enlevé de bList et ajouté à bChain (ou vice-versa)
				}
			});
			healerList.add(b);
		}
                
		
		// ============================================================
                
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
                                lblTimer.setText(String.format("00:%02d", countdownValue));
			}
		});
		// ============================================================
                frmKillerVsHealer.getContentPane().add(printZone);
		printZone.setLayout(null);
                
                lblTimer = new JLabel("00:" + String.valueOf(countdownValue));
		lblTimer.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTimer.setBounds(599, 605, 44, 14);
		printZone.add(lblTimer);
                
		panelInfo.setBackground(Color.GRAY);
		panelInfo.setBounds(207, 252, 243, 160);
		printZone.add(panelInfo);
		panelInfo.setLayout(null);
		// =============  Bouton prêt ======================================
		JButton btnPrt = new JButton("Pr\u00EAt !");
		btnPrt.setFocusable(false);
		btnPrt.setFocusTraversalKeysEnabled(false);
		btnPrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                            panelInfo.setVisible(false);
                            healerChain.setVisible(true);
                            healerList.setVisible(true);
                            printZone = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                            printZone.add(healerChain);
                            printZone.add(healerList);
                            inProgress = true;
                            countdown.start();
                            frmKillerVsHealer.setVisible(true);
                            printZone.repaint();
                            treatVictim();
			}
		});
		// =================================================================
		
		btnPrt.setBounds(74, 108, 95, 41);
		panelInfo.add(btnPrt);
		btnPrt.setFont(new Font("Tahoma", Font.PLAIN, 26));
                
                JLabel lblCestVotreTour = new JLabel("<html>C'est \u00E0 votre tour <b>Docteur</b> ! <br>\r\nAdministrer les soins au pantin endommagé afin que celui ci puisse survivre à l'attaque de l'aggresseur. <br>\r\nD\u00E8s que vous \u00EAtez pr\u00EAt cliquez sur \"Pr\u00EAt\".</html>");
		lblCestVotreTour.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCestVotreTour.setVerticalAlignment(SwingConstants.TOP);
		lblCestVotreTour.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCestVotreTour.setBounds(10, 11, 223, 96);
		panelInfo.add(lblCestVotreTour);
                
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
        
        protected boolean treatVictim() {
            boolean alive = true;   // On suppose que la victime est vivante au début du traitement
            for(Doctor d : docChain) {
                alive = d.treat(victim);
            }
            
            return alive;
        }
}

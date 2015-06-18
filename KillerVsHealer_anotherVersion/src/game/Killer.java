package game;

import interfaces.Round;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.Color;

import javax.swing.SwingConstants;

import actors.Victim;
import actors.Victim.Area;

import java.awt.Component;

public class Killer extends GameFrame implements KeyListener {

	private JPanel animZone;
	private JPanel panelInfo; // Informations avant lancer
	
	private BufferedImage casualty; // Image du pantin
	private BufferedImage casualtyTouched;
	private double xPos, yPos; // Positions
	private double xOffset, yOffset; // D�placement
	private Random rand = new Random();
	private Timer throwTimer;
	private Timer blood;
	private int speed;
	private int sinceLastChangeDirectionIdx; // Pour une d�c�leration plus r�elle
	private JLabel lblTimer;
	private boolean casualtyIsTouched = false; // Pantin touch�
        
	// D�gats
	//private int legDamage, armDamage, bodyDamage, headDamage = 0;
	private Victim victim = new Victim();
	
	// Variables du mouvement
	private final int BASE_SPEED = 15;
	private final int DECELERATION_INTERVAL = 12;

	/**
	 * Create the application.
	 */
	public Killer(Victim v) {
                victim = v;
		initialize();
	}

	/**
	 * Place le pantin au milieu de la fen�tre
	 */
	private void moveCasualtyAtCenter() {
		// Placement du pantin au milieu de la fen�tre
    	xPos = frmKillerVsHealer.getWidth() / 2 - casualty.getWidth() / 2;
    	yPos = frmKillerVsHealer.getHeight() / 2 - casualty.getHeight() / 2;
	}
	
	/**
	 * Change l'image du pantin lors d'une collision pendant 250 ms
	 */
	private void collision() {
		casualtyIsTouched = true;
		
		blood = new Timer(250 , new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				casualtyIsTouched = false;
				blood.stop(); 
			}
		});
		blood.start();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
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
		frmKillerVsHealer.setTitle("Killer Vs Healer - Killer's turn");
		frmKillerVsHealer.setBounds(100, 100, 659, 659);
		frmKillerVsHealer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Chargement de l'image du pantin depuis le package killer.images
		String imgPath = "../killer/images/casualty.png";
		String imgTouchPath = "../killer/images/casualtyTouched.png";
    	try {
			casualty = ImageIO.read(getClass().getResourceAsStream(imgPath));
			casualtyTouched = ImageIO.read(getClass().getResourceAsStream(imgTouchPath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	moveCasualtyAtCenter();
    	
    	// ================= Panel de d�placement du pantin ==================
		animZone = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				BufferedImage casualty = Killer.this.casualty;
				if(casualtyIsTouched)
					casualty = casualtyTouched;  
				g.drawImage(casualty, (int)xPos, (int)yPos, this);
			}
		};
		// ===================================================================
		
		// ================ Timer - Déplacement du pantin ====================
		throwTimer = new Timer(5, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Collision bord gauche
			    if ((int)xPos <= 0) {
			    	collision();
			        xOffset *= -1;  
			        xPos = 0;
			        if(yPos <= animZone.getHeight() / 2 - casualty.getHeight() / 2)
			        	victim.damage(1, Area.ARM);
			        else 
			        	victim.damage(1, Area.BODY);
			    }
			    
			    // Collision bord droit
			    if((int)xPos >= animZone.getWidth() - casualty.getWidth()) {
			    	collision();
			    	xOffset *= -1;
			    	xPos = animZone.getWidth() - casualty.getWidth();
			    	if(yPos <= animZone.getHeight() / 2 - casualty.getHeight() / 2)
			    		victim.damage(1, Area.ARM);
			        else 
			        	victim.damage(1, Area.BODY);
			    }

			    // Collision bord haut
			    if ((int)yPos <= 0) {
			    	collision();
			        yOffset *= -1;
			        yPos = 0;
			        victim.damage(1, Area.HEAD);
			    }
			    
			    // Collision bord bas
			    if((int)yPos >= animZone.getHeight()- casualty.getHeight()) {
			    	collision();
			    	yOffset *= -1;
			    	yPos = animZone.getHeight() - casualty.getHeight();
			    	victim.damage(1, Area.LEG);
			    }
			    
			    // Déplacement
				xPos += xOffset * speed;
			    yPos += yOffset * speed;
			   
			    // Décélération
			    if(sinceLastChangeDirectionIdx++ % DECELERATION_INTERVAL == 0)
			    	speed /= 2;
			    
			    if(speed == 0) { 
			    	throwTimer.stop();
			    	if(inProgress == false) {
                                    end();  
                                }
			    }
			    
			    animZone.repaint();
			}
		});
		// ============================================================
		
		// =================== Compte à rebours =======================
		countdown = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(--countdownValue == 0) {
					countdown.stop();
					inProgress = false;
				}	
				lblTimer.setText(String.format("00:%02d", countdownValue));
			}
		});
		// ============================================================
		frmKillerVsHealer.getContentPane().add(animZone);
		animZone.setLayout(null);
		
		lblTimer = new JLabel("00:" + String.valueOf(countdownValue));
		lblTimer.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTimer.setBounds(599, 605, 44, 14);
		animZone.add(lblTimer);
		
		panelInfo = new JPanel();
		panelInfo.setBackground(Color.GRAY);
		panelInfo.setBounds(207, 252, 243, 160);
		animZone.add(panelInfo);
		panelInfo.setLayout(null);
		
		// =============  Bouton prêt ======================================
		JButton btnPrt = new JButton("Pr\u00EAt !");
		btnPrt.setFocusable(false);
		btnPrt.setFocusTraversalKeysEnabled(false);
		btnPrt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelInfo.setVisible(false);
				inProgress = true;
				countdown.start();
			}
		});
		// =================================================================
		
		btnPrt.setBounds(74, 108, 95, 41);
		panelInfo.add(btnPrt);
		btnPrt.setFont(new Font("Tahoma", Font.PLAIN, 26));
		
		JLabel lblCestVotreTour = new JLabel("<html>C'est \u00E0 votre tour <b>tueur</b> ! <br>\r\nAmochez le plus possible le pantin en utilisant la barre d'<i>ESPACE</i> pour le projeter. <br>\r\nD\u00E8s que vous \u00EAtez pr\u00EAt cliquez sur \"Pr\u00EAt\".</html>");
		lblCestVotreTour.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCestVotreTour.setVerticalAlignment(SwingConstants.TOP);
		lblCestVotreTour.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCestVotreTour.setBounds(10, 11, 223, 96);
		panelInfo.add(lblCestVotreTour);
		
		frmKillerVsHealer.addKeyListener(this);
		frmKillerVsHealer.setVisible(true);
                
                casualtyIsTouched = true;
	}

	/**
	 * Pression sur la barre "ESPACE"
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(inProgress) {
				int angle = rand.nextInt(360) + 1;
				xOffset = Math.sin(Math.toRadians(angle));
		        yOffset = Math.cos(Math.toRadians(angle));
		        sinceLastChangeDirectionIdx = 1;
		        speed = BASE_SPEED;
		        throwTimer.start();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void begin() {
		panelInfo.setVisible(true);
		moveCasualtyAtCenter();
		super.begin();
	}

    public Victim getVictim() {
        return victim;
    }

    public boolean isCasualtyIsTouched() {
        return casualtyIsTouched;
    }    
    
}

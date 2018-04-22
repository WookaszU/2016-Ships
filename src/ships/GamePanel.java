package ships;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private PlayerShipBoard playerBoard;		// gracz ustawia tutaj swoje statki
private ShootBoard playerShootBoard;		// gracz strzela tutaj w statki przeciwnika
private JLabel[] npcinfoLabels;
 

	public GamePanel(){
		
		Player user = new Player();
		Opponent npc = new Opponent();
		
        this.playerBoard = new PlayerShipBoard(user);
		playerBoard.setOpaque(true);
        playerBoard.setBounds(0, 0, 495, 449);       
                       
        this.playerShootBoard = new ShootBoard(user,npc);	
        playerShootBoard.setOpaque(true);
        playerShootBoard.setBounds(530, 0, 495, 449);
        
        this.setLayout(null);
        this.add(playerBoard);
        this.add(playerShootBoard);
        JLabel npcships = new JLabel("Statki przeciwnika:");
        npcships.setForeground(Color.WHITE);
        npcships.setBounds(1045, 200, 150, 30);
        this.add(npcships);
        
                
        int locationBoundsY = 225;
        int shipCount = 4;
        npcinfoLabels = new JLabel [4];
        for(int i = 0; i < 4 ; i++){
        	npcinfoLabels[i] = new JLabel(String.valueOf(i+1)+"Maszt: "+String.valueOf(shipCount));
        	npcinfoLabels[i].setBounds(1045, locationBoundsY, 150, 30);
        	npcinfoLabels[i].setForeground(Color.WHITE);
            this.add(npcinfoLabels[i]);
            locationBoundsY += 20;
            shipCount--;
        }
        
	}
	
	public PlayerShipBoard getPlayerShipBoard(){
		return playerBoard;
	}
	
	public ShootBoard getShootBoard(){
		return playerShootBoard;
	}
	
	//wprowadzam jaki statek zostal zniszczony rozmiar   1-4
	public void decShipCount(int shipSize){		
		npcinfoLabels[shipSize-1].setText( String.valueOf(shipSize)+"Maszt: "+String.valueOf( playerShootBoard.getNPC().getDecshipCount(shipSize)));		
	}

}

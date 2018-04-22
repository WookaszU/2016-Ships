package ships;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerShipBoard extends JPanel implements MouseListener {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int shipSize = 4;
	
	private boolean orientation = true;		// true  poziomo    false pionowo
	private boolean delete = true;

	private int sizeBoard = 13;
	private JLabel[][] labelsBoard = new JLabel[13][13];
	
	private boolean inserted = false;	
	private boolean shipContacting = false;	
	private Player user;
			
	private BufferedImage gameBoardImage;

	
	public PlayerShipBoard(Player gracz){		
		this.setLayout(null);
		this.user = gracz;
		initshipPlaceMap();
		this.setLayout(new GridLayout(14,14));
		
		for(int i = 0; i < 14; i++){
			for(int j = 0; j < 14; j++){
				
				if(i!=0 && j!=0){								
					labelsBoard[i-1][j-1] = new JLabel();
					gracz.setStG(i-1, j-1, null);				
					labelsBoard[i-1][j-1].addMouseListener(this);								
					this.add(labelsBoard[i-1][j-1]);				
				}
				else{
					this.add(new JLabel());
				}				
			}
		}				
	}
	
	public void decshipSize(){
		shipSize--;
	}
	
	public Player getPlayer(){
		return user;
	}
	
	public void setPlayer(Player nowygracz){
		this.user = nowygracz;
	}
	
	public boolean getInserted(){
		return inserted;
	}

	public void setInserted(boolean zmiana){
		inserted = zmiana;
	}
	
	
	private void initshipPlaceMap(){

		try {
			gameBoardImage = ImageIO.read(PlayerShipBoard.class.getResource("pictures/plansza.png"));				//imageFile
		} catch (IOException e) {
			System.err.println( "Blad odczytu obrazka \nObraz planszy gry nie zostal zaladowany!" );
			e.printStackTrace();
		}

		
		int w = gameBoardImage.getWidth( this );
		int h = gameBoardImage.getHeight( this );
				
		setPreferredSize( new Dimension(w,h) );
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = ( Graphics2D ) g;
		g2d.drawImage(gameBoardImage, 0, 0, this);
	}
	
	//sprawdzanie czy pola sa puste
	private boolean isPossibleToPlace(int x, int y, boolean orientacja, int shipSize){
		
		if( orientacja ){
			if(y+shipSize > sizeBoard) return false;
			if( !shipContacting && !checkContactingPlayerShips(x, y, true) ) return false;
			for(int i=0; i < shipSize ; i++){
				if(  user.getStG( x , y+i ) != null) return false;    
			}
			return true;		
		}
		else{
			if(x+shipSize > sizeBoard)return false;
			if( !shipContacting && !checkContactingPlayerShips(x, y, false) ) return false;
			for(int i=0; i < shipSize ; i++){
				if( user.getStG( x+i , y) != null) return false;
			}
			return true;
		}
	}
	
	// rysowanie statku
	private void drawShip(int x, int y){
		
		if( orientation ){
			
			if(y + shipSize > sizeBoard) return;
		
			Ship newship = new Ship(shipSize);
			for(int k = 0;k < shipSize;k++){
				
				labelsBoard[x][y+k].setBackground( Color.black );
				labelsBoard[x][y+k].setOpaque( true );
				user.setStG(x, y+k, newship);
				inserted = true;
				NewGame.moveDecLatch();
			}
		}
		
		else{
			if(x + shipSize > sizeBoard) return;
			
			Ship newship = new Ship( shipSize );
			for(int k = 0; k < shipSize; k++){
				
				labelsBoard[x+k][y].setBackground( Color.black );
				labelsBoard[x+k][y].setOpaque( true );				
				user.setStG(x+k, y, newship);
				inserted = true;				
				NewGame.moveDecLatch();
			}			
		}		
	}
	
	public void mouseClicked(MouseEvent event){}
	 
	public void mousePressed(MouseEvent e){
			
		JLabel x = (JLabel) e.getSource();
		
		int ti=0;
		int tj=0;
		
		for(int i=0; i < sizeBoard; i++){
			for(int j=0; j < sizeBoard; j++){
				if(x == labelsBoard[i][j]){
					ti = i;
					tj = j;
				}
			}
		}
		
		///LBM
		if(e.getButton() == MouseEvent.BUTTON1){
			if( isPossibleToPlace(ti, tj, orientation, shipSize) ){
				drawShip(ti, tj);
				delete = false;
			}
		}
		
		
		///RBM
		if( e.getButton() == MouseEvent.BUTTON3 ){
		boolean drawdifforient = true;	
			if( orientation ){
				if( !shipContacting && !checkContactingPlayerShips(ti, tj, false) ) drawdifforient = false; 
				if(tj+shipSize <= sizeBoard){
					for(int i=0; i < shipSize; i++){
						if( user.getStG(ti, tj+i) == null){			
							labelsBoard[ti][tj+i].setBackground( Color.white );
							labelsBoard[ti][tj+i].setOpaque( false );
						}
						if( drawdifforient ){
							if( ti+shipSize <= sizeBoard && user.getStG(ti+i, tj) == null ){
							
								labelsBoard[ti+i][tj].setBackground( Color.GRAY );
								labelsBoard[ti+i][tj].setOpaque( true );
							}	
						}
					}
			
				}
			}
		else {	
				if( !shipContacting && !checkContactingPlayerShips(ti, tj, true) ) drawdifforient = false;
				if(ti+shipSize <= sizeBoard){
					for(int i=0; i < shipSize; i++){
						if(user.getStG(ti+i, tj) == null){
							labelsBoard[ti+i][tj].setBackground( Color.white );
							labelsBoard[ti+i][tj].setOpaque( false );
						}
						if( drawdifforient ){
							if(tj+shipSize <= sizeBoard && user.getStG(ti, tj+i) == null){
								labelsBoard[ti][tj+i].setBackground( Color.GRAY );
								labelsBoard[ti][tj+i].setOpaque( true );
							}
						}
					}
				}
			}
		orientation = !orientation;
		}
	}
	 
	public void mouseReleased(MouseEvent event){}
	 
	public void mouseEntered(MouseEvent e){
		
		
		JLabel x = (JLabel) e.getSource();
		
		int ti = 0;
		int tj = 0;
		
		for(int i=0; i < sizeBoard; i++){
			for(int j=0; j < sizeBoard; j++){
				if(x == labelsBoard[i][j]){
					ti = i;
					tj = j;
				}
			}
		}
				
		if( orientation ){
		
			if( tj + shipSize > sizeBoard ) return;
			if( !shipContacting && !checkContactingPlayerShips(ti, tj, true) ) return;
			
			for(int k = 0 ; k < shipSize ; k++){
				if(user.getStG(ti, tj+k) == null){
					labelsBoard[ti][tj+k].setBackground( Color.GRAY );
					labelsBoard[ti][tj+k].setOpaque( true );
				}
			}
		}
		
		else{
			if(ti + shipSize > sizeBoard) return;
			if( !shipContacting && !checkContactingPlayerShips(ti, tj, false) ) return;
			
			for(int k = 0; k < shipSize; k++){
				if(user.getStG(ti+k, tj) == null){
					labelsBoard[ti+k][tj].setBackground( Color.GRAY );
					labelsBoard[ti+k][tj].setOpaque( true );
				}
			}
		}
			
	}
	 
	//metoda wywo�ywana, gdy kursor opuszcza obszar nas�uchuj�cy zdarzenia
	public void mouseExited(MouseEvent e){
		if(delete == false){
			delete = true;
			return;
		}
		JLabel x = (JLabel) e.getSource();
		
		int ti=0;
		int tj=0;
		
		for(int i=0; i < sizeBoard; i++){
			for(int j=0;j < sizeBoard; j++){
				if(x == labelsBoard[i][j]){
					ti = i;
					tj = j;
				}
			}
		}
				
		if(orientation){
		
			if(tj + shipSize > sizeBoard) return;
		
			
			for(int k = 0; k < shipSize; k++){
				if(user.getStG(ti, tj+k) == null){
					labelsBoard[ti][tj+k].setBackground( Color.white );
					labelsBoard[ti][tj+k].setOpaque( false );			
				}
			}
		}
		
		else{
			if(ti + shipSize > sizeBoard) return;
					
			for(int k = 0; k < shipSize; k++){
				if(user.getStG(ti+k, tj) == null){
					labelsBoard[ti+k][tj].setBackground( Color.white );
					labelsBoard[ti+k][tj].setOpaque( false );
				}
			}
			
		}
					
		
	}
	
	//zmiana grafiki pola na mapie po strzale
	public void shootedPlayer(int x, int y, boolean trafiony){
		if(trafiony){
			labelsBoard[x][y].setIcon(
					new ImageIcon(ShootBoard.class.getResource("pictures/trafiony.png")) );
		}
		else labelsBoard[x][y].setIcon(
				new ImageIcon(ShootBoard.class.getResource("pictures/pustystrzal.png")) );
	}
	
	
	// false jesli sie stykaja, true jesli nie
	private boolean checkContactingPlayerShips(int x, int y, boolean orientacja){
			if( orientacja ){
				if(x-1 >= 0 && y - 1 >= 0){
					if(user.getStG(x-1, y-1) != null) return false;
				}
				if(y-1 >= 0){
					if(user.getStG(x, y-1) != null) return false;
				}
				if(x+1 <sizeBoard && y-1 >= 0){
					if(user.getStG(x+1, y-1) != null) return false;
				}
				if(y+shipSize < sizeBoard){
					if(user.getStG(x,y+shipSize) != null) return false;
				}
				if(x-1 >= 0 && shipSize+y < sizeBoard){
					if(user.getStG(x-1,y+shipSize) != null) return false;
				}
				if(x+1 < sizeBoard && shipSize+y < sizeBoard){
					if(user.getStG(x+1,y+shipSize) != null) return false;
				}
				if(x-1 >= 0  ){
					if(shipSize+y > sizeBoard) return false;
					for(int i=0; i < shipSize; i++){
						if(user.getStG(x-1,y+i) != null)return false;
					}
				}
				if(x+1 < sizeBoard ){
					if( shipSize+y > sizeBoard) return false;
					for(int i=0; i < shipSize; i++){
						if(user.getStG(x+1,y+i) != null)return false;
					}
				}
			}
			else{
				if(x-1 >= 0 && y - 1 >= 0){
					if(user.getStG(x-1,y-1) != null) return false;
				}
				if(x-1 >= 0){
					if(user.getStG(x-1,y) != null) return false;
				}
				if(y+1 <sizeBoard && x-1>=0){
					if(user.getStG(x-1,y+1) != null) return false;
				}
				if(x+shipSize < sizeBoard){
					if(user.getStG(x+shipSize,y) != null) return false;
				}
				if(y-1 >= 0 && shipSize+x < sizeBoard){
					if(user.getStG (shipSize+x , y-1) != null) return false;
				}
				if(y+1 < sizeBoard && shipSize+x < sizeBoard){
					if(user.getStG(shipSize+x,y+1) != null) return false;
				}
				if(y-1 >= 0 ){
					if( shipSize+x > sizeBoard) return false;
					for(int i=0; i < shipSize; i++){
						if(user.getStG(x+i,y-1) != null) return false;
					}
				}
				if(y+1<sizeBoard ){
					if( shipSize+x > sizeBoard) return false;
					for(int i=0; i < shipSize; i++){
						if(user.getStG(x+i,y+1) != null) return false;
					}
				}
			}
			return true;
		}
		
}

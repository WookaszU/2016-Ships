package ships;


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

public class ShootBoard extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BufferedImage board;
	
	private int sizeBoard = 13;
	private JLabel[][] shotBoardlabel = new JLabel[13][13];	
	private Opponent npc;									
	private Player user;
	private boolean playermove = false;
	
	public ShootBoard( Player gracz , Opponent npc ){   
		this.setLayout( null );
		this.npc = npc;
		this.user = gracz;
		initShootBoard();
		
		this.setLayout( new GridLayout( 14, 14 ) );
		
		for( int i=0; i<14; i++ ){
			for( int j=0 ;j<14; j++ ){
				
				if( i != 0 && j != 0 ){
								
					shotBoardlabel[i-1][j-1] = new JLabel();				
					shotBoardlabel[i-1][j-1].addMouseListener( this );				
					this.add( shotBoardlabel[i-1][j-1] );				
				}
				else{
					this.add( new JLabel() );
				}				
			}
		}						
	}
	
	public Player getPlayer(){
		return user;
	}
	
	public void setPlayer( Player newplayer ){
		this.user = newplayer;
	}
	
	public Opponent getNpc(){
		return npc;
	}
	
	public void setNpc( Opponent nowyprzeciwnik ){
		this.npc = nowyprzeciwnik;
	}
	

	private void initShootBoard(){
		
		try {
			board = ImageIO.read( ShootBoard.class.getResource("pictures/plansza.png") );
						
		} 
		catch ( IOException e ) {
			System.err.println( "Blad odczytu obrazka! \nObraz planszy gry nie zostal zaladowany!" );
			e.printStackTrace();
		}
		
		int w = board.getWidth(this);
		int h = board.getHeight(this);
		
		setPreferredSize(new Dimension(w,h));
		
		
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		Graphics2D g2d = ( Graphics2D ) g;
		g2d.drawImage( board, 0, 0, this );
	}

	
	private TabLocation getLocationInTab( JLabel x ){
		
		int ti=0;
		int tj=0;
		
		for( int i=0; i<sizeBoard; i++ ){
			for( int j=0; j<sizeBoard; j++ ){
				if( x == shotBoardlabel[i][j] ){
					ti = i;
					tj = j;
				}
			}
		}
		return new TabLocation(ti,tj);
	}
	
	public void mouseClicked(MouseEvent event){
	}
	 

	public void mousePressed(MouseEvent e){
		if( !playermove ) return;
		JLabel x = ( JLabel ) e.getSource();
		TabLocation tloc = getLocationInTab( x );
		if( user.getmapshoot( tloc.x, tloc.y ) )return;
		
		if( npc.playerShootCheck( tloc.x, tloc.y) ) {
			shotBoardlabel[tloc.x][tloc.y].setIcon(
					new ImageIcon(ShootBoard.class.getResource("pictures/trafiony.png")) );
			
		}
		else shotBoardlabel[tloc.x][tloc.y].setIcon(
				new ImageIcon(ShootBoard.class.getResource("pictures/pustystrzal.png")) );
		
		NewGame.moveDecLatch();
		user.setmapshootTrue( tloc.x, tloc.y );
		
		
	}
	 
	public void mouseReleased(MouseEvent event){
	}
	 
	public void mouseEntered(MouseEvent event){
	}
	 
	public void mouseExited(MouseEvent event){
	}

	public void setRound( boolean change ){
		playermove = change;
	}
	
	public Opponent getNPC(){
		return npc;
	}
	
	public void endGameShowShips(){
				
		for(int i = 0; i < sizeBoard; i++){
			for(int j = 0; j < sizeBoard; j++){				
				if(npc.getfromshiploc(i, j) != null){					
					shotBoardlabel[i][j].setIcon(
							new ImageIcon( ShootBoard.class.getResource("pictures/ujawnijstatek.png")  ) );
				}
			}
		}
	}
	
}

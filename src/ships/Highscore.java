package ships;


import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Highscore extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable scoreTable;
	
	public Highscore(){	
		
		scoreTable = inHighscores();		
		scoreTable.setFillsViewportHeight( true );
		scoreTable.setPreferredScrollableViewportSize( new Dimension( 700 , 160 ) );
		JScrollPane sp = new JScrollPane( scoreTable );		
		this.add( sp );
		sp.setVisible( true );
	}

	public JTable inHighscores(){
		
		Object [][] data = new Object [10][3];
		LinkedList<HighscoreGame> listGames = HighscoreGame.getBestGames();
		
		if( listGames != null ){
			for( int i = 0 ; i < listGames.size() ; i++ ){
				data[i][0] = i+1;
				data[i][1] = listGames.get(i).getnumberMoves();
				data[i][2] = listGames.get(i).getDate(); 
			}
			String [] columnnames = { "Position" , "Shoots" , "Game date" };
			
			HighscoreGame.HighscoresBackIn( listGames );
			return new JTable( data , columnnames );
		}
		
		return new JTable();
	}
	
}

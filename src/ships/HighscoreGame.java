package ships;


import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;


public class HighscoreGame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String playerName;
	private int	numberMoves;
	private  LocalDateTime gameDate;
	
	public HighscoreGame( String playerName , int ruchy ){
		this.playerName = playerName;
		this.numberMoves = ruchy;
		this.gameDate = LocalDateTime.now();		
	}
	
	public int getnumberMoves(){
		return this.numberMoves;
	}
	
	public String getplayerName(){
		return playerName;
	}
	
	public LocalDateTime getDate(){
		return gameDate;
	}
	
	public boolean gameAdd(){
		
		ObjectOutputStream out = null;
		LinkedList<HighscoreGame> listGames = null;
		
		try { 
			
			listGames = getBestGames();			
			out = new ObjectOutputStream( new FileOutputStream ( ( "data.txt" ) ) );
			
			if( listGames != null ){
				
				int max = 0;
				for(int i=1 ; i < listGames.size() ; i++){
					if( listGames.get(i).getnumberMoves() > listGames.get(max).getnumberMoves() ) max = i ;
				}
				if( listGames.size() == 10 && listGames.get(max).getnumberMoves() < this.getnumberMoves() ){
					
					for(int i=0 ; i < listGames.size() ; i++){
						out.writeObject( listGames.get(i) );
					}					
					return false;
				}
				

				if( listGames.size() < 10 ){
					listGames.add( this );
				}
				
				else{
										
					int min = 0;	// najgorszy wynik gry 
				
					for( int i = 1 ; i < listGames.size() ; i++ ){
						if( listGames.get(min).getnumberMoves() < listGames.get(i).getnumberMoves() ) min = i;
					}
					
					listGames.remove( min );
					listGames.add( this );
				}
				
				listGames.sort( new Komparator() );
				
				for( int i=0; i < listGames.size(); i++ ){
					out.writeObject( listGames.get(i) );
				}
			}
			else{
				out.writeObject( this );
				
			}
			return true;
		} 

		catch(IOException e){
			e.printStackTrace();

		}

		finally{
			if(out != null )
				try{	
					out.close();
				   }
				catch(IOException e){}
		}
		return false;
	}
	
	// zwraca najlepsze wyniki zapisane w pliku
	public static LinkedList<HighscoreGame> getBestGames(){
		
		ObjectInputStream inp = null;		
		LinkedList <HighscoreGame> listGames = new LinkedList<HighscoreGame>();
		
		
		try {
			
			Path path = Paths.get( "data.txt" );					
			byte [] data = Files.readAllBytes(path);			
			inp = new ObjectInputStream( new ByteArrayInputStream((data)));
						
			while(true){					
				listGames.add( (HighscoreGame)inp.readObject() );				
			}			
		}		
		catch( EOFException eof ){
			return listGames;
		}
		
		catch( FileNotFoundException d ){
			System.out.println( "FileNotFound" );
			}
		catch( IOException f ){
			return null;			
			}
		catch( ClassNotFoundException g ){
			System.out.println( "ClassNotFound" );
			}
		

		finally{
			if( inp != null )
				try{	
					inp.close();
				   }
				catch( IOException e ){}
			
			}		
		return null;
	}
	
	//wpisuje dane do pliku	
	public static void HighscoresBackIn( LinkedList<HighscoreGame> gameScores ){
		ObjectOutputStream out = null;

		try { 
			out = new ObjectOutputStream( new FileOutputStream ( ("data.txt") ) );
			
			for( int i = 0 ; i < gameScores.size() ; i++ ){
				out.writeObject( gameScores.get(i) );
			}

			
		} 


		catch( IOException e ){
			e.printStackTrace();

		}

		finally{
			if( out != null )
				try{	
					out.close();
				   }
				catch( IOException e ){}

		}

	}
	
		
	class Komparator implements Comparator <HighscoreGame>{
				
			public int compare( HighscoreGame a , HighscoreGame b ){
				
				if( a.getnumberMoves() == b.getnumberMoves() )return 0;
				if( a.getnumberMoves() > b.getnumberMoves() ) return 1;		//zamienione 1 z -1 bo sortujemy odwrotnie im mniejszy wynik
				else return -1;									//tym jest lepszy
			}			
	}
		
}

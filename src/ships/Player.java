package ships;

public class Player {
	
	private int numberofshoots;
	private Ship[][] playerships = new Ship[13][13];
	private boolean[][] playershoots = new boolean[13][13];
	private int sizeBoard = 13;

	
	public Player(){
		this.numberofshoots = 0;
		this.playerships = new Ship[sizeBoard][sizeBoard];
		this.playershoots = new boolean[sizeBoard][sizeBoard];
		
			for( int i=0; i < sizeBoard; i++ ){
				for( int j=0; j< sizeBoard; j++ ){
					playerships[i][j] = null;
					playershoots[i][j] = false;
				}
			}
	}
	
	public int getNumberofshoots(){
		return numberofshoots;
	}
	public void addshot(){
		numberofshoots++;
	}
		
	//sprawdza czy przeciwnik trafil nasz statek
	// gdy nietrafiony zwraca 0,   gdy trafiony  -1 ,  gdy trafiony+zniszczony rozmiar statku
	public int opponentShootCheck(int x, int y){
		if( playerships[x][y] != null ){
			
			if( playerships[x][y].isDestroyed() ){
				int tempshipSize = playerships[x][y].getshipSize();
				playerships[x][y] = null;
				return tempshipSize;
			}
			playerships[x][y] = null;
			return -1;
		}
		return 0;
	}
	
	public void setStG( int x, int y, Ship val ){
		playerships[x][y] = val;
	}

	public Ship getStG( int x, int y ){
		return playerships[x][y];
	}
	
	public void setmapshootTrue( int x, int y ){
		playershoots[x][y] = true;
	}
	
	public boolean getmapshoot( int x, int y ){
		return playershoots[x][y];
	}
		
}

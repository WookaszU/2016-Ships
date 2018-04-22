package ships;

import java.util.LinkedList;
import java.util.Random;

public class Opponent {
	
	
	private Ship[][] opponentShips = new Ship[13][13];
	private boolean[][] opponentShootTable = new boolean[13][13];
	private int sizeBoard = 13;	
	private int dmgLastRound;  // otrzymane straty w ostatniej rundzie	
	
	private boolean located;
	private boolean lastMove;
	private boolean secondlastMove;
	private TabLocation hitLocation1;
	private TabLocation hitLocation2;
	private TabLocation hitLocation3;	
	private LinkedList<TabLocation> possibleshoots;			//lista pol gdzie mozna strzelic
	private int[] opponentShipCounter = {4,3,2,1};
	
	
	
	public Opponent(){

		this.opponentShips = new Ship[sizeBoard][sizeBoard];
		this.opponentShootTable = new boolean[sizeBoard][sizeBoard];
		this.possibleshoots = new LinkedList<TabLocation>();
		
		this.lastMove = false;
		this.located = false;
			for(int i=0; i < sizeBoard; i++){
				for(int j=0; j< sizeBoard; j++){
					opponentShips[i][j] = null;
					opponentShootTable[i][j] = false;
					possibleshoots.add( new TabLocation( i, j) );
				}
			}
		this.setShips();
	}
		
	//sprawdza trafilismy statek przeciwnika
	//gdy nietrafiony -1    gdy trafiony 0   gdy zatopiono  rozmiar statku
	public boolean playerShootCheck(int x, int y){
		if( opponentShips[x][y] != null ){
			
			if(opponentShips[x][y].isDestroyed()){
				int shipSize = opponentShips[x][y].getshipSize();
				opponentShips[x][y] = null;
				this.dmgLastRound = shipSize;		
				return true;
			}
			opponentShips[x][y] = null;
			this.dmgLastRound = 0;
			return true;
		}
		else this.dmgLastRound = -1;
		return false;
	}
	
	
	public int getLRdmg(){
		return this.dmgLastRound;
	}
	
	
	//losowy strzal    
	private TabLocation randowShoot(){
		Random generator = new Random();
				
		int numofcases = possibleshoots.size();
		int choosen = generator.nextInt( numofcases );
		TabLocation ruch = possibleshoots.get( choosen );
		
		return ruch;		
	}
	
	private TabLocation shootinarea(TabLocation okolica){
		Random generator = new Random();
		int x1= 0,y1= 0;
		LinkedList<TabLocation> caselist = new LinkedList<TabLocation>();
		caselist.add(new TabLocation(okolica.x-1,okolica.y));
		caselist.add(new TabLocation(okolica.x+1,okolica.y));
		caselist.add(new TabLocation(okolica.x,okolica.y-1));
		caselist.add(new TabLocation(okolica.x,okolica.y+1));
		int numofcases = 4;
		TabLocation choosen;
		do{
			int mozliwosc = generator.nextInt( numofcases );
			choosen = caselist.get( mozliwosc );
			x1 = choosen.x;
			y1 = choosen.y;
			numofcases--;
			caselist.remove( mozliwosc );												
		}while( x1 >= sizeBoard || x1 < 0 || y1 >= sizeBoard || y1 < 0 || opponentShootTable[x1][y1] == true );
	
		return choosen;
	}
	
	
	private TabLocation shootinline(TabLocation p1, TabLocation p2,boolean orientacja){
		Random generator = new Random();
		
		if(orientacja){
			int shotcase = generator.nextInt(2);
			
			int maxy = Math.max(p1.y, p2.y);			
			int miny = Math.min(p1.y, p2.y);
						
			if( shotcase == 0 && maxy+1 < sizeBoard && opponentShootTable[p2.x][maxy+1] == false ){
				return new TabLocation( p2.x, maxy+1 );
			}
			else{
				if( miny-1 >=0 && opponentShootTable[p2.x][miny-1] == false ) return new TabLocation( p2.x, miny-1 );
				else return new TabLocation( p2.x, maxy+1 );
			}
			
		}
		else{
			int shotcase = generator.nextInt(2);
		
			int maxx = Math.max( p1.x, p2.x );
			
			int minx = Math.min( p1.x, p2.x );
			
			
			if( shotcase == 0 && maxx+1 < sizeBoard && opponentShootTable[maxx+1][p2.y] == false ){
				return new TabLocation( maxx+1, p2.y );
			}
			else{ 
				if(minx-1 >= 0 && opponentShootTable[minx-1][p2.y] == false)return new TabLocation( minx-1, p2.y );
				else return new TabLocation( maxx+1, p2.y );
			}
		}
	}
	
	//przeladowanie nazw funkcji,  mamy taka sama funkcje jak poprzednia dla 3 w linii
	private TabLocation shootinline( TabLocation p1, TabLocation p2, TabLocation p3, boolean orientacja ){
		Random generator = new Random();
		if( orientacja ){
			int shotcase = generator.nextInt(2);
			
			int maxy = Math.max(p1.y,p2.y);
			maxy = Math.max(maxy, p3.y);
			int miny = Math.min(p1.y,p2.y);
			miny = Math.min(miny, p3.y);
			
			if( shotcase == 0 && maxy+1 < sizeBoard && opponentShootTable[p3.x][maxy+1] == false ) return new TabLocation( p3.x, maxy+1 );
			
			else{
				if( miny-1 >= 0 && opponentShootTable[p3.x][miny-1] == false )return new TabLocation( p3.x, miny-1 );
				else return new TabLocation( p3.x, maxy+1 );
			}
			
		}
		else{
			int shotcase = generator.nextInt(2);
		
			int maxx = Math.max( p1.x, p2.x );
			maxx = Math.max( maxx, p3.x );
			int minx = Math.min( p1.x, p2.x);
			minx = Math.min( minx, p3.x );
			
			if( shotcase == 0 && maxx+1 <sizeBoard && opponentShootTable[maxx+1][p3.y] == false ){
				return new TabLocation( maxx+1, p3.y );
			}
			else{
				if( minx-1 >= 0 && opponentShootTable[minx-1][p3.y] == false ) return new TabLocation( minx-1, p3.y );
				else return new TabLocation( maxx+1, p3.y );
			}
		}
		
	}
	
	public TabLocation opponentMove(){
	
	TabLocation ruch = randowShoot();
	if( located ){
		
		do{
			if( hitLocation2 != null ){
					if(hitLocation1 != null){
						if( hitLocation1.x == hitLocation2.x && hitLocation2.x == hitLocation3.x ){
							ruch = shootinline( hitLocation1, hitLocation2, hitLocation3, true );
						}
						else if( hitLocation1.y == hitLocation2.y && hitLocation2.y == hitLocation3.y ){
							ruch = shootinline( hitLocation1, hitLocation2, hitLocation3, false );
						}
					}
				else if( hitLocation2.x == hitLocation3.x ){						
					ruch = shootinline(hitLocation2,hitLocation3,true);
				}
				else if( hitLocation2.y == hitLocation3.y ){
					ruch = shootinline(hitLocation2, hitLocation3, false );
				}
				
			}
			else if( lastMove && !secondlastMove ){
				ruch = shootinarea( hitLocation3 );
			}
			else if( !lastMove ){
				ruch = shootinarea( hitLocation3 );
			}
						
		}while( opponentShootTable[ruch.x][ruch.y] == true);		
	}
	
	opponentShootTable[ruch.x][ruch.y] = true;
	possibleshoots.remove( ruch );
	return ruch;
	}
	
	//przeciwnik ustawia swoje statki na planszy
	private void setShips(){
		
		int x,y;
		boolean orientacja;
		Random generator = new Random();
		
		int ilosc = 1;
		int j = 0;
		for(int i = 4; i > 0 ; i--){
			while( j < ilosc ){
				x = generator.nextInt( 13 );		
				y = generator.nextInt( 13 );
				orientacja = generator.nextBoolean();
				if( isPossibleAddShip(x, y, i, orientacja) ) j++;	
			}
		ilosc++;
		j=0;
		}
		
	}		
	
	// gdy statki sie nie stykaja, to wokol zatopionego statku nie strzelamy juz dalej - oznaczamy, zeby tam nie strzelac 
	public void markEmptyFields(int shipSize){
		int x = hitLocation3.x;
		int y = hitLocation3.y;
		
		if( hitLocation2==null && hitLocation3 !=null ){
			if(x-1 >= 0 && y - 1 >= 0){
				opponentShootTable[x-1][y-1] = true;
				possibleshoots.remove( new TabLocation(x-1, y-1) );	//po skosie lewy gorny				
			}
			if(x-1 >= 0){
				opponentShootTable[x-1][y] = true;
				possibleshoots.remove( new TabLocation(x-1, y) );		//gorny
			}
			if(y + 1 <sizeBoard){
				opponentShootTable[x][y+1] = true;
				possibleshoots.remove( new TabLocation(x, y+1) );		//prawy	
			}
			if(x+1 < sizeBoard && y +1 < sizeBoard){
				opponentShootTable[x+1][y+1] = true;				//po skosie dol prawy
				possibleshoots.remove( new TabLocation(x+1, y+1) );					
			}
			if(x-1 >= 0 && y + 1 < sizeBoard){
				opponentShootTable[x-1][y+1] = true;				//po skosie prawy gora
				possibleshoots.remove( new TabLocation(x-1, y+1) );					
			}
			if( x +1  < sizeBoard){
				opponentShootTable[x+1][y] = true;				// dolny
				possibleshoots.remove(new TabLocation(x+1,y));					
			}
			if(x+1 <sizeBoard && y - 1 >= 0){
				opponentShootTable[x+1][y-1] = true;			//po skosie dol lewy
				possibleshoots.remove( new TabLocation(x+1, y-1) );					
			}
			if(y - 1 >= 0){
				opponentShootTable[x][y-1] = true;			//lewy
				possibleshoots.remove( new TabLocation(x, y-1) );					
			}
			
		}
		else{
			
			boolean orientacja = false;
			if( hitLocation3.x == hitLocation2.x ) orientacja = true;
			int tempshipSize = shipSize;
			if( orientacja ){
				
				if( hitLocation2.y < hitLocation3.y ){
					tempshipSize = - shipSize;
				
				//prawy skrajny	
					if(x-1 >= 0 && y + tempshipSize >= 0){
						opponentShootTable[x-1][y+tempshipSize] = true;
						possibleshoots.remove( new TabLocation( x-1, y+tempshipSize ) );		//lewy gorny skos			
					}
					if(y+tempshipSize >= 0){
						opponentShootTable[x][y+tempshipSize] = true;
						possibleshoots.remove( new TabLocation( x, y+tempshipSize ) );	//lewy 
					}
					if(x+1 <sizeBoard && y+tempshipSize>=0){
						opponentShootTable[x+1][y+tempshipSize] = true;
						possibleshoots.remove( new TabLocation(x+1, y+tempshipSize) );		// lewy dolny skos
					}
					
					if(y+1<sizeBoard ){
						opponentShootTable[x][y+1] = true;
						possibleshoots.remove( new TabLocation(x, y+1) );	
					}
					if(y+1<sizeBoard && x-1 >= 0){
						opponentShootTable[x-1][y+1] = true;
						possibleshoots.remove( new TabLocation(x-1, y+1) );
					}
					if(y+1<sizeBoard && x+1 < sizeBoard){
						opponentShootTable[x+1][y+1] = true;
						possibleshoots.remove( new TabLocation(x+1, y+1) );
					}
				}
				else{
					if(x-1 >= 0 && y + tempshipSize < sizeBoard){
						opponentShootTable[x-1][y+tempshipSize] = true;
						possibleshoots.remove( new TabLocation(x-1, y+tempshipSize) );		//lewy gorny skos			
					}
					if(y+tempshipSize < sizeBoard){
						opponentShootTable[x][y+tempshipSize] = true;
						possibleshoots.remove( new TabLocation(x, y+tempshipSize) );	//lewy 
					}
					if(x+1 < sizeBoard && y+tempshipSize < sizeBoard){
						opponentShootTable[x+1][y+tempshipSize] = true;
						possibleshoots.remove( new TabLocation(x+1, y+tempshipSize) );		// lewy dolny skos
					}
					
					if(y-1 >= 0 ){
						opponentShootTable[x][y-1] = true;
						possibleshoots.remove( new TabLocation(x, y-1) );	
					}
					if(y-1 >= 0 && x-1 >= 0){
						opponentShootTable[x-1][y-1] = true;
						possibleshoots.remove( new TabLocation(x-1, y-1) );
					}
					if(y-1 >= 0 && x+1 < sizeBoard){
						opponentShootTable[x+1][y-1] = true;
						possibleshoots.remove( new TabLocation(x+1, y-1) );
					}
				}
				
				
				if( x-1 >= 0 ){
					if( hitLocation2.y < hitLocation3.y ){
						for(int i=0; i<shipSize;i++){
							opponentShootTable[x-1][y-i] = true;		
							possibleshoots.remove( new TabLocation(x-1, y-i) );
						}
					}
					else{
						for(int i=0; i<shipSize;i++){
							opponentShootTable[x-1][y+i] = true;		
							possibleshoots.remove( new TabLocation(x-1, y+i) );
						}
					}
				}
				if(x + 1 < sizeBoard){
					if(hitLocation2.y < hitLocation3.y){
						for(int i=0; i<shipSize; i++){
							opponentShootTable[x+1][y-i] = true;
							possibleshoots.remove( new TabLocation(x+1, y-i) );
						}
					}
					else{
						for(int i=0; i<shipSize;i++){
							opponentShootTable[x+1][y+i] = true;
							possibleshoots.remove( new TabLocation(x+1, y+i) );
						}
					}
				}
				
			}
			else{										
				if(hitLocation2.x < hitLocation3.x){
					tempshipSize = - shipSize;
				
				//dolny skrajny	
					if(y-1 >= 0 && x + tempshipSize >= 0){
						opponentShootTable[x+ tempshipSize][y-1] = true;
						possibleshoots.remove( new TabLocation(x+ tempshipSize, y-1) );		//lewy gorny skos			
					}
					if(x + tempshipSize >= 0){
						opponentShootTable[x+ tempshipSize][y] = true;
						possibleshoots.remove( new TabLocation(x+ tempshipSize, y) );	//lewy 
					}
					if(y+1 < sizeBoard && x+tempshipSize >= 0){
						opponentShootTable[x+ tempshipSize][y+1] = true;
						possibleshoots.remove( new TabLocation(x+ tempshipSize, y+1) );		// lewy dolny skos
					}
					
					if(x+1 < sizeBoard ){
						opponentShootTable[x+1][y] = true;
						possibleshoots.remove( new TabLocation(x+1, y) );	
					}
					if(x+1 < sizeBoard && y-1 >= 0){
						opponentShootTable[x+1][y-1] = true;
						possibleshoots.remove( new TabLocation(x+1, y-1) );
					}
					if(x+1 < sizeBoard && y+1 < sizeBoard){
						opponentShootTable[x+1][y+1] = true;
						possibleshoots.remove( new TabLocation(x+1, y+1) );
					}
				}
				else{
					if(y-1 >= 0 && x + tempshipSize < sizeBoard){
						opponentShootTable[x+ tempshipSize][y-1] = true;
						possibleshoots.remove( new TabLocation(x+ tempshipSize, y-1) );		//lewy gorny skos			
					}
					if(x+tempshipSize < sizeBoard){
						opponentShootTable[x+ tempshipSize][y] = true;
						possibleshoots.remove( new TabLocation(x+ tempshipSize, y) );	//lewy 
					}
					if(y+1 < sizeBoard && x+tempshipSize < sizeBoard){
						opponentShootTable[x+ tempshipSize][y+1] = true;
						possibleshoots.remove( new TabLocation(x+ tempshipSize, y+1) );		// lewy dolny skos
					}
					
					if(x-1 >= 0 ){
						opponentShootTable[x-1][y] = true;
						possibleshoots.remove( new TabLocation(x-1, y) );	
					}
					if(x-1 >= 0 && y-1 >= 0){
						opponentShootTable[x-1][y-1] = true;
						possibleshoots.remove( new TabLocation(x-1, y-1) );
					}
					if(x-1 >= 0 && y+1 < sizeBoard){
						opponentShootTable[x-1][y+1] = true;
						possibleshoots.remove( new TabLocation(x-1, y+1) );
					}
				}
				
				
				if(y-1 >= 0){
					if(hitLocation2.x < hitLocation3.x){
						for(int i=0; i < shipSize; i++){
							opponentShootTable[x-i][y-1] = true;		
							possibleshoots.remove( new TabLocation(x-i, y-1) );
						}
					}
					else{
						for(int i=0; i < shipSize; i++){
							opponentShootTable[x+i][y-1] = true;		
							possibleshoots.remove( new TabLocation(x+i, y-1) );
						}
					}
				}
				if(y+1 < sizeBoard){
					if(hitLocation2.x < hitLocation3.x){
						for(int i=0; i<shipSize;i++){
							opponentShootTable[x-i][y+1] = true;
							possibleshoots.remove( new TabLocation(x-i, y+1) );
						}
					}
					else{
						for(int i=0; i < shipSize; i++){
							opponentShootTable[x+i][y+1] = true;
							possibleshoots.remove( new TabLocation(x+i, y+1) );
						}
					}
				}
			}
		}
	}
	
	//sprawdza czy statek styka sie z innymi
	private boolean checkContactingships(int x,int y,int shipSize,boolean orientacja){
		if(orientacja){
			if(x-1 >= 0 && y - 1 >= 0){
				if(opponentShips[x-1][y-1] != null) return false;
			}
			if(y-1 >= 0){
				if(opponentShips[x][y-1] != null) return false;
			}
			if(x+1 < sizeBoard && y-1 >= 0){
				if(opponentShips[x+1][y-1] != null) return false;
			}
			if(y+shipSize < sizeBoard){
				if(opponentShips[x][y+shipSize] != null) return false;
			}
			if(x-1 >= 0 && shipSize+y < sizeBoard){
				if(opponentShips[x-1][y+shipSize] != null) return false;
			}
			if(x+1 < sizeBoard && shipSize+y < sizeBoard){
				if(opponentShips[x+1][y+shipSize] != null) return false;
			}
			if(x-1 >= 0){
				for(int i=0; i < shipSize; i++){
					if(opponentShips[x-1][y+i] != null)return false;
				}
			}
			if(x+1 < sizeBoard){
				for(int i=0; i < shipSize; i++){
					if(opponentShips[x+1][y+i] != null)return false;
				}
			}
		}
		else{
			if(x-1 >= 0 && y - 1 >= 0){
				if(opponentShips[x-1][y-1] != null) return false;
			}
			if(x-1 >= 0){
				if(opponentShips[x-1][y] != null) return false;
			}
			if(y+1 < sizeBoard && x-1 >= 0){
				if(opponentShips[x-1][y+1] != null) return false;
			}
			if(x+shipSize < sizeBoard){
				if(opponentShips[x+shipSize][y] != null) return false;
			}
			if(y-1 >= 0 && shipSize+x < sizeBoard){
				if(opponentShips[shipSize+x][y-1] != null) return false;
			}
			if(y+1 < sizeBoard && shipSize+x < sizeBoard){
				if(opponentShips[shipSize+x][y+1] != null) return false;
			}
			if(y-1 >= 0){
				for(int i=0; i < shipSize; i++){
					if(opponentShips[x+i][y-1] != null) return false;
				}
			}
			if(y+1 < sizeBoard){
				for(int i=0; i < shipSize; i++){
					if(opponentShips[x+i][y+1] != null) return false;
				}
			}
		}
		return true;
	}
	
	
	private boolean isPossibleAddShip(int x, int y, int shipSize, boolean orientacja){
		if(orientacja){
			if( y + shipSize > sizeBoard ) return false;
			for(int i = 0 ; i < shipSize ; i++){
				if(opponentShips[x][y+i] != null) return false;
			}
			if(!checkContactingships(x, y, shipSize, true) ) return false;			//
			Ship addthisship = new Ship( shipSize );
			for(int tj = 0; tj< shipSize ; tj++){
				
				opponentShips[x][y+tj] = addthisship;
				
			}
			
		}
		else{
			if( x + shipSize > sizeBoard ) return false;
			for(int i = 0 ; i < shipSize ; i++){
				if(opponentShips[x+i][y] != null) return false;
			}
			if(!checkContactingships(x, y, shipSize, false) ) return false;			//
			Ship addthisship = new Ship( shipSize );
			for(int ti = 0; ti< shipSize ; ti++){
				
				opponentShips[x+ti][y] = addthisship;
				
			}
		}
		return true;
	}
	
	public Ship getfromshiploc(int x, int y){
		return opponentShips[x][y];
	}
	
	public void setshiploc(int x, int y, Ship val){
		opponentShips[x][y] = val;
	}
	
	//wprowadzam rozmiar statku   1-4
	public int getDecshipCount(int shipSize){
		opponentShipCounter[shipSize-1]--;
		return opponentShipCounter[shipSize-1];
	}
	
	public void locatedshooted(TabLocation lasthit){
		
		located = true;
		hitLocation1 = hitLocation2;
		hitLocation2 = hitLocation3;
		hitLocation3 = lasthit;
		
		secondlastMove = lastMove;
		lastMove = true;
	}
	
	public void shipdestroyed(){
		located = false;
		hitLocation1 = null;
		hitLocation2 = null;
		hitLocation3 = null;
		secondlastMove = false;
		lastMove = false;
	}
	
	public void nothingnextmove(){
		secondlastMove = lastMove;
		lastMove = false;
	}
	
}

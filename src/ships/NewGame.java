package ships;

import java.util.concurrent.CountDownLatch;

public class NewGame {

	private static CountDownLatch latch = new CountDownLatch(1);	
	
	public static void gra(GamePanel newgame){
		
		int npcSpeed = Menu.getNPCspeed();	
		
		
		Player gracz = newgame.getPlayerShipBoard().getPlayer();
		Opponent npc = newgame.getShootBoard().getNpc();
		PlayerShipBoard shipMapPlace1 = newgame.getPlayerShipBoard();
		ShootBoard planszaG = newgame.getShootBoard();
		
		
		
		String [] alfabet = {"A","B","C","D","E","F","G","H","I","J","K","L","£"};
		
		
		Menu.setInfo("Ustaw swoje statki na planszy.");
		playerPlaceShips(shipMapPlace1);
				
		TabLocation tlocCheck;
		
		int i = 0;
		int j = 0;
		boolean end = false;
				
		while( !end ){ 
			
			do{
				planszaG.setRound(true);
				
				
				latch = new CountDownLatch(1);		//tworze nowy bo nie mozna inkrementowac
				
				try {					
					latch.await();					
				} 
				catch (InterruptedException e) {					
						e.printStackTrace();
				}
				
				gracz.addshot();
				if( npc.getLRdmg() > -1 ){
					i++;
					if( i == 20 ){					
						end = true;						
					}
					if( npc.getLRdmg() > 0 ){				//  getLRdmg  -  get last round dmg
						newgame.decShipCount(npc.getLRdmg());						
						Menu.setInfo( "Zatopiles statek przeciwnika !" );
					}
				}
			}while( npc.getLRdmg() > -1 && !end );
			
			
			planszaG.setRound( false );
			int check;
			
			
			if( !end ){
				
				do{
					
					
					try {
						Thread.sleep(npcSpeed);				//opoznienie reakcji przeciwnika, opoznienie jego ruchu
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					
					tlocCheck = npc.opponentMove();
					
					Menu.setInfo( "Przeciwnik strzela w:  " + alfabet[tlocCheck.x] + " " + (tlocCheck.y+1) );
					check = gracz.opponentShootCheck( tlocCheck.x, tlocCheck.y );
					
					if( check == -1 || check >0 ){
						npc.locatedshooted(tlocCheck);
					}
					
					else npc.nothingnextmove();
																				
					if( check > 0 ){
						npc.markEmptyFields( check );
						Menu.setInfo( "Przeciwnik zatopi³:  " + check + "masztowiec " + alfabet[tlocCheck.x] + " " + (tlocCheck.y+1) );						
						npc.shipdestroyed();
					}
					
															
					if(check == -1 || check > 0){	
						j++;
						if( j == 20 ) end = true;
						
						shipMapPlace1.shootedPlayer( tlocCheck.x , tlocCheck.y,true );											
					}
					else shipMapPlace1.shootedPlayer( tlocCheck.x , tlocCheck.y,false );
				
				} while( check != 0 && !end );
			}
		}
				
		if( i == 20 ){			
			EndGameInfo info = new EndGameInfo(true);
			isAHighscore( gracz );
		}
		else{
            planszaG.endGameShowShips();            		
			EndGameInfo info = new EndGameInfo(false);
			
		}
			
	}
	
	
	//sprawdzamy czy wynik zalapuje sie na liste najlepszych, jesli tak dodajemy
	private static void isAHighscore( Player x ){
		HighscoreGame addgame = new HighscoreGame( "x" , x.getNumberofshoots() );
		if( addgame.gameAdd() ){
			System.out.println( "dodalem" );
		}
		
	}
	
	//gracz ustawia swoje statki
	private static void playerPlaceShips(PlayerShipBoard placehere){
		
		
		int ilosc = 1;
		int j = 0;
		for(int i = 4 ; i > 0 ; i--){
			
			
			while( j < ilosc ){					
								
				latch = new CountDownLatch(1);
								
				try {					
					latch.await();					
				} 
				catch (InterruptedException e) {					
						e.printStackTrace();
				}
				placehere.setInserted( false );
				j++;
				
			}
							
		placehere.decshipSize();	
		ilosc++;
		j = 0;
		
		}		
	}
	
	public static void moveDecLatch(){
		latch.countDown();
	}
				
}

package ships;


public class Main {
	
	public static void main( String[] args ){
											
		GamePanel gamescreen = new GamePanel();		
		
		Menu showgui = new Menu( gamescreen );
		showgui.displayGUI();				
	}
}

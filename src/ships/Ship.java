package ships;

public class Ship {
	
	private int shipSize;
	private boolean destroyed;
	private int damage;
	
	public Ship(int size){
		this.shipSize = size;
		this.destroyed = false;
		this.damage = 0;
	}
	
	public int getshipSize(){
		return shipSize;
	}
	
	public int howmuchdamage(){
		return damage;
	}
	
	//zwraca true jak statek zniszczony,   false gdy niezniszczony
	public boolean isDestroyed(){
		damage++;
		if(damage == shipSize){
			destroyed = true;
			return true;
		}
		return false;
	}

}

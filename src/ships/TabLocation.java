package ships;

public class TabLocation{
	int x,y;
	public TabLocation( int x, int y){
		this.x=x;
		this.y=y;			
	}
	
	public boolean equals( Object obj ){
		
		if( this == obj ) return true;
		if( !(obj instanceof TabLocation) ) return false;

		TabLocation other = (TabLocation) obj;
		return (this.x == other.x && this.y == other.y);		
	}
	
}
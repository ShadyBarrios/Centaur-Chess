package application;

/**
 * Represents a coordinate based on the Cartesian Coordinate System.
 * @author Shady
 *
 */
public class Coordinate{
	private int x,y;
	
	/**
	 * Initializes the coordinate with (0,0)
	 */
	public Coordinate() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Initializes the coordinate with (x,y)
	 * @param x - Represents x component
	 * @param y - Represents y component
	 */
	public Coordinate(int x, int y) {this.x = x; this.y = y;}
	
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	/**
	 * Changes both x and y of a coordinate
	 * @param x - x component
	 * @param y - y component
	 */
	public void setNew(int x, int y) {
		this.x = x; 
		this.y = y;
	}
	
	/**
	 * @return The x in (x,y)
	 */
	public int getX() {return this.x;}
	/**
	 * @return The y in (x,y)
	 */
	public int getY() {return this.y;}
	
	/**
	 * @param coor - Coordinate being compared to
	 * @return The absolute value difference between the x components of the coordinates.
	 */
	public int xDifference(Coordinate coor) {
		return Math.abs(this.x - coor.x);
	}
	
	/**
	 * 
	 * @param coor - Coordinate being compared to
	 * @return The absolute value difference between the y components of the coordinates.
	 */
	public int yDifference(Coordinate coor) {
		return Math.abs(this.y - coor.y);
	}
	
	/**
	 * @param coor - Coordinate being compared to
	 * @return Determines whether or not the two coordinates have the same x components.
	 */
	public boolean matchesX(Coordinate coor) {
		return this.x == coor.x;
	}
	
	/**
	 * @param coor - Coordinate being compared to
	 *  @return Determines whether or not the two coordinates have the same y components.
	 */
	public boolean matchesY(Coordinate coor) {
		return this.y == coor.y;
	}
	
	/**
	 * @param coor - Coordinate being compared to
	 * @return Determines whether or not the two coordinates have the same x and y components.
	 */
	public boolean matches(Coordinate coor) {
		return (this.x == coor.x && this.y == coor.y);
	}

	/**
	 * Note: The JavaFX grid doesn't use a normal coordinate system.
	 * @return Converts the coordinate into an index that matches the grid panel on the JavaFX gridview.
	 */
	public int toPaneCoordinate() {
		// fxml goes from top to bottom y axis
		return ( ((this.y - 8) * (-8)) + (this.x - 1) );
		
	}
	
	/**
	 * @return The coordinate as string in "( x , y )" format.
	 */
	public String toString() {return "( " + x + " , " + y + " )";}
}
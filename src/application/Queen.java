package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Class representing a Queen chess piece
 * @author Shady
 *
 */
public class Queen extends Piece{
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteQueen.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackQueen.png";
	// value == 9
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.QUEEN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.SKINNY;
		this.value = 9;
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public Queen(Players color, int x, int y) {
		super(color, x, y);
		ConstructRest();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param coor - The coordinate the piece starts on
	 */
	public Queen(Players color, Coordinate coor) {
		super(color, coor);
		ConstructRest();
	}
	
	/**
	 * Constructs a bare bones version of the piece. Mainly used to get images for the GUI Body Counter.
	 * @param color - The color of player the piece belongs to. 
	 */
	public Queen(Players color) {
		image = (color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	@Override
	public String getInitial() {return "Q";}
	
	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(verticalCoordinates());	
		coordinates.addAll(horizontalCoordinates());
		coordinates.addAll(negativeDiagonalCoordinates());
		coordinates.addAll(positiveDiagonalCoordinates());
		
		return coordinates;
	}
}
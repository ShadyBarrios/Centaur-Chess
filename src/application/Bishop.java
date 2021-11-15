package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Class representing the Bishop chess piece.
 * @author Shady 
 * 
 */
public class Bishop extends Piece{
	
	private String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteBishop.png";
	private String PathToBlack = "file:///C:/Users/scott/Downloads/BlackBishop.png";
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.BISHOP;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.SKINNY;
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public Bishop(Players color, int x, int y) {
		super(color, x, y);
		ConstructRest();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param coor - The coordinate the piece starts on.
	 */
	public Bishop(Players color, Coordinate coor) {
		super(color, coor);
		ConstructRest();
	}
	
	/**
	 * Constructs a bare bones version of the piece. Mainly used to get images for the GUI Body Counter.
	 * @param color - The color of player the piece belongs to. 
	 */
	public Bishop(Players color) {
		image = (color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}

	@Override
	public String getInitial() {return "B";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(negativeDiagonalCoordinates());
		coordinates.addAll(positiveDiagonalCoordinates());
		
		return coordinates;
	}
}
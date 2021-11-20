package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Class representing a Rook chess piece
 * @author Shady 
 * 
 */

public class Rook extends Piece{
	String PathToWhite = "file:///../ChessPieces/WhiteRook.png";
	String PathToBlack = "file:///../ChessPieces/BlackRook.png";
	// value == 5
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.ROOK;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.THICK;
		this.value = 5;
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public Rook(Players color, int x, int y) {
		super(color, x, y);
		ConstructRest();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param coor - The coordinate the piece starts on.
	 */
	public Rook(Players color, Coordinate coor) {
		super(color, coor);
		ConstructRest();
	}
	
	/**
	 * Constructs a bare bones version of the piece. Mainly used to get images for the GUI Body Counter.
	 * @param color - The color of player the piece belongs to. 
	 */
	public Rook(Players color) {
		image = (color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}

	@Override
	public String getInitial() {return "R";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(verticalCoordinates());
		coordinates.addAll(horizontalCoordinates());
		
		return coordinates;
	}
}
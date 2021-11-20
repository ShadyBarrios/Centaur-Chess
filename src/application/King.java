package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Class representing a King chess piece
 * @author Shady
 *
 */
public class King extends Piece{
	
	String PathToWhite = "file:///../ChessPieces/WhiteKing.png";
	String PathToBlack = "file:///../ChessPieces/BlackKing.png";
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.KING;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.SKINNY;
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public King(Players color, int x, int y) {
		super(color, x, y);
		ConstructRest();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param coor - The coordinate the piece starts on.
	 */
	public King(Players color, Coordinate coor) {
		super(color, coor);
		ConstructRest();
	}
	
	@Override
	public String getInitial() {return "K";}
	
	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(topCoordinates());	
		coordinates.addAll(middleCoordinates());
		coordinates.addAll(bottomCoordinates());
		coordinates.addAll(castleCoordinates());
		
		return coordinates;
	}
	
	/**
	 * Since the coordinates are on top of the piece, yManip of {@link #surroundingCoordinates(int yManip)} is 1.
	 * @return The available coordinates above the piece that are in direct contact with the piece's tile.
	 */
	private List<Coordinate> topCoordinates(){
		return (this.currentPosition.getY() == 8) ? new ArrayList<Coordinate>() : surroundingCoordinates(1);
	}
	
	/**
	 * Since the coordinates are on the same row as the piece, yManip of {@link #surroundingCoordinates(int yManip)} is 0.
	 * @return The available coordinates on the same row as the piece that are in direct contact with the piece's tile.
	 */
	private List<Coordinate> middleCoordinates(){
		return surroundingCoordinates(0);
	}
	
	/**
	 * Since the coordinates are below the piece, yManip of {@link #surroundingCoordinates(int yManip)} is -1.
	 * @return The available coordinates below the piece that are in direct contact with the piece's tile.
	 */
	private List<Coordinate> bottomCoordinates(){
		return (this.currentPosition.getY() == 1) ? new ArrayList<Coordinate>() : surroundingCoordinates(-1);
	}
	
	/**
	 * @return A list containing coordinates the king can move to as a castle move.
	 */
	private List<Coordinate> castleCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		if(this.hasMoved)
			return coordinates;
		
		int currentY = this.currentPosition.getY();
		
		if(this.CanCastleRight())
			coordinates.add(new Coordinate(7, currentY));
		if(this.CanCastleLeft())
			coordinates.add(new Coordinate(3, currentY));
		
		return coordinates;
	}
}
package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

/**
 * Class representing a Knight chess piece
 * @author Shady
 */
public class Knight extends Piece{
	String PathToWhite = "file:///../ChessPieces/WhiteKnight.png";
	String PathToBlack = "file:///../ChessPieces/BlackKnight.png";
	// value == 3
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.KNIGHT;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.THICK;
		this.value = 3;
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public Knight(Players color, int x, int y) {
		super(color, x, y);
		ConstructRest();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param coor - The coordinate the piece starts on.
	 */
	public Knight(Players color, Coordinate coor) {
		super(color, coor);
		ConstructRest();
	}
	
	/**
	 * Constructs a bare bones version of the piece. Mainly used to get images for the GUI Body Counter.
	 * @param color - The color of player the piece belongs to. 
	 */
	public Knight(Players color) {
		image = (color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}

	@Override
	public String getInitial() {return "N";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(Q1Coordinates());
		coordinates.addAll(Q2Coordinates());
		coordinates.addAll(Q3Coordinates());
		coordinates.addAll(Q4Coordinates());
		
		return coordinates;
	}
	
	/**
	 * Qn represents the nth quadrant on a graph with the piece being the origin.
	 * @return A list of the available moves in the 1st quadrant.
	 */
	public List<Coordinate> Q1Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX + 1 <= UpperLimit && currentY + 2 <= UpperLimit) {
			if(Board.slot(new Coordinate(currentX + 1, currentY + 2)) == null) 
				coordinates.add(new Coordinate(currentX + 1, currentY + 2));
			else if(Board.slot(new Coordinate(currentX + 1, currentY + 2)).color != this.color)
				coordinates.add(new Coordinate(currentX + 1, currentY + 2));	
		}
			
		// for short one
		if(currentX + 2 <= UpperLimit && currentY + 1 <= UpperLimit){
			if(Board.slot(new Coordinate(currentX + 2, currentY + 1)) == null) 
				coordinates.add(new Coordinate(currentX + 2, currentY + 1));
			else if(Board.slot(new Coordinate(currentX + 2, currentY + 1)).color != this.color)
				coordinates.add(new Coordinate(currentX + 2, currentY + 1));	
		}
		return coordinates;
	}
	
	/**
	 * Qn represents the nth quadrant on a graph with the piece being the origin.
	 * @return A list of the available moves in the 2nd quadrant.
	 */
	public List<Coordinate> Q2Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX - 1 >= LowerLimit && currentY + 2 <= UpperLimit){
			if(Board.slot(new Coordinate(currentX - 1, currentY + 2)) == null) 
				coordinates.add(new Coordinate(currentX - 1, currentY + 2));
			else if(Board.slot(new Coordinate(currentX - 1, currentY + 2)).color != this.color)
				coordinates.add(new Coordinate(currentX - 1, currentY + 2));	
		}
		// for short one
		if(currentX - 2 >= LowerLimit && currentY + 1 <= UpperLimit){
			if(Board.slot(new Coordinate(currentX - 2, currentY + 1)) == null) 
				coordinates.add(new Coordinate(currentX - 2, currentY + 1));
			else if(Board.slot(new Coordinate(currentX - 2, currentY + 1)).color != this.color)
				coordinates.add(new Coordinate(currentX +- 2, currentY + 1));	
		}
		
		return coordinates;
	}
	
	/**
	 * Qn represents the nth quadrant on a graph with the piece being the origin.
	 * @return A list of the available moves in the 3rd quadrant.
	 */
	public List<Coordinate> Q3Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX - 1 >= LowerLimit && currentY - 2 >= LowerLimit){
			if(Board.slot(new Coordinate(currentX - 1, currentY - 2)) == null) 
				coordinates.add(new Coordinate(currentX - 1, currentY - 2));
			else if(Board.slot(new Coordinate(currentX - 1, currentY - 2)).color != this.color)
				coordinates.add(new Coordinate(currentX - 1, currentY - 2));	
		}
		// for short one
		if(currentX - 2 >= LowerLimit && currentY - 1 >= LowerLimit){
			if(Board.slot(new Coordinate(currentX - 2, currentY - 1)) == null) 
				coordinates.add(new Coordinate(currentX - 2, currentY - 1));
			else if(Board.slot(new Coordinate(currentX - 2, currentY - 1)).color != this.color)
				coordinates.add(new Coordinate(currentX - 2, currentY - 1));	
		}
		
		return coordinates;
	}
	
	/**
	 * Qn represents the nth quadrant on a graph with the piece being the origin.
	 * @return A list of the available moves in the 4th quadrant.
	 */
	public List<Coordinate> Q4Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX + 1 <= UpperLimit && currentY - 2 >= LowerLimit){
			if(Board.slot(new Coordinate(currentX + 1, currentY - 2)) == null) 
				coordinates.add(new Coordinate(currentX + 1, currentY - 2));
			else if(Board.slot(new Coordinate(currentX + 1, currentY - 2)).color != this.color)
				coordinates.add(new Coordinate(currentX + 1, currentY - 2));	
		}
		
		// for short one
		if(currentX + 2 <= UpperLimit && currentY - 1 >= LowerLimit){
			if(Board.slot(new Coordinate(currentX + 2, currentY - 1)) == null) 
				coordinates.add(new Coordinate(currentX + 2, currentY - 1));
			else if(Board.slot(new Coordinate(currentX + 2, currentY - 1)).color != this.color)
				coordinates.add(new Coordinate(currentX + 2, currentY - 1));	
		}
		
		return coordinates;
	}
	
}
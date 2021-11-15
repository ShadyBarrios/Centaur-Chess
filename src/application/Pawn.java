package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Class representing a Pawn chess piece
 * @author Shady
 *
 */
public class Pawn extends Piece{
	
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhitePawn.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackPawn.png";
	
	List<Integer> bannedPawns;
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.PAWN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.THICK;
		this.numberOfMoves = 0;
		bannedPawns = new ArrayList<Integer>();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public Pawn(Players color, int x, int y) {
		super(color, x, y);
		ConstructRest();
	}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param coor - The coordinate the piece starts on.
	 */
	public Pawn(Players color, Coordinate coor) {
		super(color, coor); 
		ConstructRest();
	}
	
	/**
	 * Constructs a bare bones version of the piece. Mainly used to get images for the GUI Body Counter.
	 * @param color - The color of player the piece belongs to. 
	 */
	public Pawn(Players color) {
		this.color = color;
		ConstructRest();
	}

	@Override
	public String getInitial() {return "P";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(getVertical());
		coordinates.addAll(eliminationCoordinates());
		
		return coordinates;
	}
	
	/**
	 * Direction of coordinates depends on pawn's color.
	 * @return A list of the coordinates the pawn can move to (in same column).
	 */
	private List<Coordinate> getVertical(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentX = currentPosition.getX();
		int currentY = currentPosition.getY();
		
		switch(this.color){
		case WHITE:
			if(currentY < UpperLimit && Board.slot(new Coordinate(currentX, currentY + 1)) == null) {
				coordinates.add(new Coordinate(currentX, currentY + 1));
				if(!hasMoved && Board.slot(new Coordinate(currentX, currentY + 2)) == null) 
					coordinates.add(new Coordinate(currentX, currentY + 2));
			}
			break;
		case BLACK:
			if(currentY > LowerLimit && Board.slot(new Coordinate(currentX, currentY - 1)) == null) {
				coordinates.add(new Coordinate(currentX, currentY - 1));
				if(!hasMoved && Board.slot(new Coordinate(currentX, currentY - 2)) == null )
					coordinates.add(new Coordinate(currentX, currentY - 2));
			}
			break;
		}
		
		return coordinates;
	}
	
	/**
	 * Valid coordinates for the pawn to eliminate pieces
	 * @return A list of coordinates for when the pawn can eliminate a piece (including En Passant).
	 */
	private List<Coordinate> eliminationCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentX = currentPosition.getX();
		int currentY = currentPosition.getY();
		Piece piece;
		
		// NOTE : DISCONNECT UPPERBOUND EXCEPTION FROM LOWERBOUND EXCEPTION
		switch(this.color){
		case WHITE:
			if(currentY + 1 <= UpperLimit && currentX + 1 <= UpperLimit) {
				if(Board.slot(new Coordinate(currentX + 1, currentY + 1)) != null){
					piece = Board.slot(new Coordinate(currentX + 1, currentY + 1));
					if(piece.color != this.color)
						coordinates.add(new Coordinate(currentX + 1, currentY + 1));
					if(piece.type == Pieces.PAWN)
						bannedPawns.add(piece.ID);
				}
			}
			if(currentY + 1 <= UpperLimit && currentX - 1 >= LowerLimit) {
				if(Board.slot(new Coordinate(currentX - 1, currentY + 1)) != null){
					piece = Board.slot(new Coordinate(currentX - 1, currentY + 1));
					if(piece.color != this.color)
						coordinates.add(new Coordinate(currentX - 1, currentY + 1));
					if(piece.type == Pieces.PAWN)
						bannedPawns.add(piece.ID);
				}
			}
			break;
		case BLACK:
			if(currentY - 1 <= UpperLimit && currentX - 1 >= LowerLimit) {
				if(Board.slot(new Coordinate(currentX - 1, currentY - 1)) != null) {
					piece = Board.slot(new Coordinate(currentX - 1, currentY - 1));
					if(piece.color != this.color)
						coordinates.add(new Coordinate(currentX - 1, currentY - 1));
					if(piece.type == Pieces.PAWN)
						bannedPawns.add(piece.ID);
				}
			}
			if(currentY - 1 <= UpperLimit && currentX + 1 <= UpperLimit) {
				if(Board.slot(new Coordinate(currentX + 1, currentY - 1)) != null){
					piece = Board.slot(new Coordinate(currentX + 1, currentY - 1));
					if(piece.color != this.color)
						coordinates.add(new Coordinate(currentX + 1, currentY -1));
					if(piece.type == Pieces.PAWN)
						bannedPawns.add(piece.ID);
				}
			}
			break;
		}
		
		coordinates.addAll(EnPassant());
		
		return coordinates;
	}
	
	/**
	 * Valid coordinates to execute an En Passant
	 * @return A list of coordinates for when the pawn can execute an En Passant move 
	 */
	public List<Coordinate> EnPassant(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentX = currentPosition.getX();
		int currentY = currentPosition.getY();
		Piece piece;
		switch(this.color) {
		case WHITE:
			if(currentY == 5) {
				if(currentX < UpperLimit) {
					piece = Board.slot(new Coordinate(currentX + 1, currentY));
					if(piece != null) {
						if(piece.type == Pieces.PAWN && piece.color != this.color && !bannedPawns.contains(piece.ID))
							coordinates.add(new Coordinate(currentX + 1, currentY + 1));
					}
				}
				if(currentX > LowerLimit) {
					piece = Board.slot(new Coordinate(currentX - 1, currentY));
					if(piece != null) {
						if(piece.type == Pieces.PAWN && piece.color != this.color && !bannedPawns.contains(piece.ID))
							coordinates.add(new Coordinate(currentX - 1, currentY + 1));
					}
				}
			}
			break;
		case BLACK:
			if(currentY == 4) {
				if(currentX < UpperLimit) {
					piece = Board.slot(new Coordinate(currentX + 1, currentY));
					if(piece != null) {
						if(piece.type == Pieces.PAWN && piece.color != this.color && piece.numberOfMoves <= 1 && !bannedPawns.contains(piece.ID))
							coordinates.add(new Coordinate(currentX + 1, currentY - 1));
					}
				}
				if(currentX > LowerLimit) {
					piece = Board.slot(new Coordinate(currentX - 1, currentY));
					if(piece != null) {
						if(piece.type == Pieces.PAWN && piece.color != this.color && piece.numberOfMoves <= 1 && !bannedPawns.contains(piece.ID))
							coordinates.add(new Coordinate(currentX - 1, currentY - 1));
					}
				}
			}
			break;
		}
		return coordinates;
	}
	
	/**
	 * Creates a full set of pawns for both sides.
	 * @return A 16 total Pawn objects (8 White / 8 Black).
	 */
	public static List<Pawn> createPawns(){
		Pawn[] pawns = new Pawn[16];
		
		for(int i = 0; i < pawns.length / 2; i++)
			pawns[i] = new Pawn(Players.BLACK, i + 1, 7);
		
		for(int i = pawns.length / 2; i < pawns.length; i++)
			pawns[i] = new Pawn(Players.WHITE, i - 8 + 1, 2);
		
		return Arrays.asList(pawns);
	}
	
}
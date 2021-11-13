package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

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
	
	public Pawn(Players type, int x, int y) {
		super(type, x, y);
		ConstructRest();
	}
	
	public Pawn(Players type, Coordinate coor) {
		super(type, coor);
		ConstructRest();
	}
	
	public Pawn(Players type) {
		image = (type == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
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
	
	public static List<Pawn> createPawns(){
		Pawn[] pawns = new Pawn[16];
		
		for(int i = 0; i < pawns.length / 2; i++)
			pawns[i] = new Pawn(Players.BLACK, i + 1, 7);
		
		for(int i = pawns.length / 2; i < pawns.length; i++)
			pawns[i] = new Pawn(Players.WHITE, i - 8 + 1, 2);
		
		return Arrays.asList(pawns);
	}
	
}
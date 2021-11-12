package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

public class Pawn extends Piece{
	
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhitePawn.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackPawn.png";
	
	// if white is parallel with black after black is on 2nd row from base, white can take black diagonally
	// vice versa with white
	
	//en passant
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.PAWN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.THICK;
		this.numberOfMoves = 0;
	}
	
	public Pawn(Players type, int x, int y) {
		super(type, x, y);
		ConstructRest();
	}
	
	public Pawn(Players type, Coordinate coor) {
		super(type, coor);
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
		
		// NOTE : DISCONNECT UPPERBOUND EXCEPTION FROM LOWERBOUND EXCEPTION
		switch(this.color){
		case WHITE:
			if(currentY + 1 <= UpperLimit && currentX + 1 <= UpperLimit) {
				if(Board.slot(new Coordinate(currentX + 1, currentY + 1)) != null){
					if(Board.slot(new Coordinate(currentX + 1, currentY + 1)).color != this.color)
						coordinates.add(new Coordinate(currentX + 1, currentY + 1));
				}
			}
			if(currentY + 1 <= UpperLimit && currentX - 1 >= LowerLimit) {
				if(Board.slot(new Coordinate(currentX - 1, currentY + 1)) != null){
					if(Board.slot(new Coordinate(currentX - 1, currentY + 1)).color != this.color)
						coordinates.add(new Coordinate(currentX - 1, currentY + 1));
				}
			}
			break;
		case BLACK:
			if(currentY - 1 <= UpperLimit && currentX - 1 >= LowerLimit) {
				if(Board.slot(new Coordinate(currentX - 1, currentY - 1)) != null) {
					if(Board.slot(new Coordinate(currentX - 1, currentY - 1)).color != this.color)
							coordinates.add(new Coordinate(currentX - 1, currentY - 1));
				}
			}
			if(currentY - 1 <= UpperLimit && currentX + 1 <= UpperLimit) {
				if(Board.slot(new Coordinate(currentX + 1, currentY - 1)) != null){
					if(Board.slot(new Coordinate(currentX + 1, currentY - 1)).color != this.color)
						coordinates.add(new Coordinate(currentX + 1, currentY -1));
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
						if(piece.type == Pieces.PAWN && piece.color != this.color)
							coordinates.add(new Coordinate(currentX + 1, currentY + 1));
					}
				}
				if(currentX > LowerLimit) {
					piece = Board.slot(new Coordinate(currentX - 1, currentY));
					if(piece != null) {
						if(piece.type == Pieces.PAWN && piece.color != this.color)
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
						if(piece.type == Pieces.PAWN && piece.color != this.color && piece.numberOfMoves <= 1)
							coordinates.add(new Coordinate(currentX + 1, currentY - 1));
					}
				}
				if(currentX > LowerLimit) {
					piece = Board.slot(new Coordinate(currentX - 1, currentY));
					if(piece != null) {
						if(piece.type == Pieces.PAWN && piece.color != this.color && piece.numberOfMoves <= 1)
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
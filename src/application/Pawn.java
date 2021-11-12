package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import application.Enums.Players;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends Piece{
	
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhitePawn.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackPawn.png";
	
	public Pawn(Players type, int x, int y) {
		super(type, x, y);
		this.type = Pieces.PAWN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	public Pawn(Players type, Coordinate coor) {
		super(type, coor);
		this.type = Pieces.PAWN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
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
			if(currentY < UpperLimit && Board.slot(new Coordinate(currentX, currentY + 1)) == null)
				coordinates.add(new Coordinate(currentX, currentY + 1));
			if(!hasMoved && currentY < UpperLimit - 1 && Board.slot(new Coordinate(currentX, currentY + 2)) == null)
				coordinates.add(new Coordinate(currentX, currentY + 2));
			break;
		case BLACK:
			if(currentY > LowerLimit && Board.slot(new Coordinate(currentX, currentY - 1)) == null)
				coordinates.add(new Coordinate(currentX, currentY - 1));
			if(!hasMoved && currentY > LowerLimit + 1 && Board.slot(new Coordinate(currentX, currentY - 2)) == null)
				coordinates.add(new Coordinate(currentX, currentY - 2));
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
			if(currentY + 1 <= UpperLimit && currentX - 1 >= LowerLimit && currentX + 1 <= UpperLimit) {
				if(Board.slot(new Coordinate(currentX + 1, currentY + 1)) != null){
					if(Board.slot(new Coordinate(currentX + 1, currentY + 1)).color != this.color)
						coordinates.add(new Coordinate(currentX + 1, currentY + 1));
				}
				if(Board.slot(new Coordinate(currentX - 1, currentY + 1)) != null){
					if(Board.slot(new Coordinate(currentX - 1, currentY + 1)).color != this.color)
						coordinates.add(new Coordinate(currentX - 1, currentY + 1));
				}
			}
			break;
		case BLACK:
			if(currentY - 1 <= UpperLimit && currentX - 1 >= LowerLimit && currentX + 1 <= UpperLimit) {
				if(Board.slot(new Coordinate(currentX - 1, currentY - 1)) != null) {
					if(Board.slot(new Coordinate(currentX - 1, currentY - 1)).color != this.color)
							coordinates.add(new Coordinate(currentX - 1, currentY - 1));
				}
				if(Board.slot(new Coordinate(currentX + 1, currentY - 1)) != null){
					if(Board.slot(new Coordinate(currentX + 1, currentY - 1)).color != this.color)
						coordinates.add(new Coordinate(currentX + 1, currentY -1));
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
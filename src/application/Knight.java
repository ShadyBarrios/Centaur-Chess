package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import application.Enums.Players;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Knight extends Piece{
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteKnight.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackKnight.png";
	
	public Knight(Players type, int x, int y) {
		super(type, x, y);
		this.type = Pieces.KNIGHT;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	public Knight(Players type, Coordinate coor) {
		super(type, coor);
		this.type = Pieces.KNIGHT;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
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
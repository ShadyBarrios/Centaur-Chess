package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.image.ImageView;

public class Knight extends Piece{
	
	public Knight(Players type, int x, int y) {
		super(type, x, y);
		this.piece = Pieces.KNIGHT;
	}
	
	public Knight(Players type, Coordinate coor) {
		super(type, coor);
		this.piece = Pieces.KNIGHT;
	}
	
	@Override
	public boolean isOnLine(Coordinate coor) {
		boolean result;
		
		if(currentPosition.matches(coor))
			result = true;
		else 
			result = false;
		
		// under progress
		
		return result;
	}
	
	@Override
	public String getInitial() {return "N";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.add(currentPosition);
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
		if(currentX + 1 <= UpperLimit && currentY + 2 <= UpperLimit) 
			coordinates.add(new Coordinate(currentX + 1, currentY + 2));
		
		// for short one
		if(currentX + 2 <= UpperLimit && currentY + 1 <= UpperLimit)
			coordinates.add(new Coordinate(currentX + 2, currentY + 1));
		
		return coordinates;
	}
	
	public List<Coordinate> Q2Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX - 1 >= LowerLimit && currentY + 2 <= UpperLimit) 
			coordinates.add(new Coordinate(currentX - 1, currentY + 2));
		
		// for short one
		if(currentX - 2 >= LowerLimit && currentY + 1 <= UpperLimit)
			coordinates.add(new Coordinate(currentX - 2, currentY + 1));
		
		return coordinates;
	}
	
	public List<Coordinate> Q3Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX - 1 >= LowerLimit && currentY - 2 >= LowerLimit) 
			coordinates.add(new Coordinate(currentX - 1, currentY - 2));
		
		// for short one
		if(currentX - 2 >= LowerLimit && currentY - 1 >= LowerLimit)
			coordinates.add(new Coordinate(currentX - 2, currentY - 1));
		
		return coordinates;
	}
	
	public List<Coordinate> Q4Coordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = currentPosition.getX();
		final int currentY = currentPosition.getY();
		
		// for tall one
		if(currentX + 1 <= UpperLimit && currentY - 2 >= LowerLimit) 
			coordinates.add(new Coordinate(currentX + 1, currentY - 2));
		
		// for short one
		if(currentX + 2 <= UpperLimit && currentY - 1 >= LowerLimit)
			coordinates.add(new Coordinate(currentX + 2, currentY - 1));
		
		return coordinates;
	}
	
}
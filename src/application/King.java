package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.image.ImageView;

public class King extends Piece{
	
	public King(Players type, int x, int y) {
		super(type, x, y);
		this.piece = Pieces.KING;
	}
	
	public King(Players type, Coordinate coor) {
		super(type, coor);
		this.piece = Pieces.KING;
	}
	
	@Override
	public boolean isOnLine(Coordinate coor) {
		boolean result;
		// x dif and y dif get the absolute value of the distances in the axes
		// if |change in x| == |change in y|, |slope| is 1 and is on path for King
		if(currentPosition.matchesX(coor) || currentPosition.matchesY(coor))
			result = true;
		else if(currentPosition.xDifference(coor) == currentPosition.yDifference(coor))
			result = true;
		else 
			result = false;
		
		return result;
	}
	
	@Override
	public String getInitial() {return "K";}
	
	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(topCoordinates());	
		coordinates.addAll(middleCoordinates());
		coordinates.addAll(bottomCoordinates());
		
		return coordinates;
	}
	
	private List<Coordinate> topCoordinates(){
		return (this.currentPosition.getY() == 8) ? new ArrayList<Coordinate>() : surroundingCoordinates(1);
	}
	
	private List<Coordinate> middleCoordinates(){
		return surroundingCoordinates(0);
	}
	
	private List<Coordinate> bottomCoordinates(){
		return (this.currentPosition.getY() == 1) ? new ArrayList<Coordinate>() : surroundingCoordinates(-1);
	}
}
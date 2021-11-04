package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.image.ImageView;

public class Queen extends Piece{
	
	public Queen(Players type, int x, int y) {
		super(type, x, y);
		this.piece = Pieces.QUEEN;
	}
	
	public Queen(Players type, Coordinate coor) {
		super(type, coor);
		this.piece = Pieces.QUEEN;
	}
	
	@Override
	public boolean isOnLine(Coordinate coor) {
		boolean result;
		// x dif and y dif get the absolute value of the distances in the axes
		// if |change in x| == |change in y|, |slope| is 1 and is on path for queen
		if(currentPosition.matchesX(coor) || currentPosition.matchesY(coor))
			result = true;
		else if(currentPosition.xDifference(coor) == currentPosition.yDifference(coor))
			result = true;
		else 
			result = false;
		
		return result;
	}
	
	@Override
	public String getInitial() {return "Q";}
	
	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(verticalCoordinates());	
		coordinates.addAll(horizontalCoordinates());
		coordinates.addAll(negativeDiagonalCoordinates());
		coordinates.addAll(positiveDiagonalCoordinates());
		
		return coordinates;
	}
}
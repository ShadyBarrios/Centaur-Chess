package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.image.ImageView;

public class Bishop extends Piece{
	
	public Bishop(Players type, int x, int y) {
		super(type, x, y);
		this.piece = Pieces.BISHOP;
	}
	
	public Bishop(Players type, Coordinate coor) {
		super(type, coor);
		this.piece = Pieces.BISHOP;
	}
	
	@Override
	public boolean isOnLine(Coordinate coor) {
		boolean result;
		// x dif and y dif get the absolute value of the distances in the axes
		// if |change in x| == |change in y|, |slope| is 1 and is on path for Bishop
		
		if(currentPosition.matches(coor))
			result = true;
		else if(currentPosition.xDifference(coor) == currentPosition.yDifference(coor))
			result = true;
		else 
			result = false;
		
		return result;
	}
	
	@Override
	public String getInitial() {return "B";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(negativeDiagonalCoordinates());
		coordinates.addAll(positiveDiagonalCoordinates());
		
		return coordinates;
	}
}
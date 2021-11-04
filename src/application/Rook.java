package application;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javafx.scene.image.ImageView;

public class Rook extends Piece{
	
	public Rook(Players type, int x, int y) {
		super(type, x, y);
		this.piece = Pieces.ROOK;
	}
	
	public Rook(Players type, Coordinate coor) {
		super(type, coor);
		this.piece = Pieces.ROOK;
	}
	
	@Override
	public boolean isOnLine(Coordinate coor) {
		boolean result;
		
		if(currentPosition.matchesX(coor) || currentPosition.matchesY(coor))
			result = true;
		else 
			result = false;
		
		return result;
	}
	
	@Override
	public String getInitial() {return "R";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(verticalCoordinates());
		coordinates.addAll(horizontalCoordinates());
		
		return coordinates;
	}
}
package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class Bishop extends Piece{
	
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteBishop.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackBishop.png";
	
	public Bishop(Players type, int x, int y) {
		super(type, x, y);
		this.type = Pieces.BISHOP;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	public Bishop(Players type, Coordinate coor) {
		super(type, coor);
		this.type = Pieces.BISHOP;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
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
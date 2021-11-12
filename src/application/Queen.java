package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class Queen extends Piece{
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteQueen.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackQueen.png";
	
	@Override
	protected void ConstructRest() {
		this.type = Pieces.QUEEN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
		this.thickness = Thickness.SKINNY;
	}
	
	public Queen(Players type, int x, int y) {
		super(type, x, y);
		ConstructRest();
	}
	
	public Queen(Players type, Coordinate coor) {
		super(type, coor);
		ConstructRest();
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
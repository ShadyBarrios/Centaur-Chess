package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class Queen extends Piece{
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteQueen.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackQueen.png";
	public Queen(Players type, int x, int y) {
		super(type, x, y);
		this.type = Pieces.QUEEN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	public Queen(Players type, Coordinate coor) {
		super(type, coor);
		this.type = Pieces.QUEEN;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
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
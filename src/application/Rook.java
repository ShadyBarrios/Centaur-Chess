package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class Rook extends Piece{
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteRook.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackRook.png";
	
	public Rook(Players type, int x, int y) {
		super(type, x, y);
		this.type = Pieces.ROOK;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	public Rook(Players type, Coordinate coor) {
		super(type, coor);
		this.type = Pieces.ROOK;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
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
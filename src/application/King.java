package application;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class King extends Piece{
	
	String PathToWhite = "file:///C:/Users/scott/Downloads/WhiteKing.png";
	String PathToBlack = "file:///C:/Users/scott/Downloads/BlackKing.png";
	
	public King(Players type, int x, int y) {
		super(type, x, y);
		this.type = Pieces.KING;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	public King(Players type, Coordinate coor) {
		super(type, coor);
		this.type = Pieces.KING;
		image = (this.color == Players.BLACK) ? new Image(PathToBlack) : new Image(PathToWhite);
	}
	
	@Override
	public String getInitial() {return "K";}
	
	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(topCoordinates());	
		coordinates.addAll(middleCoordinates());
		coordinates.addAll(bottomCoordinates());
		coordinates.addAll(castleCoordinates());
		
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
	
	private List<Coordinate> castleCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		if(this.hasMoved)
			return coordinates;
		
		int currentY = this.currentPosition.getY();
		
		if(this.CanCastleRight())
			coordinates.add(new Coordinate(7, currentY));
		if(this.CanCastleLeft())
			coordinates.add(new Coordinate(3, currentY));
		
		return coordinates;
	}
	
	
}
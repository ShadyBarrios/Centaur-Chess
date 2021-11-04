package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

abstract class Piece implements Enums{
	protected ImageView image; // image shown in FXML
	
	boolean hasMoved;
	
	protected Players color;
	protected Pieces piece;
	
	// applys for both x and y
	public static final int UpperLimit = 8, LowerLimit = 1;
	
	protected Coordinate currentPosition;
	
	public Piece(Players color, int x, int y) {
		setPosition(x,y);
		this.color = color;
		this.hasMoved = false;
	}
	
	public Piece(Players color, Coordinate coor) {
		this.currentPosition = coor;
		this.color = color;
		this.hasMoved = false;
	}
	
	public void moved() {this.hasMoved = true;}
	
	public void setPosition(int x, int y) {this.currentPosition = new Coordinate(x,y);}
	public void setPosition(Coordinate coor) {this.currentPosition = coor;}
	
	public Coordinate getPostition() { return this.currentPosition;}
	
	abstract public List<Coordinate> getAvaliablePositions();
	abstract public String getInitial();
	abstract public boolean isOnLine(Coordinate coor);
	
	// finds the vertical line
	protected List<Coordinate> verticalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		
		for(int y = 1; y <= UpperLimit; y++) 
			coordinates.add(new Coordinate(currentX, y));
		
		return coordinates;
	}
	
	// finds the horizontal line
	protected List<Coordinate> horizontalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentY = this.currentPosition.getY();
		
		for(int x = 1; x <= UpperLimit; x++) 
			coordinates.add(new Coordinate(x, currentY));
		
		return coordinates;
	}
	
	// finds the negative slope diagonal line
	protected List<Coordinate> negativeDiagonalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.addAll(negativeDiagonalLeft());
		coordinates.addAll(negativeDiagonalRight());
		return coordinates;
	}
	
	// find the negative slope diagonal on the left side
	private List<Coordinate> negativeDiagonalLeft(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		final int currentY = this.currentPosition.getY();
		int iterX = 0, iterY = 0;
		
		do{
			coordinates.add(new Coordinate(currentX - iterX, currentY + iterY));
			iterX++; iterY++;
		}while(currentX - iterX >= LowerLimit && currentY + iterY <= UpperLimit);
		
		return coordinates;
	}
	
	// finds the negative slope on the right side
	private List<Coordinate> negativeDiagonalRight(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		final int currentY = this.currentPosition.getY();
		int iterX = 0, iterY = 0;
		
		do{
			coordinates.add(new Coordinate(currentX + iterX, currentY - iterY));
			iterX++; iterY++;	
		}while(currentX + iterX <= UpperLimit && currentY - iterY >= LowerLimit);
		
		return coordinates;
	}
	
	// finds the positive slope diagonal
	protected List<Coordinate> positiveDiagonalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.addAll(positiveDiagonalRight());
		coordinates.addAll(positiveDiagonalLeft());
		return coordinates;
	}
	
	// finds the positive slope diagonal on the right side
	private List<Coordinate> positiveDiagonalRight(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		final int currentY = this.currentPosition.getY();
		int iterX = 0, iterY = 0;
		
		do{
			coordinates.add(new Coordinate(currentX + iterX, currentY + iterY));
			iterX++; iterY++;	
		}while(currentX + iterX <= UpperLimit && currentY + iterY <= UpperLimit);
		
		return coordinates;
	}
	
	// finds the positive slope diagonal on the left side
	private List<Coordinate> positiveDiagonalLeft(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		final int currentY = this.currentPosition.getY();
		int iterX = 0, iterY = 0;
		
		do{
			coordinates.add(new Coordinate(currentX - iterX, currentY - iterY));
			iterX++; iterY++;	
		}while(currentX - iterX >= LowerLimit && currentY - iterY >= LowerLimit);
		
		return coordinates;
	}
	
	protected List<Coordinate> surroundingCoordinates(int yManip){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentX = this.currentPosition.getX();
		int currentY = this.currentPosition.getY();
		
		for(int i = -1; i <= 1; i++) 
			if(currentX + i >= LowerLimit && currentX + i <= UpperLimit)
				coordinates.add(new Coordinate(currentX + i, currentY + yManip));
		
		return coordinates;
	}
	
	public String toString() {
		String str = "Color: " + color + "\nPiece: " + piece + "\nCoordinates: " + currentPosition;
		return str;
	}
}
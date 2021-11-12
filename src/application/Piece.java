package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.Enums.Players;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

abstract class Piece implements Enums{
	protected Image image; // Image shown in FXML
	
	boolean hasMoved;
	
	protected Players color;
	protected Pieces type;
	
	// applys for both x and y
	public static final int UpperLimit = 8, LowerLimit = 1;
	protected int ID;
	private static List<Integer> createdIDs = new ArrayList<Integer>();
	protected Coordinate currentPosition;
	protected String PathToBlack, PathToWhite;
	
	public Piece(Players color, int x, int y) {
		setPosition(x,y);
		this.color = color;
		this.ID = createRandomID();
		this.hasMoved = false;
	}
	
	public Piece(Players color, Coordinate coor) {
		this.currentPosition = coor;
		this.color = color;
		this.ID = createRandomID();
		this.hasMoved = false;
	}
	
	public void moved() {this.hasMoved = true;}
	
	public Image getImage() {return this.image;}
	
	public void setPosition(int x, int y) {this.currentPosition = new Coordinate(x,y);}
	public void setPosition(Coordinate coor) {this.currentPosition = coor;}
	
	public Coordinate getPosition() { return this.currentPosition;}
	
	abstract public List<Coordinate> getAvaliablePositions();
	abstract public String getInitial();

	public boolean isOnLine(Coordinate coor) {
		for(Coordinate valid : getAvaliablePositions())
			if(coor.matches(valid)) 
				return true;
		
		return false;
	}
	
	// finds the vertical line
	protected List<Coordinate> verticalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(verticalTop());
		coordinates.addAll(verticalBottom());
		
		return coordinates;
	}
	
	protected List<Coordinate> verticalTop(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		Piece slot;
		
		for(int y = this.currentPosition.getY(); y <= UpperLimit; y++) {
			coordinates.add(new Coordinate(currentX, y));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}
		}
		
		return coordinates;
	}
	
	protected List<Coordinate> verticalBottom(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		final int currentX = this.currentPosition.getX();
		Piece slot;
		
		for(int y = this.currentPosition.getY(); y >= LowerLimit; y--) {
			coordinates.add(new Coordinate(currentX, y));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color) 
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}
		}
		
		return coordinates;
	}
	
	// finds the horizontal line
	protected List<Coordinate> horizontalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(horizontalLeft());
		coordinates.addAll(horizontalRight());
		
		return coordinates;
	}
	
	private List<Coordinate> horizontalLeft(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentY = this.getPosition().getY();
		Piece slot;
		
		for(int x = this.getPosition().getX(); x >= LowerLimit; x--) {
			coordinates.add(new Coordinate(x, currentY));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}	
		}
		return coordinates;
	}
	
	private List<Coordinate> horizontalRight(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentY = this.getPosition().getY();
		Piece slot;
		
		for(int x = this.getPosition().getX(); x <= UpperLimit; x++) {
			coordinates.add(new Coordinate(x, currentY));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}	
		}
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
		Piece slot;
		int iterX = 0, iterY = 0;
		
		do{
			coordinates.add(new Coordinate(currentX - iterX, currentY + iterY));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}
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
		Piece slot;
		
		do{
			coordinates.add(new Coordinate(currentX + iterX, currentY - iterY));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}
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
		Piece slot;
		
		do{
			coordinates.add(new Coordinate(currentX + iterX, currentY + iterY));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}
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
		Piece slot;
		
		do{
			coordinates.add(new Coordinate(currentX - iterX, currentY - iterY));
			slot = Board.slot(coordinates.get(coordinates.size() - 1));
			if(slot != null) {
				if(slot.color == this.color)
					coordinates.remove(coordinates.size() - 1);
				if(slot.ID != this.ID)
					return coordinates;
			}
				
			iterX++; iterY++;	
		}while(currentX - iterX >= LowerLimit && currentY - iterY >= LowerLimit);
		
		return coordinates;
	}
	
	protected List<Coordinate> surroundingCoordinates(int yManip){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentX = this.currentPosition.getX();
		int currentY = this.currentPosition.getY();
		
		/* ex: current coordinate (2, 2), y manip = -1
		 * starts at (1, 1)
		 * iter 1: new coordinate = (1, 1)
		 * iter 2: new coordinate = (2, 1)
		 * iter 3: new coordinate = (3, 1)
		 */
		
		for(int i = -1; i <= 1; i++) {
			if(currentX + i >= LowerLimit && currentX + i <= UpperLimit) { 
				coordinates.add(new Coordinate(currentX + i, currentY + yManip));
				if(Board.slot(new Coordinate(currentX + i, currentY + yManip)) != null 
						&& Board.slot(new Coordinate(currentX + i, currentY + yManip)).color == this.color)
					coordinates.remove(coordinates.size() - 1);
			}
		}
		
		return coordinates;
	}
	
	public static List<Piece> CreatePieces(){
		Queen Queen1 = new Queen(Players.BLACK, 4, 8);
		Queen Queen2 = new Queen(Players.WHITE, 4, 1);
		
		List<Queen> queens = Arrays.asList(Queen1, Queen2);
		
		Bishop Bishop1 = new Bishop(Players.WHITE, 3, 1);
		Bishop Bishop2 = new Bishop(Players.WHITE, 6, 1);
		Bishop Bishop3 = new Bishop(Players.BLACK, 3, 8);
		Bishop Bishop4 = new Bishop(Players.BLACK, 6, 8);
		
		List<Bishop> bishops = Arrays.asList(Bishop1, Bishop2, Bishop3, Bishop4);
		
		Rook Rook1 = new Rook(Players.WHITE, 1, 1);
		Rook Rook2 = new Rook(Players.WHITE, 8, 1);
		Rook Rook3 = new Rook(Players.BLACK, 1, 8);
		Rook Rook4 = new Rook(Players.BLACK, 8, 8);
		
		List<Rook> rooks = Arrays.asList(Rook1, Rook2, Rook3, Rook4);
		
		Knight Knight1 = new Knight(Players.WHITE, 2, 1);
		Knight Knight2 = new Knight(Players.WHITE, 7, 1);
		Knight Knight3 = new Knight(Players.BLACK, 2, 8);
		Knight Knight4 = new Knight(Players.BLACK, 7, 8);
		
		List<Knight> knights = Arrays.asList(Knight1, Knight2, Knight3, Knight4);
		
		King King1 = new King(Players.BLACK, 5, 8);
		King King2 = new King(Players.WHITE, 5, 1);
		
		List<King> kings = Arrays.asList(King1, King2);
		
		List<Pawn> pawns = Pawn.createPawns();
		
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.addAll(kings);
		pieces.addAll(queens);
		pieces.addAll(pawns);
		pieces.addAll(knights);
		pieces.addAll(rooks);
		pieces.addAll(bishops);
		
		return pieces;
	}
	
	public String toString() {
		String str = "Color: " + color + "\nPiece: " + type + "\nCoordinates: " + currentPosition;
		return str;
	}
	
	public boolean isCastleCoordinate(Coordinate coor) {
		if(this.type == Pieces.KING) {
			if(CanCastleRight())
				if(coor.matches(new Coordinate(this.currentPosition.getX() + 2, this.currentPosition.getY())))
					return true;
			if(CanCastleLeft())
				if(coor.matches(new Coordinate(this.currentPosition.getX() - 2, this.currentPosition.getY())))
					return true;
		}
		return false;
	}
	
	protected boolean CanCastleRight() {
		boolean CanCastleRight = true;
		int currentX = this.getPosition().getX();
		int currentY = this.getPosition().getY();
		if(this.hasMoved) return false; 
		if(Board.slot(new Coordinate(this.currentPosition.getX() + 3, this.currentPosition.getY())) != null) {
			if(Board.slot(new Coordinate(this.currentPosition.getX() + 3, this.currentPosition.getY())).hasMoved) 
				return false;
		}
		else 
			return false;
			
		for(int x = currentX + 1; x < UpperLimit; x++)
			if(Board.slot(new Coordinate(x, currentY)) != null) CanCastleRight = false;
		
		return CanCastleRight;
	}
	
	protected boolean CanCastleLeft() {
		boolean CanCastleLeft = true;
		int currentX = this.getPosition().getX();
		int currentY = this.getPosition().getY();
		
		if(this.hasMoved) return false; 
		if(Board.slot(new Coordinate(this.currentPosition.getX() - 4, this.currentPosition.getY())) != null) {
			if(Board.slot(new Coordinate(this.currentPosition.getX() - 4, this.currentPosition.getY())).hasMoved)
				return false;
		}
		else
			return false;
		
		for(int x = currentX - 1; x > LowerLimit; x--)
			if(Board.slot(new Coordinate(x, currentY)) != null) CanCastleLeft = false;
		return CanCastleLeft;
	}
	
	private int createRandomID() {
		int rng = (int)((Math.random() * 99999) + 1); // 1 -> 99999
		if(createdIDs.contains(rng))
			return createRandomID();
		else {
			createdIDs.add(rng);
			return rng;
		}
	}
}
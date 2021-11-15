package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;

/**
 * Abstract class representing a chess piece
 * @author Shady
 */
abstract class Piece implements Enums{
	protected Image image; // Image shown in FXML
	
	protected int numberOfMoves;
	
	boolean hasMoved;
	
	protected Players color;
	protected Pieces type;
	protected Thickness thickness;
	
	public static final int UpperLimit = 8, LowerLimit = 1;
	protected int ID;
	private static List<Integer> createdIDs = new ArrayList<Integer>();
	protected Coordinate currentPosition;
	protected String PathToBlack, PathToWhite;
	
	public Piece() {}
	
	/**
	 * @param color - The color of player the piece belongs to.
	 * @param x - The column the piece is on.
	 * @param y - The row the piece is on.
	 */
	public Piece(Players color, int x, int y) {
		setPosition(x,y);
		this.color = color;
		this.ID = createRandomID();
		this.hasMoved = false;
		this.numberOfMoves = 0;
	}
	
	/**
	 * @param color - The player the piece belongs to.
	 * @param coor - The coordinate the piece will be placed on.
	 */
	public Piece(Players color, Coordinate coor) {
		setPosition(coor);
		this.color = color;
		this.ID = createRandomID();
		this.numberOfMoves = 0;
	}
	
	/**
	 * Number of moves and hasMoved boolean are updated.
	 */
	public void moved() {
		this.hasMoved = true; 
		this.numberOfMoves++;
	}
	
	/** 
	 * Each piece has PNG representing the piece's type.
	 * @return The image associated with the piece.
	 */
	public Image getImage() {
		return this.image;
	}
	
	/**
	 * Moves the piece to the CCS coordinate based on the info given.
	 * @param x - The column the piece is moved to.
	 * @param y - The row the piece is moved to.
	 */
	public void setPosition(int x, int y) {
		this.currentPosition = new Coordinate(x,y);
	}
	
	/**
	 * After a move, each piece acquires a new position.
	 * @param coor - The new position of the piece.
	 */
	public void setPosition(Coordinate coor) {
		this.currentPosition = coor;
	}
	
	/**
	 * @return A Coordinate instance variable with the location of the piece.
	 */
	public Coordinate getPosition() {
		return this.currentPosition;
	}
	
	/**
	 * Each type of piece has their own system of finding available positions
	 * @return A list of all the available coordinates the piece can be moved to.
	 */
	abstract public List<Coordinate> getAvaliablePositions();
	
	/**
	 * Each piece has an initial representing its type.
	 * @return A one char string.
	 */
	abstract public String getInitial();
	
	/**
	 * Constructs the rest of the piece that isn't made by inherited constructors.
	 */
	abstract protected void ConstructRest();

	/**
	 * Calculates all the possible moves of the pieces and then determines if the argument is valid.
	 * @return boolean indicating if the piece can move to the passed coordinate.
	 */
	public boolean isOnLine(Coordinate coor) {
		for(Coordinate valid : getAvaliablePositions())
			if(coor.matches(valid)) 
				return true;
		
		return false;
	}
	
	/**
	 * The process to find the available vertical coordinates above and below the piece
	 * is split between two methods: {@link #verticalTop()} and {@link #verticalBottom()}
	 * @return All the coordinates in a piece's column that the piece can be moved to.
	 */
	protected List<Coordinate> verticalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(verticalTop());
		coordinates.addAll(verticalBottom());
		
		return coordinates;
	}
	
	/**
	 * @return The top half of the {@link #verticalCoordinates()} method
	 */
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
	
	/**
	 * @return The bottom half of the {@link #verticalCoordinates()} method
	 */
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
	
	/**
	 * The process to find the available horizontal coordinates to the left and right of the piece
	 * is split between two methods: {@link #horizontalLeft()} and {@link #horizontalRight()}
	 * @return All the coordinates in a piece's row that the piece can be moved to.
	 */
	
	protected List<Coordinate> horizontalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.addAll(horizontalLeft());
		coordinates.addAll(horizontalRight());
		
		return coordinates;
	}
	
	/**
	 * @return The left half of the {@link #horizontalCoordinates()} method
	 */
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
	
	/**
	 * @return A list including right half of the {@link #horizontalCoordinates()} method
	 */
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
	
	/**
	 * The negative diagonal line is identical to a y = -x line. 
	 * Coordinates that contain pieces of the same color or the piece itself are not in the returned list.
	 * The process to find the available coordinates is split between two methods: 
	 * {@link #negativeDiagonalLeft()} and {@link #negativeDiagonalRight()}
	 * @return A list of coordinates the piece can be moved to based on a negatively sloped diagonal line. 
	 */
	protected List<Coordinate> negativeDiagonalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.addAll(negativeDiagonalLeft());
		coordinates.addAll(negativeDiagonalRight());
		return coordinates;
	}
	
	/**
	 * @return The left half of the {@link #negativeDiagonalCoordinates()} method
	 */
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
	
	/**
	 * @return The right half of the {@link #negativeDiagonalCoordinates()} method 
	 */
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
	
	/**
	 * The positive diagonal line is identical to a y = x line
	 * Coordinates that contain pieces of the same color or the piece itself are not in the returned list.
	 * The process to find the available coordinates is split between two methods: 
	 * {@link #positiveDiagonalRight()} and {@link #positiveDiagonalLeft()}
	 * @return All the coordinates the piece can be moved to based on a positively sloped diagonal line. 
	 */
	protected List<Coordinate> positiveDiagonalCoordinates(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		coordinates.addAll(positiveDiagonalRight());
		coordinates.addAll(positiveDiagonalLeft());
		return coordinates;
	}
	
	/**
	 * @return The right half of the {@link #positiveDiagonalCoordinates()} method
	 */
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
	
	/**
	 * @return The left half of the positiveDiagonalCoordinates() method
	 */
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
	
	/**
	 * Finds the coordinates of tiles directly in contact with the piece's tile.
	 * @param yManip - Changes the row to acquire coordinates from with respect to the piece's current row.
	 * @return A list of valid coordinates that surround the piece.
	 */
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
		
		for(int xManip = -1; xManip <= 1; xManip++) {
			if(currentX + xManip >= LowerLimit && currentX + xManip <= UpperLimit) { 
				coordinates.add(new Coordinate(currentX + xManip, currentY + yManip));
				if(Board.slot(new Coordinate(currentX + xManip, currentY + yManip)) != null 
						&& Board.slot(new Coordinate(currentX + xManip, currentY + yManip)).color == this.color)
					coordinates.remove(coordinates.size() - 1);
			}
		}
		
		return coordinates;
	}
	
	/**
	 * The process to create all the pieces for the board.
	 * @return A full set of Chess Pieces with preset coordinates.
	 */
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
	
	/**
	 * @return A string of the type (Piece type).
	 */
	public String toString() {
		return "" + type;
	}
	
	/**
	 * Equivalent to a .equals()
	 * @param piece - The piece being compared with.
	 * @return True if argument matches this piece's type and color. False if either of the two conditions aren't met.
	 */
	public boolean matches(Piece piece) {return (this.type == piece.type && this.color == piece.color);}
	
	/**
	 * A king moving to a coordinate might either result in a castle move or regular move.
	 * The process to determine if it can castle to either side is split between two methods:
	 * {@link #CanCastleLeft()} and {@link #CanCastleRight()}
	 * @param coor - The coordinate the player wants to move the piece to.
	 * @return Boolean indicating whether or not the requested move should result in a castle move.
	 */
	public boolean isCastleCoordinate(Coordinate coor) {
		if(this.type == Pieces.KING) {
			if(CanCastleRight()) {
				if(coor.matches(new Coordinate(this.currentPosition.getX() + 2, this.currentPosition.getY())))
					return true;
			}
			if(CanCastleLeft()) {
				if(coor.matches(new Coordinate(this.currentPosition.getX() - 2, this.currentPosition.getY())))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * @return Boolean indicating whether or not the king piece can castle with its right-hand side rook
	 */
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
	
	/**
	 * @return Boolean indicating whether or not the king piece can castle with its left-hand side rook
	 */
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
	
	/**
	 * Pawns can only en passant other pawns.
	 * @param coor - The coordinate the player wants to move the piece to.
	 * @return Boolean indicating whether or not the requested move should result in an En Passant move.
	 */
	public boolean IsEnPassantCoordinate(Coordinate coor) {
		boolean contains = false;
		List<Coordinate> coordinates;
		Pawn pawn;
		
		if(this.type == Pieces.PAWN) {
			pawn = (Pawn)this;
			coordinates = pawn.EnPassant();
			for(Coordinate coordinate : coordinates)
				if(coordinate.matches(coor)) contains = true;
			
			if(contains)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Each piece having a unique id can help in filtering processes.
	 * @return A unique randomly generated int ID within range 1 to 99999.
	 */
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
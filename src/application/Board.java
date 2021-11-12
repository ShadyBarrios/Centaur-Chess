package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Board extends Coordinate implements Enums{
	private String[][] PlayingBoardDisplay;
	public static Piece[][] PlayingBoard;
	private Pane[][] PlayingBoardPane;
	public static Players turn;
	private Piece PieceSelected;
	private final Paint BLUE = Paint.valueOf("#49d6e9"), GREEN = Paint.valueOf("#75c367"), WHITE = Paint.valueOf("#ffffff");
	private List<Piece> EliminatedPieces;
	private TextField Messenger;
	private boolean justEliminated;
	private boolean GameOver;
	private GridPane grid;
	
	public Board() {
		PlayingBoardDisplay = new String[8][8];
		PlayingBoard = new Piece[8][8];
		PlayingBoardPane = new Pane[8][8];
		EliminatedPieces = new ArrayList<Piece>();
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				PlayingBoard[y][x] = null;
				PlayingBoardPane[y][x] = null;
				PlayingBoardDisplay[y][x] = "-";
			}
		}
		PieceSelected = null;
		justEliminated = false;
		GameOver = false;
		turn = Players.WHITE;
	}
	
	public Board(List<Piece> pieces) {
		this();
		addAll(pieces);	
	}
	
	public Board(List<Piece> pieces, GridPane grid, TextField Messenger) {
		this(pieces);
		for(int y = 1; y <= 8; y++) {
			for(int x = 1; x <= 8; x++) {
				PlayingBoardPane[y - 1][x - 1] = (Pane) grid.getChildren().get(new Coordinate(x, y).toPaneCoordinate());
			}
		}
		SetUpButtons();
		this.grid = grid;
		this.Messenger = Messenger;
		SetMessage(turn + " Player Turn");
	}
	
	public void addAll(List<Piece> pieces) {
		pieces.forEach(piece -> PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = piece);
		UpdateConsoleDisplay();
	}
	private void PlayerWon(Players winner) {
		SetMessage("WINNER: " + winner);
		grid.setDisable(true);
		GameOver = true;
	}
	
	public boolean MovePiece(Piece piece, Coordinate NewCoor) {
		
		if(!piece.isOnLine(NewCoor)) {
			SetMessage("Illegal Move; Turn - " + turn);
			return false;
		}
		if(slot(NewCoor) != null) {		
			EliminatedPieces.add(slot(NewCoor));
			PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
			piece.setPosition(NewCoor);
			piece.moved();
			PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
			
			if(EliminatedPieces.get(EliminatedPieces.size() - 1).type == Pieces.KING) 
				PlayerWon(turn);
			else
				return true;
		}
		
		if(piece.isCastleCoordinate(NewCoor)) {
			Piece rook;
			// right side castle
			if(NewCoor.getX() > piece.currentPosition.getX()) {
				rook = Board.slot(new Coordinate(8, piece.getPosition().getY()));
				// set king
				PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
				piece.setPosition(NewCoor);
				piece.moved();
				PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
				
				// set rook
				PlayingBoard[rook.getPosition().getY() - 1][rook.getPosition().getX() - 1] = null;
				rook.setPosition(new Coordinate(NewCoor.getY(), NewCoor.getX() - 1));
				rook.moved();
				PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 2] = rook;
			}
			else { // left side castle
				rook = Board.slot(new Coordinate(1, piece.getPosition().getY()));
				// set king
				PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
				piece.setPosition(NewCoor);
				piece.moved();
				PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
				
				// set rook
				PlayingBoard[rook.getPosition().getY() - 1][rook.getPosition().getX() - 1] = null;
				rook.setPosition(new Coordinate(NewCoor.getY(), NewCoor.getX() + 1));
				rook.moved();
				PlayingBoard[NewCoor.getY() - 1][NewCoor.getX()] = rook;
			}
		}
		else {
			PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
			piece.setPosition(NewCoor);
			piece.moved();
			PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
		}
		
		UpdateGUI();
		return false;
	}
	
	public static Piece slot(Coordinate coor) {
		return PlayingBoard[coor.getY() - 1][coor.getX() - 1];
	}
	
	private void SetUpButtons() {
		Button but;
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				but = getButton(PlayingBoardPane[y][x]);
				but.setOnAction(e -> ButtonActionWanted(e));
			}
		}
		ImageView img;
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				img = getImageView(PlayingBoardPane[y][x]);
				img.setOnMouseClicked(e -> ButtonActionWanted(e));
			}
		}
	}
	
	private void ButtonActionWanted(ActionEvent e) {
		Button but = (Button)e.getTarget();
		Pane pane = (Pane)but.getParent();
		ButtonAction(pane);
	}
	
	private void ButtonActionWanted(MouseEvent e) {
		ImageView but = (ImageView)e.getTarget();
		Pane pane = (Pane)but.getParent();
		ButtonAction(pane);
	}
	
	private void ButtonAction(Pane pane) {
		Coordinate location = GetPaneCoordinate(pane);
		Piece piece = slot(location);
		
		if(PieceSelected != null && !justEliminated) {
			if(PieceSelected.isOnLine(location)) {
				if(MovePiece(PieceSelected, location))
					justEliminated = true;
				UpdateGUI();
				resetColor();
				if(!GameOver) {
					ChangeTurn();
					PieceSelected = null;
					SetMessage(turn + " Player Turn");
				}
			}
			else if(!PieceSelected.isOnLine(location))
				SetMessage("Invalid Move");
			else if(piece.color == turn) {
				PieceSelected = piece;
				resetColor();
				SetMessage(turn + " Player Turn");
				ShowMoves(piece.getAvaliablePositions());
			}
			else
				SetMessage("Not " + piece.color + " Player turn");
		}
		// if null and needs to be selected
		if(piece == null)
			return;
		else if(piece.color == turn && !justEliminated) {
			PieceSelected = piece;
			resetColor();
			SetMessage(turn + " Player Turn");
			ShowMoves(piece.getAvaliablePositions());
		}
		
		if(justEliminated)
			justEliminated = false;
	}
	
	
	private Coordinate GetPaneCoordinate(Pane pane) {
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(PlayingBoardPane[y][x].equals(pane))
					return new Coordinate(x + 1, y + 1);
			}
		}
		return null;
	}
	
	private Pane getPane(Button button) {return (Pane) button.getParent();}
	private Rectangle getColorRect(Pane pane) {return (Rectangle) pane.getChildren().get(0);}
	private Button getButton(Pane pane) {return (Button) pane.getChildren().get(1);}
	private ImageView getImageView(Pane pane) {return (ImageView) pane.getChildren().get(2);}
	
	private void UpdateConsoleDisplay() {
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				if(PlayingBoard[y][x] != null)
					PlayingBoardDisplay[y][x] = PlayingBoard[y][x].getInitial();
				else
					PlayingBoardDisplay[y][x] = "-";
			}
		}
	}
	
	public void UpdateGUI() {
		Pane pane;
		ImageView imgView;
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				pane = PlayingBoardPane[y][x];
				imgView = getImageView(pane);
				imgView.setImage((PlayingBoard[y][x] != null) ? PlayingBoard[y][x].getImage() : null);
			}
		}
	}
	
	public void resetColor() {
		// start first as white
		Pane pane = PlayingBoardPane[0][0];
		Rectangle colorRect = getColorRect(pane);
		colorRect.setFill(GREEN);
		boolean lastWhite = false;
		int i = 0;
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				pane = PlayingBoardPane[y][x];
				colorRect = getColorRect(pane);
				if(lastWhite) {
					if(i % 8 == 0) {
						colorRect.setFill(WHITE);
						lastWhite = true;
					}
					else {
						colorRect.setFill(GREEN);
						lastWhite = false;
					}
				}
				else {
					if(i % 8 == 0) {
						colorRect.setFill(GREEN);
						lastWhite = false;
					}
					else {
						colorRect.setFill(WHITE);
						lastWhite = true;
					}
				}
				i++;
			}
		}
	}
	
	private void ShowMoves(List<Coordinate> validCoordinates) {
		validCoordinates.forEach(coordinate -> {
			Rectangle rect = getColorRect(PlayingBoardPane[coordinate.getY() - 1][coordinate.getX() - 1]);
			rect.setFill(BLUE);
		});
	}
	
	public Rectangle getColorRectangleAt(Coordinate coor) {
		return (Rectangle)PlayingBoardPane[coor.getY() - 1][coor.getX() - 1].getChildren().get(0);
	}
	
	private void ChangeTurn() {
		turn = (turn == Players.WHITE) ? Players.BLACK : Players.WHITE;	
	}
	
	public void SetMessage(String string) {
		Messenger.setText(string);
	}
	
	public String toString() {
		String grid = "";
		for(int y = Piece.UpperLimit - 1; y >= 0; y--) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				grid += PlayingBoardDisplay[y][x] + " ";
			}
			grid += "\n";
		}
		return grid;
	}
}
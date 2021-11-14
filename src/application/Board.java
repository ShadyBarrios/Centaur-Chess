package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
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
	private Pane[] BlackElim;
	private Pane[] WhiteElim;
	public static Players turn;
	private Piece PieceSelected;
	private final Paint BLUE = Paint.valueOf("#49d6e9"), GREEN = Paint.valueOf("#75c367"), WHITE = Paint.valueOf("#ffffff");
	private List<Piece> EliminatedPieces;
	public TextField Messenger;
	public Text DisplayBlackTimer, DisplayWhiteTimer;
	private boolean justEliminated;
	private boolean GameOver;
	private GridPane grid;
	private static ChessTimer BlackTimer, WhiteTimer, CountdownTimer;
	private static Timer RealBlackTimer, RealWhiteTimer, RealCountdownTimer;
	
	public Board() {
		PlayingBoardDisplay = new String[8][8];
		PlayingBoard = new Piece[8][8];
		PlayingBoardPane = new Pane[8][8];
		BlackElim = new Pane[5];
		WhiteElim = new Pane[5];
		EliminatedPieces = new ArrayList<Piece>();
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				PlayingBoard[y][x] = null;
				PlayingBoardPane[y][x] = null;
				PlayingBoardDisplay[y][x] = "-";
			}
		}
		
		for(int y = 0; y < 5; y++) {
			BlackElim[y] = null;
			WhiteElim[y] = null;
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
	
	public Board(List<Piece> pieces, GridPane grid, TextField Messenger, GridPane blackElim, GridPane whiteElim, Text GUIB, Text GUIW, long TimeWanted) {
		this(pieces);
		for(int y = 1; y <= 8; y++) {
			for(int x = 1; x <= 8; x++) {
				PlayingBoardPane[y - 1][x - 1] = (Pane) grid.getChildren().get(new Coordinate(x, y).toPaneCoordinate());
			}
		}
		
		for(int y = 0; y < 5; y++) {
			BlackElim[y] = (Pane) blackElim.getChildren().get(y);
			WhiteElim[y] = (Pane) whiteElim.getChildren().get(y);
		}
		
		this.DisplayBlackTimer = GUIB;
		this.DisplayWhiteTimer = GUIW;
		
		UpdateBlackElim();
		UpdateWhiteElim();
		SetUpButtons();
		this.grid = grid;
		this.Messenger = Messenger;
		
		SetMessage(turn + " Player Turn");
		
		if(TimeWanted != -1) {
			CountdownTimer = new ChessTimer(7000, Players.WHITE, TimeWanted * 1000); // seconds to milliseconds
			RealCountdownTimer = new Timer(); 
			RealCountdownTimer.schedule(CountdownTimer, 0, 1000);

			grid.setDisable(true);
		}
	}
	
	public void addAll(List<Piece> pieces) {
		pieces.forEach(piece -> PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = piece);
		UpdateConsoleDisplay();
	}
	
	private void PlayerWon(Players winner) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		SetMessage("WINNER: " + winner);
		grid.setDisable(true);
		File audioFile = new File("C:\\Users\\scott\\Downloads\\WinSound.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		Clip clip = AudioSystem.getClip();
		clip.open(audioStream);
		
		if(RealBlackTimer != null) {
			RealBlackTimer.cancel();
			RealWhiteTimer.cancel(); // if one is null, both are null
		}
			
		
		clip.start();
		GameOver = true;
	}
	
	private Piece ConstructPawnExchange(Pawn pawn) {
		Object[] options = {"QUEEN", "BISHOP", "ROOK", "KNIGHT"};
		String choice = (String)JOptionPane.showInputDialog(null, "Congrats! Choose a replacement piece", 
				"Pawn Exchange",JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(choice == null)
			return null;
		else {
			switch(choice) {
			case "QUEEN":
				return new Queen(pawn.color, pawn.getPosition());
			case "ROOK":
				return new Rook(pawn.color, pawn.getPosition());
			case "BISHOP":
				return new Bishop(pawn.color, pawn.getPosition());
			case "KNIGHT":
				return new Knight(pawn.color, pawn.getPosition());
			default:
					return null;
			}
		}
	}
	
//	private List<Piece> EliminatedPiecesFrom(Players side){
//		List<Piece> elim = EliminatedPieces.stream().filter(piece -> (piece.color == side && piece.type != Pieces.PAWN)).toList();
//		return elim;
//	}
//	
//	private void RemoveFromEliminatedList(Piece piece) {
//		for(int i = 0; i < EliminatedPieces.size(); i++) {
//			if(EliminatedPieces.get(i).matches(piece)) {
//				EliminatedPieces.remove(i);
//				return;
//			}	
//		}
//	}
	
	private boolean PawnExchange(Pawn piece) {
		if(piece.color == Players.WHITE && piece.currentPosition.getY() != Piece.UpperLimit)
			return true;
		else if(piece.color == Players.BLACK && piece.currentPosition.getY() != Piece.LowerLimit)
			return true;
		
		Piece replacement = ConstructPawnExchange(piece);
		PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = (replacement == null) ? null : replacement;
		EliminatedPieces.add(piece);
		return true;
		
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
			
			if(EliminatedPieces.get(EliminatedPieces.size() - 1).type == Pieces.KING) {
				try {
					PlayerWon(turn);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
			else if(piece.type == Pieces.PAWN && !GameOver) {
				return PawnExchange((Pawn)piece);
			}
			else
				return true;
		}
		
		if(piece.isCastleCoordinate(NewCoor)) {
			Piece rook;
			// ? is right side castle, : is left side castle
			rook = (Rook)Board.slot(new Coordinate(NewCoor.getX() > piece.currentPosition.getX() ? 8 : 1, piece.getPosition().getY()));
			// set rook
			PlayingBoard[rook.getPosition().getY() - 1][rook.getPosition().getX() - 1] = null;
			rook.setPosition(new Coordinate((NewCoor.getX() > piece.currentPosition.getX()) ? NewCoor.getX() - 1 : NewCoor.getX() + 1, NewCoor.getY()));
			rook.moved();
			PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() > piece.currentPosition.getX() ? NewCoor.getX() - 2 : NewCoor.getX()] = rook;
			// set king
			PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
			piece.setPosition(NewCoor);
			piece.moved();
			PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
			
		}
		else if(piece.IsEnPassantCoordinate(NewCoor)) {
			EliminatedPieces.add(PlayingBoard[(piece.color == Players.WHITE) ? NewCoor.getY() - 2 : NewCoor.getY()][NewCoor.getX() - 1]);
			PlayingBoard[(piece.color == Players.WHITE) ? NewCoor.getY() - 2 : NewCoor.getY()][NewCoor.getX() - 1] = null;
			PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
			piece.setPosition(NewCoor);
			piece.moved();
			PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
		}
		else {
			PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = null;
			piece.setPosition(NewCoor);
			piece.moved();
			PlayingBoard[NewCoor.getY() - 1][NewCoor.getX() - 1] = piece;
			if(piece.type == Pieces.PAWN && !GameOver)
				PawnExchange((Pawn)piece);
		}
		return false;
	}
	
	public static Piece slot(Coordinate coor) {
		return (coor.getY() > Piece.UpperLimit || 
				coor.getX() > Piece.UpperLimit || 
				coor.getX() < Piece.LowerLimit ||
				coor.getY() < Piece.LowerLimit) ? null : PlayingBoard[coor.getY() - 1][coor.getX() - 1];
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
	
	private Rectangle getColorRect(Pane pane) {return (Rectangle) pane.getChildren().get(0);}
	private Button getButton(Pane pane) {return (Button) pane.getChildren().get(1);}
	private ImageView getImageView(Pane pane) {return (ImageView) pane.getChildren().get(2);}
	private ImageView getImageViewElim(Pane pane) {return (ImageView)pane.getChildren().get(0);}
	private Label getLabel(Pane pane) {return (Label)pane.getChildren().get(1);}
	
	private void UpdateConsoleDisplay() {
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) 
				PlayingBoardDisplay[y][x] = (PlayingBoard[y][x] != null) ? PlayingBoard[y][x].getInitial() : "-";
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
				if(PlayingBoard[y][x] != null)
					imgView.setLayoutX((PlayingBoard[y][x].thickness == Thickness.THICK) ? 15 : (PlayingBoard[y][x].type == Pieces.KING ? 20 : 18) );
			}
		}
		if(invertColor(turn) == Players.WHITE)
			UpdateWhiteElim();
		else
			UpdateBlackElim();
	}
	
	private void UpdateBlackElim() {
		Pane pane;
		ImageView imgView;
		Label label;
		Image image;
		int killCount;
		
		for(int y = 0; y < 5; y++) {
			pane = BlackElim[y];
			imgView = getImageViewElim(pane);
			label = getLabel(pane);

			image = getImage(y, Players.BLACK);
			killCount = getKillCount(getType(y), Players.BLACK);
			label.setText("" + killCount);
			imgView.setImage(image);
		}
	}
	
	private Image getImage(int y, Players type) {
		if(y == 0) return (new Pawn(type).getImage());
		else if(y == 1) return (new Queen(type).getImage());
		else if(y == 2) return (new Knight(type).getImage());
		else if(y == 3) return (new Bishop(type).getImage());
		else return (new Rook(type).getImage());
	}
	
	private Pieces getType(int y) {
		if(y == 0) return Pieces.PAWN;
		else if(y == 1) return Pieces.QUEEN;
		else if(y == 2) return Pieces.KNIGHT;
		else if(y == 3) return Pieces.BISHOP;
		else return Pieces.ROOK;
	}
	
	private int getKillCount(Pieces type, Players color) {
		return (int)EliminatedPieces.stream().filter(piece -> (piece.color == color && piece.type == type)).count();
	}

	private void UpdateWhiteElim() {
		Pane pane;
		ImageView imgView;
		Label label;
		Image image;
		int killCount;
		
		for(int y = 0; y < 5; y++) {
			pane = WhiteElim[y];
			imgView = getImageViewElim(pane);
			label = getLabel(pane);

			image = getImage(y, Players.WHITE);
			killCount = getKillCount(getType(y), Players.WHITE);
			label.setText("" + killCount);
			imgView.setImage(image);
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
					colorRect.setFill( (i % 8 == 0) ? WHITE : GREEN);
					lastWhite = (i % 8 == 0) ? true : false;
				}
				else {
					colorRect.setFill( (i % 8 == 0) ? GREEN : WHITE);
					lastWhite = (i % 8 == 0) ? false : true;
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
	
	private Players invertColor(Players turn) {
		return (turn == Players.WHITE) ? Players.BLACK : Players.WHITE; 
	}
	
	private void ChangeTurn() {
		turn = (turn == Players.WHITE) ? Players.BLACK : Players.WHITE;	
	}
	
	public void SetMessage(String string) {
		Messenger.setText(string);
	}
	
	class ChessTimer extends TimerTask{
		private long remainingTime;
		private Players player;
		private long allottedTime;
		private long TimeWanted;
		private int times;
		
		public ChessTimer(long allottedTime, Players player) {
			this.allottedTime = allottedTime;
			this.remainingTime = allottedTime;
			this.player = player;
			times = 0;
		}
		
		public ChessTimer(long allottedTime, Players player, long TimeWanted) {
			this.allottedTime = allottedTime;
			this.remainingTime = allottedTime;
			this.player = player;
			this.TimeWanted = TimeWanted;
			times = 0;
		}
		
		@Override
		public void run() {
			if(allottedTime == 7000) {
				long remainingTime = getRemainingTime();
				if(remainingTime <= 0 && times == 0) {
					// 1000 -> 1 sec 
					// 60 * 1 sec -> 1 min -> 60,000
					// 10 * 1 min -> 10 mins -> 600,000
					++times;
					
					BlackTimer = new ChessTimer(TimeWanted, Players.BLACK);
					RealBlackTimer = new Timer();
					RealBlackTimer.schedule(BlackTimer, 0, 1000);
					
					WhiteTimer = new ChessTimer(TimeWanted, Players.WHITE);
					RealWhiteTimer = new Timer();
					RealWhiteTimer.schedule(WhiteTimer, 0, 1000);
					
					SetMessage(turn + " Player Turn");
					
					grid.setDisable(false);
					
					RealCountdownTimer.cancel();
				}
				String message = "" + remainingTime;
				if(allottedTime == 7000 && times == 0) {
					SetMessage("GAME STARTS IN " + message);
					if(DisplayBlackTimer != null)
						DisplayBlackTimer.setText(toMinute(toSecond(this.TimeWanted)));
					if(DisplayWhiteTimer != null)
						DisplayWhiteTimer.setText(toMinute(toSecond(this.TimeWanted)));
				}
				return;
			}
			
			if(player == turn) {
				String message = toMinute(getRemainingTime());
				try {
					if(player == Players.BLACK) {
						DisplayBlackTimer.setText("");
						DisplayBlackTimer.setText(message);
					}
					else if(player == Players.WHITE){
						DisplayWhiteTimer.setText("");
						DisplayWhiteTimer.setText(message);
					}
				}
				catch(NullPointerException e) {
					getRemainingTime();
					return;
				}
				
			}
				
			if(remainingTime <= 0) {
				try {
					PlayerWon(turn);
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				}
			}
		}
		
		private long toSecond(long milli) {
			return milli / 1000;
		}
		
		private String toMinute(long timeInSec) {
			long minutes = timeInSec / 60;
			return Format(minutes) + " : " + Format(timeInSec - (minutes * 60));
		}
		
		private long getRemainingTime() {
			this.remainingTime -= 1000;
			return this.remainingTime / 1000;
		}
		
		private String Format(long time) {
			String Time = "" + time;
			return ((Time.length() == 1) ? "0" + Time : Time);
		}
		
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
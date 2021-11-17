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

/**
 * Technically the driver that handles the game itself.
 * @author Shady
 *
 */
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
	public Text DisplayBlackTimer, DisplayWhiteTimer, DisplayWhiteElims, DisplayBlackElims;
	private boolean justEliminated;
	private boolean GameOver;
	private GridPane grid;
	private static ChessTimer BlackTimer, WhiteTimer, CountdownTimer;
	private static Timer RealBlackTimer, RealWhiteTimer, RealCountdownTimer;
	
	/**
	 * Constructs a null version of the visual and literal Playing Boards,
	 *  as well as a null version of the Eliminated Pieces side displays.
	 */
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
	
	/**
	 * Uses the 0-arg constructor and {@link #addAll(List)}
	 * @param pieces - A full set of pieces.
	 */
	public Board(List<Piece> pieces) {
		this();
		addAll(pieces);	
	}
	
	/**
	 * Populates the previously created gui components and starts the speed chess timers if requested.
	 * @param pieces - A full set of pieces
	 * @param grid - The GUI chess board
	 * @param Messenger - The TextField used to display the current turn and errors.
	 * @param blackElim - The GUI list of eliminated black pieces.
	 * @param whiteElim - The GUI list of eliminated white pieces.
	 * @param GUIB - The Text used to display the remaining time for the black player or "Chess"
	 * @param GUIW - The Text used to display the remaining time for the white player or "Centaur".
	 * @param TimeWanted - The time requested (speed chess) by the player.
	 * @param WhiteElims - Text javafx property displaying points of eliminated white pieces
	 * @param BlackElims - Text javafx property displaying points of eliminated black pieces
	 */
	public Board(List<Piece> pieces, GridPane grid, TextField Messenger, GridPane blackElim, GridPane whiteElim, Text GUIB, Text GUIW, long TimeWanted, Text WhiteElims, Text BlackElims) {
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
		this.DisplayWhiteElims = WhiteElims;
		this.DisplayBlackElims = BlackElims;
		
		UpdateBlackElim();
		UpdateWhiteElim();
		SetUpButtons();
		this.grid = grid;
		this.Messenger = Messenger;
		
		SetMessage(turn + " Player Turn");
		
		if(TimeWanted != -1) {
			CountdownTimer = new ChessTimer(7000, TimeWanted * 1000); // seconds to milliseconds
			RealCountdownTimer = new Timer(); 
			RealCountdownTimer.schedule(CountdownTimer, 0, 1000);

			grid.setDisable(true);
		}
	}
	
	/**
	 * Adds all the freshly created pieces to the literal playing board 
	 * @param pieces - the pieces created by the Piece createPieces static method
	 */
	public void addAll(List<Piece> pieces) {
		pieces.forEach(piece -> PlayingBoard[piece.getPosition().getY() - 1][piece.getPosition().getX() - 1] = piece);
		UpdateConsoleDisplay();
	}
	
	/**
	 * Plays the CR winner music, cancels all the timers if set, and disables the panel
	 * @param winner - The winner of the game
	 * @throws LineUnavailableException
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
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
	
	/**
	 * Constructs a JOptionPane that allows the player to choose a piece to replace the pawn with.
	 * @param pawn - The pawn that will be replaced
	 * @return The replacement piece with the same coordinates as the pawn
	 */
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
	
	/**
	 * Uses the {@link #ConstructPawnExchange(pawn)} to create a replacement piece. The replacement piece is put on the literal board.
	 * The pawn is added to the {@link #EliminatedPieces}.
	 * @param piece
	 * @return True regardless. However, it skips the process if the pawn hasn't reached the appropriate row.
	 */
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
	
	/**
	 * There are 5 outcomes when a piece is trying to be moved.
	 * 1) The tile selected is not an available option.
	 * 2) The tile selected is vacant and no swapping needs to take place
	 * 3) The move intended is a castle move
	 * 4) The move intended is an en passant move
	 * 5) No special move is requested but the tile isn't vacant, requiring a swap.
	 * @param piece - The piece that will be moved
	 * @param NewCoor - The coordinate the piece will be moved to.
	 * @return True if the coordinate is an available coordinate. False if the coordinate isn't.
	 */
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
	
	/**
	 * Can be used to peek into the literal board.
	 * @param coor - The coordinate that the piece will be extracted from
	 * @return The piece on located at coor on the literal board. Returns null if the coordinate is out of bounds or if the piece doesn't exist.
	 */
	public static Piece slot(Coordinate coor) {
		return (coor.getY() > Piece.UpperLimit || 
				coor.getX() > Piece.UpperLimit || 
				coor.getX() < Piece.LowerLimit ||
				coor.getY() < Piece.LowerLimit) ? null : PlayingBoard[coor.getY() - 1][coor.getX() - 1];
	}
	
	/**
	 * Adds button listeners to the image of the pieces and the buttons in every panel
	 */
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
	
	/**
	 * Whenever a button is clicked, {@link #ButtonAction(Pane)} is triggered
	 * @param e - the ActionEvent
	 */
	private void ButtonActionWanted(ActionEvent e) {
		Button but = (Button)e.getTarget();
		Pane pane = (Pane)but.getParent();
		ButtonAction(pane);
	}
	
	/**
	 * Whenever an image is clicked, {@link #ButtonAction(Pane)} is triggered
	 * @param e - the MouseEvent 
	 */
	private void ButtonActionWanted(MouseEvent e) {
		ImageView but = (ImageView)e.getTarget();
		Pane pane = (Pane)but.getParent();
		ButtonAction(pane);
	}
	
	/**
	 * Decides what to do after a tile has been clicked. 
	 * If a piece has already been picked, 
	 * it'll be decided if the tile selected is a valid move or if a new piece is being selected.
	 * Otherwise, itll decide whether or not the piece selected is of the right player (during appropriate turn).
	 * If the user clicks and empty tile when no piece has been previously selected, nothing will happen.
	 * @param pane - The tile that was clicked
	 */
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
	
	/**
	 * Get the coordinate of a tile on the visual
	 * @param pane - The tile on the visual grid
	 * @return The coordinate of the tile
	 */
	private Coordinate GetPaneCoordinate(Pane pane) {
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(PlayingBoardPane[y][x].equals(pane))
					return new Coordinate(x + 1, y + 1);
			}
		}
		return null;
	}
	
	/**
	 * Rectangles (tiles) have an index of 0 in a board pane's child list
	 * @param pane - The pane selected
	 * @return The Rectangle (tile) contained within the pane
	 */
	private Rectangle getColorRect(Pane pane) {return (Rectangle) pane.getChildren().get(0);}
	
	/**
	 * Buttons have an index of 1 in a board pane's child list
	 * @param pane - The pane selected
	 * @return The button contained within the pane
	 */
	private Button getButton(Pane pane) {return (Button) pane.getChildren().get(1);}
	
	/**
	 * ImageViews have an index of 2 in a board pane's child list
	 * @param pane - The pane selected
	 * @return The ImageView contained within the pane
	 */
	private ImageView getImageView(Pane pane) {return (ImageView) pane.getChildren().get(2);}
	
	/**
	 * ImageViews have an index of 0 in an Eliminated Piece pane's child list
	 * @param pane - The pane in the eliminated list that will be updated
	 * @return The ImageView contained within the pane.
	 */
	private ImageView getImageViewElim(Pane pane) {return (ImageView)pane.getChildren().get(0);}
	/**
	 * Labels have an index of 1 in an Eliminated Piece pane's child list
	 * @param pane - The pane in the eliminated list that will be updated.
	 * @return The label contained within the pane.
	 */
	private Label getLabel(Pane pane) {return (Label)pane.getChildren().get(1);}
	
	/**
	 * Updates the console version of the literal board.
	 */
	private void UpdateConsoleDisplay() {
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) 
				PlayingBoardDisplay[y][x] = (PlayingBoard[y][x] != null) ? PlayingBoard[y][x].getInitial() : "-";
		}
	}
	
	/**
	 * Update the visual board.
	 */
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
	
	/**
	 * Updates the visual list of the eliminated black pieces
	 */
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
		

		List<Piece> eliminatedBlacks = EliminatedPieces.stream().filter(piece -> piece.color == Players.BLACK).toList();
		DisplayBlackElims.setText("0");
		if(!eliminatedBlacks.isEmpty())
			eliminatedBlacks.forEach(piece -> DisplayBlackElims.setText(String.valueOf(Integer.parseInt(DisplayBlackElims.getText()) + piece.value)));
	}
	
	/**
	 * Will create a throw-away instance of a piece to acquire the piece's image.
	 * Each type of piece has an arbitrary ID (King has no ID):
	 * Pawn ID = 0 ; 
	 * Queen ID = 1; 
	 * Knight ID = 2;
	 * Bishop ID = 3;
	 * Rook ID = 4 (and up);
	 * @param ID - The Arbitrary number indicating each type of piece
	 * @param color - The color of the piece
	 * @return
	 */
	private Image getImage(int ID, Players color) {
		if(ID == 0) return (new Pawn(color).getImage());
		else if(ID == 1) return (new Queen(color).getImage());
		else if(ID == 2) return (new Knight(color).getImage());
		else if(ID == 3) return (new Bishop(color).getImage());
		else return (new Rook(color).getImage());
	}
	
	/**
	 * Gets the type of piece based on the arbitrary ID passed.
	 * @param y
	 * @return
	 */
	private Pieces getType(int y) {
		if(y == 0) return Pieces.PAWN;
		else if(y == 1) return Pieces.QUEEN;
		else if(y == 2) return Pieces.KNIGHT;
		else if(y == 3) return Pieces.BISHOP;
		else return Pieces.ROOK;
	}
	
	/**
	 * Accesses the {@link #EliminatedPieces} list to determine how many of a type (& color) of piece has been eliminated.
	 * @param type - The type of piece
	 * @param color - The color of the piece
	 * @return The number of eliminated pieces.
	 */
	private int getKillCount(Pieces type, Players color) {
		return (int)EliminatedPieces.stream().filter(piece -> (piece.color == color && piece.type == type)).count();
	}

	/**
	 * Updates the visual Eliminated List of the white pieces.
	 */
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
		
		List<Piece> eliminatedWhites = EliminatedPieces.stream().filter(piece -> piece.color == Players.WHITE).toList();
		DisplayWhiteElims.setText("0");
		if(!eliminatedWhites.isEmpty())
			eliminatedWhites.forEach(piece -> DisplayWhiteElims.setText(String.valueOf(Integer.parseInt(DisplayWhiteElims.getText()) + piece.value)));
	}

	/**
	 * Resets the tiles' colors to their original colors.
	 */
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
	
	/**
	 * Visually shows the available move of a piece by highlighting the available tiles.
	 * @param validCoordinates - A list of the coordinates the piece can be moved to.
	 */
	private void ShowMoves(List<Coordinate> validCoordinates) {
		validCoordinates.forEach(coordinate -> {
			Rectangle rect = getColorRect(PlayingBoardPane[coordinate.getY() - 1][coordinate.getX() - 1]);
			rect.setFill(BLUE);
		});
	}
	
	/**
	 * Gets the rectangle within a pane at a given coordinate
	 * @param coor - The coordinate of the pane / rectangle.
	 * @return The rectangle at the coordinate.
	 */
	public Rectangle getColorRectangleAt(Coordinate coor) {
		return (Rectangle)PlayingBoardPane[coor.getY() - 1][coor.getX() - 1].getChildren().get(0);
	}
	
	/**
	 * Equivalent of a ! for Players.
	 * @param color - The color being negated
	 * @return The opposite color of the argument
	 */
	private Players invertColor(Players color) {
		return (color == Players.WHITE) ? Players.BLACK : Players.WHITE; 
	}
	
	/**
	 * Updates the turn (flips it)
	 */
	private void ChangeTurn() {
		turn = (turn == Players.WHITE) ? Players.BLACK : Players.WHITE;	
	}
	
	/**
	 * Changes the text displayed by the Messenger TextField
	 * @param string - The message
	 */
	public void SetMessage(String string) {
		Messenger.setText(string);
	}
	
	/**
	 * An extension of the TimerTask class.
	 * Will act as the speed chess timers and the count-down timer.
	 * @author Shady
	 *
	 */
	class ChessTimer extends TimerTask{
		private long remainingTime;
		private Players player;
		private long allottedTime;
		private long TimeWanted;
		private int times;
		
		/**
		 * Constructor used for the main timers
		 * @param allottedTime - Time given
		 * @param player - The player the timer is associated with.
		 */
		public ChessTimer(long allottedTime, Players player) {
			this.allottedTime = allottedTime;
			this.remainingTime = allottedTime;
			this.player = player;
			times = 0;
		}
		
		/**
		 * Constructor used in the creation of the count-down timer
		 * @param allottedTime - Time before game starts
		 * @param TimeWanted - Time wanted for the regular speed chess timers.
		 */
		public ChessTimer(long allottedTime, long TimeWanted) {
			this.allottedTime = allottedTime;
			this.remainingTime = allottedTime;
			this.TimeWanted = TimeWanted;
			times = 0;
		}
		
		@Override
		public void run() {
			// ONLY starting timer has 7 seconds
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
		
		/**
		 * Converts milliseconds to seconds
		 * @param milli - The amount of milliseconds.
		 * @return X seconds worth of the passed milliseconds.
		 */
		private long toSecond(long milli) {
			return milli / 1000;
		}
		
		/**
		 * Formats the number of seconds into MM : SS format
		 * @param timeInSec - Number of seconds.
		 * @return A string with the formatted time
		 */
		private String toMinute(long timeInSec) {
			long minutes = timeInSec / 60;
			return Format(minutes) + " : " + Format(timeInSec - (minutes * 60));
		}
		
		/**
		 * Subtracts 1000 from the remainingTime instance variable (represents 1 second)
		 * @return The time after the subtraction in seconds.
		 */
		private long getRemainingTime() {
			this.remainingTime -= 1000;
			return this.remainingTime / 1000;
		}
		
		/**
		 * Formats an individual amount of time to have 2 digits.
		 * @param time - The time being formatted
		 * @return The time is XX format
		 */
		private String Format(long time) {
			String Time = "" + time;
			return ((Time.length() == 1) ? "0" + Time : Time);
		}
		
	}
	
	/**
	 * String version of the literal board.
	 */
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
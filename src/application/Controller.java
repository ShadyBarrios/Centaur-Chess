package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Class defined as controller for Javafx application. Hands all control to Board.java
 * @author Shady
 */

public class Controller {
	@FXML
	private GridPane ChessBoard, BlackElim, WhiteElim;
	@FXML
	private TextField Messenger;
	@FXML
	private Text DisplayBlackTimer, DisplayWhiteTimer;
	
	public void initialize() {
		 Board MasterBoard = new Board(Piece.CreatePieces(), ChessBoard, Messenger, BlackElim, WhiteElim, DisplayBlackTimer, DisplayWhiteTimer, Main.TimeWanted);
		 MasterBoard.resetColor();
		 MasterBoard.UpdateGUI();
		 
	}
}

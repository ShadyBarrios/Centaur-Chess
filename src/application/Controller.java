package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {
	@FXML
	private GridPane ChessBoard;
	@FXML
	private TextField Messenger;
	
	public void initialize() {
		 Board MasterBoard = new Board(Piece.CreatePieces(), ChessBoard, Messenger);
		 MasterBoard.resetColor();
		 MasterBoard.UpdateGUI();
	}
}

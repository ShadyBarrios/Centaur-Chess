package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {
	@FXML
	private GridPane ChessBoard, BlackElim, WhiteElim;
	@FXML
	private TextField Messenger;
	
	public void initialize() {
		 Board MasterBoard = new Board(Piece.CreatePieces(), ChessBoard, Messenger, BlackElim, WhiteElim);
		 MasterBoard.resetColor();
		 MasterBoard.UpdateGUI();
		 
	}
}

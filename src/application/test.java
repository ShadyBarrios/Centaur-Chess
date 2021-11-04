package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.Enums.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;


public class test{
	public static void main(String[] args) {
		Queen Queen1 = new Queen(Players.BLACK, 4, 8);
		Queen Queen2 = new Queen(Players.WHITE, 5, 1);
		
		List<Queen> queens = Arrays.asList(Queen1, Queen2);
		
		Bishop Bishop1 = new Bishop(Players.BLACK, 3, 1);
		Bishop Bishop2 = new Bishop(Players.BLACK, 6, 1);
		Bishop Bishop3 = new Bishop(Players.WHITE, 3, 8);
		Bishop Bishop4 = new Bishop(Players.WHITE, 6, 8);
		
		List<Bishop> bishops = Arrays.asList(Bishop1, Bishop2, Bishop3, Bishop4);
		
		Rook Rook1 = new Rook(Players.BLACK, 1, 1);
		Rook Rook2 = new Rook(Players.BLACK, 8, 1);
		Rook Rook3 = new Rook(Players.WHITE, 1, 8);
		Rook Rook4 = new Rook(Players.WHITE, 8, 8);
		
		List<Rook> rooks = Arrays.asList(Rook1, Rook2, Rook3, Rook4);
		
		Knight Knight1 = new Knight(Players.BLACK, 2, 1);
		Knight Knight2 = new Knight(Players.BLACK, 7, 1);
		Knight Knight3 = new Knight(Players.WHITE, 2, 8);
		Knight Knight4 = new Knight(Players.WHITE, 7, 8);
		
		List<Knight> knights = Arrays.asList(Knight1, Knight2, Knight3, Knight4);
		
		King King1 = new King(Players.BLACK, 5, 8);
		King King2 = new King(Players.WHITE, 4, 1);
		
		List<King> kings = Arrays.asList(King1, King2);
		
		List<Pawn> pawns = Pawn.createPawns();
		
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.addAll(kings);
		pieces.addAll(queens);
		pieces.addAll(pawns);
		pieces.addAll(knights);
		pieces.addAll(rooks);
		pieces.addAll(bishops);
		
		String[][] grid = new String[8][8];
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				grid[y][x] = "-";
			}
		}
		
		pieces.forEach(piece -> grid[piece.currentPosition.getY() - 1][piece.currentPosition.getX() - 1] = piece.getInitial());
		for(int y = Piece.UpperLimit - 1; y >= 0; y--) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				System.out.print(grid[y][x] + " ");
			}
			System.out.println();
		}
		
		pieces.forEach(piece -> PrintGrid(piece.getAvaliablePositions(), piece));
	}
	
	public static void PrintGrid(List<Coordinate> coordinates, Piece p) {
		String[][] grid = new String[8][8];
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				grid[y][x] = "-";
			}
		}
		
		System.out.println(p);
		
		coordinates.forEach(coor -> grid[coor.getY() - 1][coor.getX() - 1] = coor.matches(p.currentPosition) ? p.getInitial() : "M");
		
		for(int y = Piece.UpperLimit - 1; y >= 0; y--) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				System.out.print(grid[y][x] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
package application;

import java.util.List;

import application.Enums.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;


public class test{
	public static void main(String[] args) {
		Queen q = new Queen(Players.BLACK,4, 2);
		System.out.println(q);
		PrintGrid(q.getAvaliablePositions(), q);
		
		Bishop b = new Bishop(Players.BLACK,  8, 7);
		System.out.println(b);
		PrintGrid(b.getAvaliablePositions(), b);
		
		Rook r = new Rook(Players.BLACK, 6,4);
		System.out.println(r);
		PrintGrid(r.getAvaliablePositions(), r);
		
		Knight k = new Knight(Players.BLACK, 3, 3);
		System.out.println(k);
		PrintGrid(k.getAvaliablePositions(), k);
	}
	
	public static void PrintGrid(List<Coordinate> coordinates, Piece p) {
		String[][] grid = new String[8][8];
		
		for(int y = 0; y < Piece.UpperLimit; y++) {
			for(int x = 0; x < Piece.UpperLimit; x++) {
				grid[y][x] = "-";
			}
		}
		
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
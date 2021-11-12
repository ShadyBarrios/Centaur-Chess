package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import application.Enums.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;


public class test{
	private static Board board;
	public static void main(String[] args) {
		List<Piece> Pieces = Piece.CreatePieces();
		Scanner cin = new Scanner(System.in);
		board = new Board(Pieces);
		System.out.println(board);
		System.out.print("Command: ");
		String input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);	
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		System.out.print("Command: ");
		input = cin.nextLine();
		CommandHandler(input);
		System.out.println(board);
		
		
		
		cin.close();
	}
	
	
	private static void CommandHandler(String cmd) {
		Scanner cin = new Scanner(cmd);
		int x = cin.nextInt();
		int y = cin.nextInt();
		cin.next();
		int x2 = cin.nextInt();
		int y2 = cin.nextInt();
		Piece piece = Board.slot(new Coordinate(x, y));
		if(piece == null) {
			System.out.println("Empty slot");
			cin.close();
			return;
		}
		board.MovePiece(piece, new Coordinate(x2, y2));
		cin.close();
		return;
	}
}
package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javafx.scene.image.ImageView;

public class Pawn extends Piece{
	
	public Pawn(Players type, int x, int y) {
		super(type, x, y);
		this.piece = Pieces.PAWN;
	}
	
	public Pawn(Players type, Coordinate coor) {
		super(type, coor);
		this.piece = Pieces.PAWN;
	}
	
	@Override
	public boolean isOnLine(Coordinate coor) {
		boolean result;
		
		if(currentPosition.matches(coor))
			result = true;
		else if(currentPosition.matchesX(coor) && coor.getY() == currentPosition.getY() + 1) 
			result = true;
		else 
			result = false;
		
		return result;
	}
	
	@Override
	public String getInitial() {return "P";}

	@Override
	public List<Coordinate> getAvaliablePositions() {
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		
		coordinates.add(currentPosition);
		coordinates.addAll(getVertical());
		
		return coordinates;
	}
	
	private List<Coordinate> getVertical(){
		List<Coordinate> coordinates = new ArrayList<Coordinate>();
		int currentX = currentPosition.getX();
		int currentY = currentPosition.getY();
		
		if(currentY < UpperLimit)
			coordinates.add(new Coordinate(currentX, currentY + 1));
		if(!hasMoved && currentY < UpperLimit - 1)
			coordinates.add(new Coordinate(currentX, currentY + 2));
		
			// work in progress
		return coordinates;
	}
	
	public static List<Pawn> createPawns(){
		Pawn[] pawns = new Pawn[16];
		
		for(int i = 0; i < pawns.length / 2; i++)
			pawns[i] = new Pawn(Players.BLACK, i + 1, 7);
		
		for(int i = pawns.length / 2; i < pawns.length; i++)
			pawns[i] = new Pawn(Players.WHITE, i - 8 + 1, 2);
		
		return Arrays.asList(pawns);
	}
	
}
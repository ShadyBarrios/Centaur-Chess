package application;

import java.util.ArrayList;
import java.util.List;

public class Coordinate{
	private int x,y;
	
	public Coordinate() {x = 0; y = 0;}
	public Coordinate(int x, int y) {this.x = x; this.y = y;}
	
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setNew(int x, int y) {this.x = x; this.y = y;}
	
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	
	public int xDifference(Coordinate coor) {return Math.abs(this.x - coor.x);}
	public int yDifference(Coordinate coor) {return Math.abs(this.y - coor.y);}
	
	public boolean matchesX(Coordinate coor) {return this.x == coor.x;}
	public boolean matchesY(Coordinate coor) {return this.y == coor.y;}
	public boolean matches(Coordinate coor) {return (this.x == coor.x && this.y == coor.y);}
	
	public String toString() {return "( " + x + " , " + y + " )";}
}
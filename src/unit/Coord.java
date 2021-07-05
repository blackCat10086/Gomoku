package unit;

import java.io.Serializable;
/**
 * ×ø±êÀà
 * @author huang
 *
 */
public class Coord implements Serializable{
	private static final long serialVersionUID = 1l;
	private int x;
	private int y;
	private ChessPieces pieces;
	
	public Coord(int x, int y, ChessPieces pieces) {
		super();
		this.x = x;
		this.y = y;
		this.pieces = pieces;
	}
	public ChessPieces getPieces() {
		return pieces;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}

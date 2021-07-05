package unit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
/**
 * 真实的棋子类
 * @author huang
 *
 */
public class Pieces implements ChessPieces,Serializable{
	private static final long serialVersionUID = 1l;
	private String color;
	
	public Pieces(String color) {
		super();
		this.color = color;
	}

	@Override
	public void DownPieces(Graphics g, Point pt) {
		if(color.equals("黑子")) {
			g.setColor(Color.BLACK);
			g.fillOval(pt.x, pt.y, Constant.PIECES_SIZE, Constant.PIECES_SIZE);
		}else if(color.equals("白子")) {
			g.setColor(Color.WHITE);
			g.fillOval(pt.x, pt.y, Constant.PIECES_SIZE, Constant.PIECES_SIZE);
		}
	}

	@Override
	public String getColor() {
		return color;
	}

}

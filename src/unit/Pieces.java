package unit;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
/**
 * ��ʵ��������
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
		if(color.equals("����")) {
			g.setColor(Color.BLACK);
			g.fillOval(pt.x, pt.y, Constant.PIECES_SIZE, Constant.PIECES_SIZE);
		}else if(color.equals("����")) {
			g.setColor(Color.WHITE);
			g.fillOval(pt.x, pt.y, Constant.PIECES_SIZE, Constant.PIECES_SIZE);
		}
	}

	@Override
	public String getColor() {
		return color;
	}

}

package unit;

import java.awt.Graphics;
import java.awt.Point;
/**
 * 棋子的接口
 * @author huang
 *
 */
public interface ChessPieces {
	public void DownPieces(Graphics g,Point pt);
	public String getColor();
}

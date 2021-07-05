package unit;

import java.util.HashMap;
import java.util.Map;
/**
 * 棋子的工厂，使用了享元模式，使得大批量的棋子对象大幅度减少
 * @author huang
 *
 */
public class PiecesFactory {
	private static Map<String,ChessPieces> piecesMap = new HashMap<String,ChessPieces>();
	
	public static ChessPieces getPieces(String color) {
		if(piecesMap.containsKey(color)) {
			return piecesMap.get(color);
		}
		ChessPieces pieces = new Pieces(color);
		piecesMap.put(color, pieces);
		return pieces;
	}
}

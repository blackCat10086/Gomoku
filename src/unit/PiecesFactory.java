package unit;

import java.util.HashMap;
import java.util.Map;
/**
 * ���ӵĹ�����ʹ������Ԫģʽ��ʹ�ô����������Ӷ������ȼ���
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

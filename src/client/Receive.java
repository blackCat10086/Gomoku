package client;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import unit.Constant;
import unit.Coord;
/**
 * �ͻ��˽����࣬���շ���˷�����������Ϣ�����������ϻ�����
 * @author huang
 *
 */
public class Receive implements Runnable{
	private ObjectInputStream bis;
	private boolean isRunning = true;
	Chessboard chessboard;
	Send send;
	public Receive(Socket client,Chessboard chessboard,Send send) {
		super();
		try {
			this.send = send;
			this.chessboard = chessboard;
			this.bis = new ObjectInputStream(client.getInputStream());
		} catch (Exception e) {
			isRunning = false;
			try {
				if(bis!=null) {
					bis.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				isRunning = false;
			}
		}
	}
	/**
	 * ������Ϣ
	 */
	private void receive() {
		Coord coord = null;
		try {
			Object object = bis.readObject();
			/**
			 * �ж��Ƿ��������Ӯ���
			 */
			if(object.equals(true)) {
				GameOver gameOver = new GameOver();
				gameOver.gameOver("You Win");
				chessboard.setVisible(false);
				isRunning = false;
			}else if(object.equals(false)){
				GameOver gameOver = new GameOver();
				gameOver.gameOver("You Lose");
				chessboard.setVisible(false);
				isRunning = false;
				send.setFlag(true);
			}else {
				coord = (Coord)object;
				if(coord!=null) {
					chessboard.setAllPieces(coord);
					coord.getPieces().DownPieces(chessboard.getGraphics(), new Point(coord.getX()-Constant.PIECES_SIZE/2,coord.getY()-Constant.PIECES_SIZE/2));
					chessboard.setFlag(true);  //�Է������꣬�ֵ��ҷ�����
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isRunning = false;
		}
	}
	
	@Override
	public void run() {
		while(isRunning) {
			receive();
		}
	}

	public void stop() {
		this.isRunning = false;
	}
}

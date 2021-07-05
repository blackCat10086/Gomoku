package client;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import unit.Constant;
import unit.Coord;
/**
 * 客户端接收类，接收服务端发来的坐标信息，并在棋盘上画出来
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
	 * 接收消息
	 */
	private void receive() {
		Coord coord = null;
		try {
			Object object = bis.readObject();
			/**
			 * 判断是否结束，输赢如何
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
					chessboard.setFlag(true);  //对方已下完，轮到我方下了
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

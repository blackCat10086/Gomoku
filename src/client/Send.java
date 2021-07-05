package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import unit.Coord;
/**
 * 客户端发送类，接收棋盘传来的坐标信息，并发送给服务端
 * @author huang
 *
 */
public class Send implements Runnable{
	private ObjectOutputStream bos;
	private boolean isRunning = true;
	private volatile Coord coord = null;
	private boolean flag = false;   //判断游戏是否结束，如果为true证明游戏结束，我方输了
	public Send(Socket client) {
		super();
		try {
			this.bos = new ObjectOutputStream(client.getOutputStream());
		} catch (Exception e) {
			try {
				if(bos!=null) {
					bos.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				isRunning = false;
			}
		}
	}
	
	public void send() {
		try {
			if(coord!=null) {
				bos.writeObject(coord);
				bos.flush();
				coord = null;
			}
			/**
			 * 告诉对方，你赢了
			 */
			if(flag) {
				bos.writeObject(flag);
				bos.flush();
				flag = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			isRunning = false;
		}
	}

	@Override
	public void run() {
		while(isRunning) {
			send();
		}
	}
	
	public void setCoord(Coord coord) {
		this.coord = coord;
	}
	
	public void stop() {
		this.isRunning = false;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}

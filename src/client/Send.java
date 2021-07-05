package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import unit.Coord;
/**
 * �ͻ��˷����࣬�������̴�����������Ϣ�������͸������
 * @author huang
 *
 */
public class Send implements Runnable{
	private ObjectOutputStream bos;
	private boolean isRunning = true;
	private volatile Coord coord = null;
	private boolean flag = false;   //�ж���Ϸ�Ƿ���������Ϊtrue֤����Ϸ�������ҷ�����
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
			 * ���߶Է�����Ӯ��
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

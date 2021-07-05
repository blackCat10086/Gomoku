package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * �������п��ƣ�������
 * @author huang
 *
 */
public class GameFacade {
	public static void gameStart() {
		Scanner s = null;
		try {
			s = new Scanner(System.in);
			System.out.print("�����������ַ��");
			String ip = s.nextLine();
			Socket client = new Socket(ip,6666);
			//ƥ��ҳ��
			Waiting wait = new Waiting();
			new Thread(wait).start();
			//��ȡ����˴�������ɫ���Ⱥ��ֵ���Ϣ
			String temp = receive(client,wait);
			String[] color = temp.split(" ");
			/**
			 * �������գ������̣߳���������
			 */
			Send send = new Send(client);
			new Thread(send).start();
			Chessboard c = new Chessboard(color[0],color[1]);
			new Thread(new Receive(client,c,send)).start();
			c.launchFrame(send);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(s != null) {
				s.close();
			}
		}
	}
	/**
	 * ��ȡ����˵���Ϣ���ж��Ƿ�ʼ��Ϸ
	 * @param client
	 * @param wait
	 * @return
	 * @throws IOException
	 */
	private static String receive(Socket client,Waiting wait) throws IOException {
		DataInputStream dis = new DataInputStream(client.getInputStream());
		String temp = "";
		while((temp=dis.readUTF())!=null) {
			if(temp.equals("true")) {
				if((temp=dis.readUTF())!=null) {
					wait.setFlag(false);
					break;
				}
			}
		}
		return temp;
	}
	
}

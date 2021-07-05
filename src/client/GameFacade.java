package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * 用来进行控制，启动等
 * @author huang
 *
 */
public class GameFacade {
	public static void gameStart() {
		Scanner s = null;
		try {
			s = new Scanner(System.in);
			System.out.print("输入服务器地址：");
			String ip = s.nextLine();
			Socket client = new Socket(ip,6666);
			//匹配页面
			Waiting wait = new Waiting();
			new Thread(wait).start();
			//获取服务端传来的颜色，先后手等信息
			String temp = receive(client,wait);
			String[] color = temp.split(" ");
			/**
			 * 启动接收，发送线程，启动棋盘
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
	 * 获取服务端的消息，判断是否开始游戏
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

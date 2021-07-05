package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unit.Coord;

public class Server {
	//下棋的总组数
	private static Map<String,List<Socket>> group = new HashMap<String,List<Socket>>();
	//所有的棋子，分组
	private static Map<String,List<Coord>> allPieces = new HashMap<String,List<Coord>>();
	//匹配的人数
	private static List<Socket> groupList = new ArrayList<Socket>();
	//棋子的颜色
	private static String[] piecesColor = {"黑子","白子"};
	//有几组
	private static int groupNum = 0;
	//总共有多少已连接
	private static int connectionNum = 0;
	
	public static void main(String[] args) {
		new Server().start();
	}
	
	private void start() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(6666);
			System.out.println("服务器已启动！！！");
			while(true) {
				Socket client = server.accept();
				connectionNum++;  			//每连接一个客户端加一
				groupList.add(client);		//等待数也加一
				group.put(groupNum+"", groupList); //增加一个组
				if(connectionNum%2==0) {
					//给两个客户端分别发送消息
					DataOutputStream dos1 = new DataOutputStream(client.getOutputStream());
					DataOutputStream dos2 = new DataOutputStream(groupList.get(0).getOutputStream());
					dos1.writeUTF("true");    //开始游戏
					dos1.writeUTF(piecesColor[0]+" true");   //true 先手
					dos2.writeUTF("true");   //开始游戏
					dos2.writeUTF(piecesColor[1]+" false");   //false 禁止下棋
					allPieces.put(groupNum+"", new ArrayList<Coord>());
					//启动两个线程，分别监听一个客户端，并把客户端的内容转发到另一个客户端上
					new Thread(new Transfer(groupList.get(0),groupList,allPieces,group,groupNum,1)).start();
					new Thread(new Transfer(groupList.get(1),groupList,allPieces,group,groupNum,0)).start();
					//清除正在匹配的人数
					groupList.clear();
					//组数加一
					groupNum++;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(server!=null) {
					server.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

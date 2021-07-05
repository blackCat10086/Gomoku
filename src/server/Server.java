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
	//�����������
	private static Map<String,List<Socket>> group = new HashMap<String,List<Socket>>();
	//���е����ӣ�����
	private static Map<String,List<Coord>> allPieces = new HashMap<String,List<Coord>>();
	//ƥ�������
	private static List<Socket> groupList = new ArrayList<Socket>();
	//���ӵ���ɫ
	private static String[] piecesColor = {"����","����"};
	//�м���
	private static int groupNum = 0;
	//�ܹ��ж���������
	private static int connectionNum = 0;
	
	public static void main(String[] args) {
		new Server().start();
	}
	
	private void start() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(6666);
			System.out.println("������������������");
			while(true) {
				Socket client = server.accept();
				connectionNum++;  			//ÿ����һ���ͻ��˼�һ
				groupList.add(client);		//�ȴ���Ҳ��һ
				group.put(groupNum+"", groupList); //����һ����
				if(connectionNum%2==0) {
					//�������ͻ��˷ֱ�����Ϣ
					DataOutputStream dos1 = new DataOutputStream(client.getOutputStream());
					DataOutputStream dos2 = new DataOutputStream(groupList.get(0).getOutputStream());
					dos1.writeUTF("true");    //��ʼ��Ϸ
					dos1.writeUTF(piecesColor[0]+" true");   //true ����
					dos2.writeUTF("true");   //��ʼ��Ϸ
					dos2.writeUTF(piecesColor[1]+" false");   //false ��ֹ����
					allPieces.put(groupNum+"", new ArrayList<Coord>());
					//���������̣߳��ֱ����һ���ͻ��ˣ����ѿͻ��˵�����ת������һ���ͻ�����
					new Thread(new Transfer(groupList.get(0),groupList,allPieces,group,groupNum,1)).start();
					new Thread(new Transfer(groupList.get(1),groupList,allPieces,group,groupNum,0)).start();
					//�������ƥ�������
					groupList.clear();
					//������һ
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

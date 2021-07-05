package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unit.Constant;
import unit.Coord;

/**
 * 进行坐标的转发和存储
 * @author huang
 *
 */
public class Transfer implements Runnable{
	//下棋的总组数
	private Map<String,List<Socket>> group = new HashMap<String,List<Socket>>();
	//所有的棋子，分组
	private Map<String,List<Coord>> allPieces = new HashMap<String,List<Coord>>();
	//该局中所有的黑棋
	private Map<Integer,List<Integer>> bPiecesCoord = new HashMap<Integer,List<Integer>>();
	//该局中所有的白棋
	private Map<Integer,List<Integer>> wPiecesCoord = new HashMap<Integer,List<Integer>>();
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private int groupNum;  //组数
	private boolean isRunning = true;
	
	public Transfer(Socket client,List<Socket> list,Map<String,List<Coord>> allPieces,Map<String,List<Socket>> group,int groupNum,int order) {
		super();
		try {
			this.group = group;
			this.allPieces = allPieces;
			this.groupNum =groupNum;
			this.ois = new ObjectInputStream(client.getInputStream());
			this.oos = new ObjectOutputStream(list.get(order).getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			isRunning = false;
		}
	}
	private Coord receive() {
		Coord coord = null;
		try {
			coord = (Coord)ois.readObject();
		} catch (Exception e) {
			this.isRunning = false;
			this.group.remove(groupNum+"");
			try {
				oos.writeObject(true);   //我方已退出游戏，对方获胜
			} catch (IOException e1) {
				isRunning = false;
			}
		}
		return coord;
	}
	/**
	 * 发送以及存储
	 * @return
	 */
	private Coord send() {
		Coord coord = receive();
		if(coord != null) {
			try {
				//增加棋子到所有棋子的组中
				if(!allPieces.containsKey(groupNum+"")) {
					allPieces.put(groupNum+"", new ArrayList<Coord>());
				}
				allPieces.get(groupNum+"").add(coord);
				//分别添加棋子到各种的组中
				if(coord.getPieces().getColor().equals("黑子")) {
					if(!bPiecesCoord.containsKey(coord.getX())) {
						bPiecesCoord.put(coord.getX(), new ArrayList<Integer>());
					}
					bPiecesCoord.get(coord.getX()).add(coord.getY());
				}else if(coord.getPieces().getColor().equals("白子")) {
					if(!wPiecesCoord.containsKey(coord.getX())) {
						wPiecesCoord.put(coord.getX(), new ArrayList<Integer>());
					}
					wPiecesCoord.get(coord.getX()).add(coord.getY());
				}
				oos.writeObject(coord);
			} catch (IOException e) {
				e.printStackTrace();
				isRunning = false;
			}
		}
		return coord;
	}
	/**
	 * 判断是否有五子相连
	 * @param c
	 */
	private void judge(Coord c) {
		try {
			Map<Integer,List<Integer>> temp = null;
			int count = 0;      //计数，当为五时，游戏结束
			if(c.getPieces().getColor().equals("黑子")) {
				temp = bPiecesCoord;
			}else if(c.getPieces().getColor().equals("白子")) {
				temp = wPiecesCoord;
			}
			List<Integer> tempList = temp.get(c.getX());
			//判断竖直方向
			for(int j = Constant.DISTANCE_Y;j <= Constant.CB_WH+Constant.DISTANCE_Y;j+=Constant.GRID_WH) {
				int tempCount = count;
				for(int i = 0;i < tempList.size();i++) {
					if(tempList.get(i).equals(j)) {
						count++;
					}
				}
				if(count >= 5) {
					result();
				}
				if(tempCount == count) {
					count = 0;
				}
			}
			count = 0;
			//判断水平方向
			for(int j = Constant.DISTANCE_X;j <= Constant.CB_WH+Constant.DISTANCE_X;j+=Constant.GRID_WH) {
				int tempCount = count;
				if(temp.containsKey(j)) {
					tempList = temp.get(j);
					for(int i = 0;i < tempList.size();i++) {
						if(tempList.get(i).equals(c.getY())) {
							count++;
						}
					}
				}
				if(count >= 5) {
					result();
				}
				if(tempCount == count) {
					count = 0;
				}
			}
			//判断从左上到右下方向
			count = 0;
			int distance = (c.getY()- Constant.DISTANCE_Y)/Constant.GRID_WH;
			int tempX = c.getX()-(Constant.GRID_WH*distance);
			int tempY = c.getY()-(Constant.GRID_WH*distance);
			for(int j = tempX;j <= Constant.CB_WH+Constant.DISTANCE_X;j+=Constant.GRID_WH) {
				int tempCount = count;
				if(temp.containsKey(j)) {
					tempList = temp.get(j);
					for(int i = 0;i < tempList.size();i++) {
						if(tempList.get(i).equals(tempY)) {
							count++;
						}
					}
				}
				tempY += Constant.GRID_WH;
				if(count >= 5) {
					result();
				}
				if(tempCount == count) {
					count = 0;
				}
			}
			//判断从右上到左下方向
			count = 0;
			tempX = c.getX()+(Constant.GRID_WH*distance);
			tempY = c.getY()-(Constant.GRID_WH*distance);
			for(int j = tempX;j >= Constant.DISTANCE_X;j-=Constant.GRID_WH) {
				int tempCount = count;
				if(temp.containsKey(j)) {
					tempList = temp.get(j);
					for(int i = 0;i < tempList.size();i++) {
						if(tempList.get(i).equals(tempY)) {
							count++;
						}
					}
				}
				tempY += Constant.GRID_WH;
				if(count >= 5) {
					result();
				}
				if(tempCount == count) {
					count = 0;
				}
			}
		} catch (Exception e) {
			isRunning = false;
		}
	}
	//结果
	private void result() {
		try {
			//告诉对方已经输了
			oos.writeObject(false);
			this.isRunning = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	@Override
	public void run() {
		while(isRunning) {
			judge(send());
		}
	}
	public void stop() {
		this.isRunning = false;
	}
}

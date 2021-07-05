package client;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import unit.ChessPieces;
import unit.Constant;
import unit.Coord;
import unit.PiecesFactory;

public class Chessboard extends Frame{
	private static final long serialVersionUID = 1l;
	private ChessPieces pieces = null;		//棋子
	private Map<Integer,List<Integer>> allPieces = new HashMap<Integer,List<Integer>>();
	private boolean flag;   //判断是否该我下棋
	
	public Chessboard(String color,String flag) throws HeadlessException {
		super();
		this.pieces = PiecesFactory.getPieces(color);
		if(flag.equals("true")) {
			this.flag = true;
		}else {
			this.flag = false;
		}
	}
	/**
	 * 游戏界面，添加了对鼠标的监控，通过鼠标的左键点击，在棋盘上画出来
	 * @param send
	 */
	public void launchFrame(Send send) {
		setSize(Constant.CB_WH+Constant.DISTANCE_X*2,Constant.CB_WH+Constant.DISTANCE_Y*2);
		setLocation(480, 180);
		setTitle("五子棋1.0");
		setResizable(false);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1) {
					if(!flag) {
						return;
					}
					if(judge(e.getX(),e.getY())) {
						int x = e.getX();
						int y = e.getY();
						//调整位置
						if(!((x-Constant.DISTANCE_X)%Constant.GRID_WH==0&&(y-Constant.DISTANCE_Y)%Constant.GRID_WH==0)) {
							x -= (x-Constant.DISTANCE_X)%Constant.GRID_WH;
							y -= (y-Constant.DISTANCE_X)%Constant.GRID_WH;
						}
						//判断该位置是否已经有棋子
						if(allPieces.containsKey(x)) {
							for(int i = 0;i < allPieces.get(x).size();i++) {
								if(allPieces.get(x).get(i).equals(y)) {
									return;
								}
							}
						}
						if(!allPieces.containsKey(x)) {
							allPieces.put(x, new ArrayList<Integer>());
						}
						allPieces.get(x).add(y);
						Coord coord = new Coord(x,y,pieces);
						send.setCoord(coord);
						pieces.DownPieces(getGraphics(), new Point(x-Constant.PIECES_SIZE/2,y-Constant.PIECES_SIZE/2));
						flag = false;   //停止下棋，轮到对方
					}
				}
			}
		});
		setVisible(true);
	}
	//判断是否按到交点上，允许有POSIT_ADJ常量规定的偏差
	private boolean judge(int x,int y) {
		boolean flag = false;
		if(x<=Constant.CB_WH+Constant.DISTANCE_X && x>=Constant.DISTANCE_X && y<=Constant.CB_WH+Constant.DISTANCE_Y && y>=Constant.DISTANCE_Y) {
			if((x-Constant.DISTANCE_X)%Constant.GRID_WH<Constant.POSIT_ADJ && (y-Constant.DISTANCE_Y)%Constant.GRID_WH<Constant.POSIT_ADJ) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 把棋盘画出来
	 */
	public void paint(Graphics g) {
		BufferedImage bi = null;
	        try {
	        	URL u = Chessboard.class.getClassLoader().getResource("img/plank.jpg");
	            bi = ImageIO.read(u);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		g.drawImage(bi, 0, 0, null);
		g.setColor(Color.BLACK);
		for(int i = 0;i < Constant.RANK;i++) {
			g.drawLine(Constant.DISTANCE_X, Constant.DISTANCE_Y+(i*Constant.GRID_WH), Constant.DISTANCE_X+Constant.CB_WH, Constant.DISTANCE_Y+(i*Constant.GRID_WH));
			g.drawLine(Constant.DISTANCE_X+(i*Constant.GRID_WH), Constant.DISTANCE_Y, Constant.DISTANCE_X+(i*Constant.GRID_WH), Constant.DISTANCE_Y+Constant.CB_WH);
		}
	}
	/**
	 * 获取对方下的棋子位置并保存
	 * @param coord
	 */
	public void setAllPieces(Coord coord) {
		if(!allPieces.containsKey(coord.getX())) {
			allPieces.put(coord.getX(), new ArrayList<Integer>());
		}
		allPieces.get(coord.getX()).add(coord.getY());
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}

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
	private ChessPieces pieces = null;		//����
	private Map<Integer,List<Integer>> allPieces = new HashMap<Integer,List<Integer>>();
	private boolean flag;   //�ж��Ƿ��������
	
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
	 * ��Ϸ���棬����˶����ļ�أ�ͨ���������������������ϻ�����
	 * @param send
	 */
	public void launchFrame(Send send) {
		setSize(Constant.CB_WH+Constant.DISTANCE_X*2,Constant.CB_WH+Constant.DISTANCE_Y*2);
		setLocation(480, 180);
		setTitle("������1.0");
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
						//����λ��
						if(!((x-Constant.DISTANCE_X)%Constant.GRID_WH==0&&(y-Constant.DISTANCE_Y)%Constant.GRID_WH==0)) {
							x -= (x-Constant.DISTANCE_X)%Constant.GRID_WH;
							y -= (y-Constant.DISTANCE_X)%Constant.GRID_WH;
						}
						//�жϸ�λ���Ƿ��Ѿ�������
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
						flag = false;   //ֹͣ���壬�ֵ��Է�
					}
				}
			}
		});
		setVisible(true);
	}
	//�ж��Ƿ񰴵������ϣ�������POSIT_ADJ�����涨��ƫ��
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
	 * �����̻�����
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
	 * ��ȡ�Է��µ�����λ�ò�����
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

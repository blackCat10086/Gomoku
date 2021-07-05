package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
/**
 * �ȴ�ƥ��״̬
 * @author huang
 *
 */
public class Waiting extends Frame implements Runnable{
	private static final long serialVersionUID = 1l;
	private boolean flag = true;
	
	public void waiting() {
		setSize(450, 320);
		setLocation(530, 240);
		setVisible(true);
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font("����",Font.BOLD,40));
		int count = 1;
		while(flag) {
			try {
				g.clearRect(0, 0, 400, 300);
				g.drawString("����ƥ�䣺", 80, 160);
				g.drawString(new String(count+++"��"), 280, 160);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setVisible(false);
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public void run() {
		waiting();
	}
	
}

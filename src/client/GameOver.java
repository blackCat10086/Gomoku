package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
/**
 * 游戏结束时的页面
 * @author huang
 *
 */
public class GameOver extends Frame{
	private static final long serialVersionUID = 1l;
	
	public void gameOver(String result) {
		setSize(450, 320);
		setLocation(530, 240);
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font("宋体",Font.BOLD,40));
		g.drawString(result, 130, 160);
		BufferedImage bi = null;
        try {
        	String path = null;
        	if(result.equals("You Win")) {
        		path = "img/win.jpg";
        	}else {
        		path = "img/lose.jpg";
        	}
        	URL u = Chessboard.class.getClassLoader().getResource(path);
            bi = ImageIO.read(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(bi, 0, 0, null);
	}
}

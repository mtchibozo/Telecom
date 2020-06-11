package server;


import java.awt.Dimension;
import javax.swing.JFrame;


public class StopServer extends Thread {
	
	private ServerTeech st;
	JFrame win;
	public StopServer(ServerTeech st) {
		super();
		this.st = st;
		
	}
	
	public void run() {
		win = new JFrame();
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setMinimumSize(new Dimension(250, 200));
		win.add(new myButton(win, st));
		win.pack();
		win.setVisible(true);
	}
	
	public void close() {
		win.dispose();
	}
}

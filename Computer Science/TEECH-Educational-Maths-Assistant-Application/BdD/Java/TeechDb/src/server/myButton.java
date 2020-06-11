package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;






public class myButton extends JButton implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean stop = true;
	private ServerTeech st;
	private JFrame win;
	
	public myButton(JFrame win, ServerTeech st) {
		super("STOP SERVEUR");
		this.st = st;
		this.win = win;
		setMaximumSize(new Dimension(100, 50));
		setBorder( BorderFactory.createRaisedBevelBorder());
		setBackground(Color.YELLOW);
		
		addActionListener(this);
		
		
	}
	
	public boolean getBoolean() {
		return (this.stop);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		setBorder(BorderFactory.createLoweredBevelBorder());
		
		try { 
			st.getServerSocket().close();
			win.dispose();
			} 
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
}

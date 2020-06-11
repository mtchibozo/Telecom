import java.awt.BorderLayout;

import javax.swing.*;

public class ClientFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientFrame(byte[] image) {
		
		super("Image client");
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add(new JLabel( new ImageIcon(image)));
		add(panelImage);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
		pack() ;            
		setVisible(true) ; 
	}
}

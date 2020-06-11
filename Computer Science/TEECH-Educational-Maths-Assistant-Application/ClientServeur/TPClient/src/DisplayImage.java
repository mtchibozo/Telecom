import java.awt.BorderLayout;
import javax.swing.*;

public class DisplayImage extends JFrame {
	
	
	private static final long serialVersionUID = 1L;


	public DisplayImage(byte[] image) {
		
		super("Image returned");
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add(new JLabel( new ImageIcon(image)));
		add(panelImage);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		pack() ;            
		setVisible(true) ; 
		setAlwaysOnTop(true);
	}

}
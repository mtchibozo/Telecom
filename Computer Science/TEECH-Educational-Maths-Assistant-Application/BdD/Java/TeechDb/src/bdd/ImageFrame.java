package bdd;
import java.awt.BorderLayout;


import javax.swing.*;

public class ImageFrame extends JFrame {
	
	
	private static final long serialVersionUID = 1L;


	public ImageFrame(String image) {
		
		super("Image returned");
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add(new JLabel( new ImageIcon(image)));
		add(panelImage);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
		pack() ;            
		setVisible(true) ; 
	}
	
	public ImageFrame(byte[] image) {
		
		super("Image returned");
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add(new JLabel( new ImageIcon(image)));
		add(panelImage);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		pack() ;            
		setVisible(true) ; 
	}
	
	

	public ImageFrame(String description, String image) {
		
		super("Image returned");
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add(new JLabel(new ImageIcon(image)), BorderLayout.NORTH);
		panelImage.add(new JLabel(description), BorderLayout.SOUTH);
		add(panelImage);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		pack() ;            
		setVisible(true) ;
		
	}
	
	public ImageFrame(String description, byte[] image) {
		
		super("Image returned");
		JPanel panelImage = new JPanel(new BorderLayout());
		panelImage.add(new JLabel(new ImageIcon(image)), BorderLayout.NORTH);
		panelImage.add(new JLabel(description), BorderLayout.SOUTH);
		add(panelImage);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		pack() ;            
		setVisible(true) ;
		
	}

}

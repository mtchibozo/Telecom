import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Frame  extends JFrame{
	
	private JTextArea text; 
	private JButton bouton1; 
	private JButton bouton2; 
	private JButton bouton3; 
	
	private static final long serialVersionUID = 1L; 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Frame window= new Frame(); 
	}
	 
	private Client client=null; 
	
	
	public void launchClient() {
	    String host = Client.DEFAULT_HOST;
	    int port = Client.DEFAULT_PORT;
	    

	    try {
	      client = new Client(host, port);
	    }
	    catch (Exception e) {
	      System.err.println("Client: Couldn't connect to "+host+":"+port);
	      System.exit(1);
	    }
	    
	    System.out.println("Client connected to "+host+":"+port);

	  }

	public Frame () {
		
		
		text= new JTextArea(5,10); 
		JScrollPane scroll = new JScrollPane(text);
		
		bouton1= new JButton("Search Multimedia"); 
		bouton1.addActionListener(new Button1Listener("Search Multimedia"));
		bouton2= new JButton("Play Multimedia"); 
		bouton2.addActionListener(new Button2Listener("Play Multimedia"));
		bouton3= new JButton("Exit"); 
		bouton3.addActionListener(new Button3Listener("Exit"));
		
		JMenuBar menuBar= new JMenuBar(); 
		JMenu menuFile= new JMenu("File"); 
		
		JMenuItem menuBouton1= new JMenuItem("Search Multimedia"); 
		menuBouton1.addActionListener(new Button1Listener("Search Multimedia"));
		
		
		JMenuItem menuBouton2= new JMenuItem("Play Multimedia"); 
		menuBouton2.addActionListener(new Button2Listener("Play Multimedia"));
		
		
		JMenuItem menuBouton3= new JMenuItem("Exit"); 
		menuBouton3.addActionListener(new Button3Listener("Exit"));
		 
		
		menuFile.insert(menuBouton1,1); 
		menuFile.insert(menuBouton2,2); 
		menuFile.addSeparator();
		menuFile.insert(menuBouton3,3); 
		
		menuBar.add(menuFile); 
		
		this.setJMenuBar(menuBar); 
		
		JToolBar toolBar= new JToolBar(); 
		toolBar.add(new Button1Listener("Search Multimedia")); 
		toolBar.add(new Button2Listener("Play Multimedia")); 
		toolBar.add(new Button3Listener("Exit")); 

		
		add(bouton1, BorderLayout.EAST); 
		add(bouton2, BorderLayout.WEST);
		add(bouton3, BorderLayout.SOUTH);
		add(scroll, BorderLayout.CENTER);
		add(toolBar,BorderLayout.NORTH);
		
		this.setPreferredSize(new Dimension(500,500)); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setTitle("TPINF224"); 
		pack(); 
		setVisible(true);
		launchClient(); 
	}
	
	class Button1Listener extends AbstractAction {
		Button1Listener(String name) {
			super(name); 
		}
		
		public void actionPerformed(ActionEvent e) {
			text.insert("Searching file... \n", 0);
			Request("search"); 
			
			
		}

	
	}
	class Button2Listener extends AbstractAction {
		Button2Listener(String name) {
			super(name); 
		}
		public void actionPerformed(ActionEvent e) {
			text.insert("Playing file... \n", 0);
			Request("play"); 
		}

		
	}
	class Button3Listener extends AbstractAction {
		Button3Listener(String name) {
			super(name); 
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}

		
	}
	public void Request(String request) {
		String nameFile = JOptionPane.showInputDialog(this,"What is the name of the file?", null);
		
		String reponse=client.send(request+' '+nameFile); 
		
		text.insert(reponse+"\n", 0);
	}
	

}

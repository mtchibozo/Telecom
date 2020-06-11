package tableEntities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import server.TeechProtocol;

public class Corrige extends DatabaseEntry {
	
	

	private int idExercice; //identifiant de l'exercie que le corrig� traite.
	private String indication; //une cha�ne de caract�res qui donne des pistes de r�solution pour l'exercice.
	private String description;
	private int recommandation; //un entier compris entre 0 et 10 (par exemple) qui �value la pertinence de l'exercice.
	private String enonce; //une cha�ne de caract�res qui contient le format sous lequel est stocke le corrige : image ou latex
	private int idAuteur;
	private String type;

	public Corrige(int id) throws SQLException {
		
		super("CORRIGES", id);
		idExercice = data.getInt(2);
		indication = data.getString(3);
		description = data.getString(4);
		recommandation = data.getInt(5);
		enonce = data.getString(6);
		idAuteur = data.getInt(7);
		type = data.getString(8);
		
	}
	
	public Corrige(ResultSet data) throws SQLException {
		
		super("CORRIGES", data);
		idExercice = data.getInt(2);
		indication = data.getString(3);
		description = data.getString(4);
		recommandation = data.getInt(5);
		enonce = data.getString(6);
		idAuteur = data.getInt(7);
		type = data.getString(8);
		
	}
	
	
	public String convertToMsg() throws IOException, SQLException {
		
		
		String sep = TeechProtocol.sep;
		String content;
		switch(type) {
		case "image" : 
			byte[] img = TeechProtocol.extractBytes(enonce);
			content = TeechProtocol.encodeImage(img);
			break;
		case "latex" : 
			content = TeechProtocol.extractText(enonce);
		default :
			throw new SQLException("Unknown type of file : " +type +", corrige id : " + id);
		}
		String msg= id+sep+getIdExercice()+sep+getIndication()+sep+getDescription()
		+sep+getRecommandation()+sep+content+sep+getIdAuteur()+sep+getType();
		return msg;
	}
	
	
	private Corrige(
			int idExercice,
			String description, 
			String indication,
			int recommendation,
			String enonce,
			int idAuteur,
			String type) throws SQLException {
		
		super("CORRIGES");
		this.idExercice = idExercice;
		this.recommandation = recommendation;
		this.indication = indication;
		this.enonce = enonce;
		this.idAuteur =idAuteur;
		this.type = type;
		
	}
		
	public static Corrige add(
			int idExercice,
			String description, 
			String indication,
			int recommendation,
			String enonce, // contenu exercice photo encodee ou string latex
			int idAuteur,
			String type) throws SQLException, IOException {
		
		Corrige corr = new Corrige(idExercice, description, indication, recommendation, enonce, idAuteur, type) ;
		
		int id = corr.newId();
		corr.setId(id);
		String query =  "INSERT INTO CORRIGES (id, idExercice, description, indication, recommendation, enonce, idAuteur, type) VALUES(?,?,?,?,?,?,?,?)"; // oui il y a une faute a recommandation
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setInt(2,idExercice);
		pstmt.setString(3, description);
		pstmt.setString(4, indication);
		pstmt.setInt(5, recommendation);
		File outputfile =null;
		
		System.out.println(type+"1");
		switch(type){
			
			case "image" :
				System.out.println(type+"2");
				byte[] img = TeechProtocol.decodeImage(enonce);
				BufferedImage bi = ImageIO.read(new ByteArrayInputStream(img));
				outputfile = new File("data/corriges/corriges_"+id+".png");
				ImageIO.write(bi,"png", outputfile);
				break;
				
			case "latex":		
				outputfile = new File("data/corrige/corr_"+id+".tex");
				PrintWriter pw = new PrintWriter(outputfile);
				pw.print(enonce);	
				System.out.print(enonce);
				pw.close();
				break;
		}
		
		pstmt.setString(6, outputfile.getPath());
		pstmt.setInt(7, idAuteur);
		pstmt.setString(8, type);
		pstmt.executeUpdate();
		corr.setEnonce(outputfile.getPath());
		return corr;
		
	}




	public int getIdExercice() {
		return idExercice;
	}


	public String getIndication() {
		return indication;
	}


	public String getDescription() {
		return description;
	}


	public int getRecommandation() {
		return recommandation;
	}

	
	/** Renvoie suivant le type "image" ou "latex" : un string contenant le texte du fichier latex ou l'image encodee en string
	 * @return String
	 * @throws IOException SQLException*/
	public String getEnonce() {
		return enonce;
		
	}



	public int getIdAuteur() {
		return idAuteur;
	}


	public String getType() {
		return type;
	}


	public void setEnonce(String enonce) {
		this.enonce = enonce;
	}

	
}

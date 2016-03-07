

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import auswertung.DBQuery;
import auswertung.ExcelWriter;
import auswertung.Mitarbeiter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Settings {

	private LocalDate startDate;
	private LocalDate endDate;
	private static String path;
	private Boolean email;
	private Date[] holidays;
	private Mitarbeiter[] mitarbeiter;
	
	private String startDayFormated;
	private String endDayFormated;
	

	public Settings(){
	    
	}
	
	public Settings(LocalDate startDate, LocalDate endDate, String path, Boolean email){
		this.startDate = startDate;
		this.endDate = endDate;
		this.path = path+'\\';
		this.email = email;
		this.startDayFormated =  startDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));
		this.endDayFormated = endDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));		
	}
	
	public void startApp() throws Exception{	    
	    DBQuery datenbank = new DBQuery(startDayFormated, endDayFormated, path);  		
	    Mitarbeiter[] mitarbeiterDB = datenbank.createMitarbeiterDB();    //Erstelt einen Array aus Mitarbeiter Objekten aller Aktiven Mitarbeiter 	    
    	    ExcelWriter excelWriter = new ExcelWriter(startDayFormated, endDayFormated, path, mitarbeiterDB);
    	    excelWriter.createExcel();
    	    excelWriter.addHeader();
    	    excelWriter.fillExcel();
    	    excelWriter.fillSum();	
    	    excelWriter.addImage();
	}
	
	public void sendMail(){
	    try{
		Parent root = FXMLLoader.load(getClass().getResource("Mail_GUI.fxml"));
		Stage stage = new Stage();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	    }catch(Exception e) {
		e.printStackTrace();
	    }
	}
		
	public static String getPath() {
	    return path;
	}
}

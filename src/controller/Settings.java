package controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.Locale;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Mitarbeiter;
import view.GuiController;

public class Settings {

    	private static String password = "ABC123";	// Passwort 
	private LocalDate startDate;	// Gewähltes Anfangsdatum
	private LocalDate endDate;	// Gewähltes Enddatum
	private static String path;	//gewählter Pfad
	private Boolean email;	//Boolean ob Emailversendet werden soll oder nicht
	
	
	public static String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    Settings.password = password;
	}

	private static String startDayFormated;
	private static String endDayFormated;
	
	public Settings(LocalDate startDate, LocalDate endDate, String path, Boolean email){
	    this.startDate = startDate;
	    this.endDate = endDate;
	    Settings.path = path+'\\';
	    this.email = email;
	    Settings.startDayFormated =  startDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));
	    Settings.endDayFormated = endDate.format(DateTimeFormatter.ofPattern("d.MM.yyyy"));	    
	}
	
	public void startApp() throws Exception{
	    DBQuery datenbank = new DBQuery(startDayFormated, endDayFormated); 
	    Mitarbeiter[] mitarbeiter = datenbank.createMitarbeiterDB();    
    	    ExcelWriter excelWriter = new ExcelWriter(startDayFormated, endDayFormated, path, mitarbeiter);
    	    excelWriter.createExcel();
    	    excelWriter.addHeader();
    	    excelWriter.fillExcel();
    	    excelWriter.fillSum();	
    	    excelWriter.addImage();
	}
			
	public static String getPath() {
	    return path;
	}
}

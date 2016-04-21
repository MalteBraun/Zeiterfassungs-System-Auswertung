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

    	private static String password = "ABC123";
	private LocalDate startDate;
	private LocalDate endDate;
	private static String path;
	private Boolean email;
	GuiController gui;
	
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
	    gui = new GuiController();
	}
	
	public void startApp() throws Exception{
	    GuiController.setInfoText("Sammele Daten im gewählten Zeitraum");
	    DBQuery datenbank = new DBQuery(startDayFormated, endDayFormated, path);
	    GuiController.setProgressbar(0.1);
	    Mitarbeiter[] mitarbeiterDB = DBQuery.createMitarbeiterDB();    //Erstelt einen Array aus Mitarbeiter Objekten aller Aktiven Mitarbeiter
	    GuiController.setProgressbar(0.2);
    	    ExcelWriter excelWriter = new ExcelWriter(startDayFormated, endDayFormated, path, mitarbeiterDB);
    	    GuiController.setProgressbar(0.1);
    	    excelWriter.createExcel();
    	    GuiController.setProgressbar(0.1);
    	    excelWriter.addHeader();
    	    GuiController.setProgressbar(0.1);
    	    excelWriter.fillExcel();
    	    GuiController.setProgressbar(0.1);
    	    excelWriter.fillSum();	
    	    GuiController.setProgressbar(0.15);
    	    excelWriter.addImage();
    	    GuiController.setProgressbar(0.1);
    	    GuiController.setInfoText("dfgadfgdrfa");
    	    System.out.println("Fertig");
	}
			
	public static String getPath() {
	    return path;
	}
}

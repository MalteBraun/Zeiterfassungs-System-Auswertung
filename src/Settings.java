

import java.time.LocalDate;
import java.util.Date;

import auswertung.DBQuery;
import auswertung.ExcelWriter;
import auswertung.Mitarbeiter;

public class Settings {

	private LocalDate startDate;
	private LocalDate endDate;
	private String path;
	private Boolean email;
	private Date[] holidays;
	private Mitarbeiter[] mitarbeiter;
		
	
	public Settings(LocalDate startDate, LocalDate endDate, String path, Boolean email){
		this.startDate = startDate;
		this.endDate = endDate;
		this.path = path;
		this.email = email;
		try{
			this.holidays = auswertung.DBQuery.getHolidays();
			this.mitarbeiter = auswertung.DBQuery.createMitarbeiterDB();
		}catch(Exception e){
			
		}
	}
	
	public void startApp() throws Exception{
		auswertung.DBQuery.connectToDB();
		Mitarbeiter[] mitarbeiterDB;
		
    	mitarbeiterDB = DBQuery.createMitarbeiterDB();    //Erstelt einen Array aus Mitarbeiter Objekten aller Aktiven Mitarbeiter
     	
    	for( Mitarbeiter x : mitarbeiterDB){
    		x.print();
    	}
    	
    	ExcelWriter excelWriter = new ExcelWriter(startDate.toString(), endDate.toString(), path, mitarbeiterDB);
    	
    	System.out.println("Start");
    	excelWriter.addHeader();
    	excelWriter.addImage();
    	excelWriter.fillExcel();
    	excelWriter.fillSum();
	}
	
	public static void sendMail(String path){
		
		
	}
	
	
}

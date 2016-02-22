package auswertung;

import java.util.Date;

public class Settings {

	private Date startDate;
	private Date endDate;
	private String path;
	private Boolean email;
	private Date[] holidays;
	private Mitarbeiter[] mitarbeiter;
		
	
	public Settings(Date startDate, Date endDate, String path, Boolean email){
		this.startDate = startDate;
		this.endDate = endDate;
		this.path = path;
		this.email = email;
		try{
			this.holidays = DBQuery.getHolidays();
			this.mitarbeiter = DBQuery.createMitarbeiterDB();
		}catch(Exception e){
			
		}
	}
	
	public static void launch(){
		
	}
}

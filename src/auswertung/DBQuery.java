package auswertung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.DateTimeException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.Date;
//import java.util.Locale;


/**
 * @author Malte Braun
 */
public class DBQuery {
     
    private static Connection connection;
    public static Date[] feiertage; //erstellt in Objekt
    public static String startDate; 
    public static String endDate;
    public static String startDayFormated; //erstellt in Objekt
    public static String endDayFormated; //erstellt in Objekt
    public static String path;
	
    public DBQuery(String startDate, String endDate, String path) {
	System.out.println("StartDate: "+startDate);
	System.out.println("EndDate: "+endDate);
	
	DBQuery.startDate = startDate;
	DBQuery.endDate = endDate;
	DBQuery.path = path;
	
	startDayFormated = convertDateSQL(startDate);
	endDayFormated = convertDateSQL(endDate);
	
	System.out.println("StartDate: "+startDayFormated);
	System.out.println("EndDate: "+endDayFormated);
	
	connectToDB();
	getHolidays();
    }

    protected static void connectToDB(){
	    	
        try {             	
            connection = DriverManager.getConnection("jdbc:firebirdsql://192.168.99.151/HecomZEF?encoding=ISO8859_1", "SYSDBA", "masterkey");
            System.out.println("Verbindung hergestellt");
            
        } catch (SQLException ex) {
            System.out.println("Datenbankverbindung konnte nicht hergestellt werden.");
            Thread.currentThread().getStackTrace();
        }
    }  
        
    private static String convertDateSQL(String value) {
        try {
            SimpleDateFormat formatZEF = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = (formatZEF.parse(value));
            return formatSQL.format(date);
        } catch (ParseException ex) {
            System.out.println("Fehler in convertDate.");    
        }
        return null;
    }     
        
    protected static ResultSet query(String queryString) {
        try {
            ResultSet rs = null;
            rs = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT).executeQuery(queryString);
            return rs;
        } 
        catch (SQLException | NullPointerException ex) {
        	System.out.println("Datenbank Anfrage konnte nicht ausgeführt werden");  
            Thread.currentThread().getStackTrace();
            return null;
        }
    }
            
    protected static Date[] getHolidays()
    {   
	try { 	    
	    PreparedStatement prepstate = connection.prepareStatement("SELECT TAG FROM AUSWERTUNG WHERE TAG >= ? AND TAG <= ? AND Feiertag = 1 GROUP BY TAG", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
	    prepstate.setString(1, startDayFormated);
	    prepstate.setString(2, endDayFormated);
    	
            ResultSet rs1 = prepstate.executeQuery();
            rs1.last();    
            feiertage = new Date[rs1.getRow()];
            rs1.beforeFirst();
            int i = 0;
            while (rs1.next())
            {
                feiertage[i] = rs1.getDate("Tag");
                i++;
            }
            return feiertage;
	} catch (SQLException e){
	    System.out.println("Holidays erstellen hat Fehler geworfen");
	    e.printStackTrace();
	    return null;
	}
    }
    
    public static Mitarbeiter[] createMitarbeiterDB() throws Exception
    {
    	String query = "Select VORNAME, NACHNAME, PNR from PERSONAL where AUSGETRETEN = 0 and PNR <> 16 and PNR <> 17 and PNR < 990 ORDER by PNR";
    	try
    	{
    		ResultSet rs2 = query(query);
    		rs2.last();
    		Mitarbeiter[] mitarbeiterDB = new Mitarbeiter[rs2.getRow()]; 
    		rs2.beforeFirst();
    		int i = 0;
    		while(rs2.next())              
    		{	
    			mitarbeiterDB[i]= new Mitarbeiter(rs2.getString("VORNAME"),rs2.getString("NACHNAME"),rs2.getString("PNR"));
    			i++;
    		}
    		return mitarbeiterDB;
    	} 
    	catch (SQLException ex)
    	{
    		System.out.println("Fehler beim abrufen der Mitarbeiter");
    		ex.printStackTrace();
    		return null;
    	}	
    }
    
    protected static ResultSet get_krankheit(String pnr) throws Exception{
    	PreparedStatement prepstate = connection.prepareStatement("Select TAG, BEG_STD, BEG_MIN, END_STD, END_MIN, KENNZEICHEN from STEMPELUNG where PNR = ? and KENNZEICHEN = 'K' AND TAG >= ? AND TAG <= ? ORDER by TAG ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		prepstate.setString(1, pnr);
		prepstate.setString(2, startDayFormated);
		prepstate.setString(3, endDayFormated);  		
		
		String x = prepstate.toString();
		System.out.println(x);
		
		ResultSet rs = prepstate.executeQuery();
		return rs;
    }
      
    protected static ResultSet get_urlaub(String pnr) throws Exception{
    	System.out.println("getUrlaub");
    	PreparedStatement prepstate = connection.prepareStatement("Select PNR, TAG, BEG_STD, BEG_MIN, END_STD, END_MIN, KENNZEICHEN from STEMPELUNG where PNR = ?  AND TAG >= ? AND TAG <= ? and (KENNZEICHEN = 'G' or KENNZEICHEN = 'H') ORDER by TAG ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		prepstate.setString(1, pnr);
		prepstate.setString(2, startDayFormated);
		prepstate.setString(3, endDayFormated);

		ResultSet rs = prepstate.executeQuery();
		return rs;
    }
}

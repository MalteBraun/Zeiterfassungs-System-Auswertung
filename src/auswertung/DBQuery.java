package auswertung;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;



/**
 *
 * @author Malte Braun
 */
public class DBQuery {
     
    private static Connection connection;
    public  static Date[] feiertage;
    public  static String startDayString;
    public  static String endDayString;
    public  static String startDayFormated;
    public  static String endDayFormated;
	
     	
    public static  void connectToDB(){  	    	
        try {                 	
            connection = DriverManager.getConnection("jdbc:firebirdsql://192.168.99.151/HecomZEF?encoding=ISO8859_1", "SYSDBA", "masterkey");
            System.out.println("Connection hergestellt");
        } catch (SQLException ex) {
            System.out.println("Datenbankverbindung konnte nicht hergestellt werden.");
            Thread.currentThread().getStackTrace();
        }
    }  
            
    protected static  ResultSet query(String queryString) {
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
            
    public static  Date[] getHolidays() throws Exception
    {   
		PreparedStatement prepstate = connection.prepareStatement("SELECT TAG FROM AUSWERTUNG WHERE TAG >= ? AND TAG <= ? AND Feiertag = 1 GROUP BY TAG", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);	
		prepstate.setString(1, startDayString);
		prepstate.setString(2, endDayString);
		
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
    }
    
    public static  Mitarbeiter[] createMitarbeiterDB() throws Exception
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
    
    protected static  ResultSet get_krankheit(String pnr) throws Exception{
    	//System.out.println("getKrankheit");
    	PreparedStatement prepstate = connection.prepareStatement("Select TAG, BEG_STD, BEG_MIN, END_STD, END_MIN, KENNZEICHEN from STEMPELUNG where PNR = ? and KENNZEICHEN = 'K' AND TAG >= ? AND TAG <= ? ORDER by TAG ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		prepstate.setString(1, pnr);
		prepstate.setString(2, startDayString);
		prepstate.setString(3, endDayString);  		
		
		String x = prepstate.toString();
		System.out.println(x);
		
		ResultSet rs = prepstate.executeQuery();
		return rs;
    }
      
    protected static  ResultSet get_urlaub(String pnr) throws Exception{
    	System.out.println("getUrlaub");
    	PreparedStatement prepstate = connection.prepareStatement("Select PNR, TAG, BEG_STD, BEG_MIN, END_STD, END_MIN, KENNZEICHEN from STEMPELUNG where PNR = ?  AND TAG >= ? AND TAG <= ? and (KENNZEICHEN = 'G' or KENNZEICHEN = 'H') ORDER by TAG ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		prepstate.setString(1, pnr);
		prepstate.setString(2, startDayString);
		prepstate.setString(3, endDayString);

		ResultSet rs = prepstate.executeQuery();
		return rs;
    }
}

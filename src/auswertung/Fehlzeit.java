package auswertung;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class Fehlzeit.
 *
 * @author Malte Braun
 */


public class Fehlzeit {

    	private String typ;
	private String pnr;
	private Stempelung[] stempel;
	private Intervall[] intervall;
	private String[] vonbis;
	private Core core;

    

	 public Fehlzeit(String typ, String pnr) throws Exception
 	{
 		this.typ = typ;
 		this.pnr = pnr;
 		
 		switch(typ){
 		case "Krankheit": 
 			getKrankheitAusfall();
 			break;
 		case "Urlaub":;
 			getUrlaubAusfall();
 			break;
 		default:
 			System.out.println("Fehler beim Erstellen von Abwesenheitsstempelung");
 			Thread.currentThread().getStackTrace();	
 		}
 		this.core = new Core();
 		this.vonbis =  core.getVonBis(stempel);
 		this.intervall = core.getIntervall();
 	}
 	 	
	 public void getKrankheitAusfall() throws Exception
 	{
 		ResultSet rs2 = DBQuery.get_krankheit(pnr);
		try
		{
			int gueltige_Fehlzeit = 0;
			rs2.beforeFirst();
			while(rs2.next()){
				int min = rs2.getInt("END_MIN") - rs2.getInt("BEG_MIN");
				int hours = rs2.getInt("END_STD")-rs2.getInt("BEG_STD");
				min = min/60;
				int differenz = hours + min;
				if(differenz >= 3 || differenz == 0){
					gueltige_Fehlzeit++;
				}
			}
			stempel = new Stempelung[gueltige_Fehlzeit];
			rs2.beforeFirst();
			int i = 0;
			while(rs2.next())
			{					
				int min = rs2.getInt("END_MIN") - rs2.getInt("BEG_MIN");
				int hours = rs2.getInt("END_STD")-rs2.getInt("BEG_STD");
				min = min/60;
				int differenz = hours + min;
				if(differenz >= 3 || differenz == 0){		//Es wird geprüft ob weniger als 3 Stunden, damit kurze Fehlzeiten nicht als krankheit auftauchen
					stempel[i] = new Stempelung( pnr, rs2.getInt("BEG_STD"), rs2.getInt("BEG_MIN"), rs2.getInt("END_STD"), rs2.getInt("END_MIN") , rs2.getDate("TAG") , rs2.getString("KENNZEICHEN"));
					i++;
				}
			}
			
		}
		catch(SQLException ex)
		{
	 	System.out.println("Fehler bei Abfragen der krankheits Fehlzeiten");
	 	ex.printStackTrace();
		}
 	}
 	

	 public void getUrlaubAusfall() throws Exception
 	{
 		ResultSet rs3 = DBQuery.get_urlaub(pnr);
 		try
 		{
 			rs3.last();
 			System.out.println(rs3.getRow());
 			stempel = new Stempelung[rs3.getRow()];
 			rs3.beforeFirst();
 			int i = 0;
 			while(rs3.next())
    		{
 				int min = rs3.getInt("END_MIN") - rs3.getInt("BEG_MIN");
				int hours = rs3.getInt("END_STD")-rs3.getInt("BEG_STD");
				min = min/60;
				double differenz = hours + min;
				
				if(differenz >= 3 || differenz == 0){
					stempel[i] = new Stempelung( pnr, rs3.getInt("BEG_STD"), rs3.getInt("BEG_MIN"), rs3.getInt("END_STD"), rs3.getInt("END_MIN") , rs3.getDate("TAG") , rs3.getString("KENNZEICHEN"));
					i++;
				}
    		}
    	} catch(SQLException ex)
    	{
    		System.out.println("Fehler bei Abfragen der Urlaubs-Fehlzeiten");
    		ex.printStackTrace();
    	}
 	}
 	

	 public void setType(String type){
 		this.typ = type;
 	}

	 public String getType(){
 		return typ;
 	}

	 public void print(){
 		System.out.println(typ);
 		int i = 1;
 		for (Stempelung einzelStempel : stempel) {
			einzelStempel.print(i);
			i++;
		}
 	}

	 public void printVonBis(){    
 		if( vonbis != null){
 			for(String tag : vonbis){
 				System.out.println(tag);
 			}
 		}	
 	}
 	
	 public String[] getVonBis(){
 			return vonbis;	
 	}
 	
	 public double getKrankheitFehlzeit(){
 		double sum = 0.0;
 		
 		for( Intervall inter: intervall)
 		{
 			sum += inter.getDauer();
 		}	
 		return sum;
 	}

	 public double getUrlaubFehlzeit(){
 		double sum = 0.0;
 		
 		for( Intervall inter: intervall)
 		{
 			sum += inter.getDauer();
 		}
 		return sum;
 	}

	 public int getLength(){
 		return stempel.length;
 	}


	 public Stempelung[] getStempelung(){
 		return this.stempel;
 	}

	public Core getCore() {
		return core;
	} 	
	
	public boolean getFlag()
	{
		return core.getFlag();
	}

	public Intervall[] getIntervall() {
		return intervall;
	}

	public void setIntervall(Intervall[] intervall) {
		this.intervall = intervall;
	}

}

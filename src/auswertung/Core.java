/*
 * 
 */
package auswertung;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



// TODO: Auto-generated Javadoc
/**
 * The Class Core.
 * Hier werden die meisten Berechnung vorgenommen
 */
public class Core {
	
	String sortiert[][];
	private long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private Stempelung[][] vonbis_interval = new Stempelung[100][20];
	private double[] length_vonbis_interval;
	boolean flag = false;
	
 	 /**
 	  * Nxt day tmw.
	  * @param current the current date
	  * @param nxtDate the next date
	  * @return true, if nxtDate is the next day after current 
	  */
	 private boolean nxtDayTmw(Date current, Date nxtDate){
 		
 		if(current.equals(nxtDate))
 		{
 			this.flag = true;
 		}
 		
		int difInDays = (int) ((nxtDate.getTime() - current.getTime())/ DAY_IN_MILLIS );
		 if (difInDays == 1 || difInDays == 0){
			 return true;
		 }else{
			 return false;
		 }
	 }
	 
	/**
	 * Adds the to sortiert.
	 *
	 * @param tag the tag
	 * @param interval the interval
	 * @param datum the datum
	 */
	private void addToSortiert(int tag, int interval, String datum)
	{		
		sortiert[tag][interval] = datum;
	}
		
	/**
	 * Checks if is following weekday nxt.
	 *
	 * @param now the current Date
	 * @param then the next Day in the Array
	 * @return true, if is following weekday is the next day. (Skips Saturday, Sunday and Holidays)
	 */
	private boolean isFollowingWeekdayNxt(Stempelung now, Stempelung then){
				
		Date heute = now.getDatum();			// das Datum der Stempelung
		Instant instant = Instant.ofEpochMilli(heute.getTime()); // Es wird die Epoche mit eingefügt --> für LocalDate notwendig
		
		Date nxtStempel = then.getDatum();
		Instant instant2 = Instant.ofEpochMilli(nxtStempel.getTime());
		
		LocalDate today = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();  // Localdate wird aus Instant gebildet & Zeitzone des Systems miteingefügt
		LocalDate tomorrow = today.plusDays(1);
		LocalDate endDate = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault()).toLocalDate();	
		String dayOfWeek = tomorrow.getDayOfWeek().toString();  //muss zu String werden um vergleich durchführen zu können
		
		do
		{
			if( dayOfWeek.equals("SATURDAY") || dayOfWeek.equals("SUNDAY") || isFeiertag(tomorrow))
			{
				today = tomorrow;
				tomorrow = tomorrow.plusDays(1);
				dayOfWeek = tomorrow.getDayOfWeek().toString();
			}
		}while( dayOfWeek.equals("SATURDAY") || dayOfWeek.equals("SUNDAY") || isFeiertag(tomorrow));
		
		if( tomorrow.equals(endDate))
		{
			return true;
		}else{
			return false;
		}	
	}
	 
	/**
	 * Gets the von bis.
	 *
	 * @param stempel the stempel
	 * @return the von bis
	 */
	public String[] getVonBis(Stempelung[] stempel)
	{	
		System.out.println("getvonBis_Angekommen");
		int interval = 0;
		int position = 0;
		int tag = 0;		
		sortiert = new String[265][52];	
		if(stempel.length == 0){	//Wenn keine Stemplungen vorhanden sind wird "-" zurückgegeben
			String[] leer = new String[1];
			leer[0] = "-";
			System.out.println("Keine Fehlzeit");
			return leer;
		}
		System.out.println("Stempel Length: "+stempel.length);
		System.out.println("Position: "+position);
		while( stempel.length  > position )		// Solange weitere Stempelung vorhanden sind	
		{
			System.out.println("addToSortiert Tag: "+tag+" interval: "+interval+" Stempel "+stempel);
			addToSortiert(tag,interval,stempel[position].simplifyDate());	// Füge den ausgewählten Tag in die Liste ein
			set_vonbis_Interval(tag,interval,stempel[position]);
			//System.out.println("Zweiter Teil");
			if ( stempel.length - 1 >  position)	// Gibt es einen weiteren Tag?
			{	
				if( nxtDayTmw(stempel[position].getDatum(),stempel[position+1].getDatum())) // Ist der weitere Tag der nächste?
				{
					position++;	//wähle den nächsten Tag aus
					tag++;
				}else
				{	
					if (isFollowingWeekdayNxt(stempel[position],stempel[position+1]) )//Ist der nächste Tag Samstag, Sonntag od. Feiertag
					{
						position++;	//Wähle den nächsten (in diesem Fall Sa,So od. Feiertag)
						tag++;
					}else{
						position++;	//Wähle den nächsten Tag und beginn eine neue Liste
						interval++; //Es wird ein neues Intervall angesetzt
						tag = 0; //gezählte Tage werden wieder auf 0 gesetzt
					}
				}
			}else
			{
				position++;
				interval++;
				tag = 0;
			}		
		}
		//setLength_vonbis();
		System.out.println("getvonBis_Fertig");
		return compactVonBis(sortiert);		
	}

 	/**
	  * Compact von bis.
	  *
	  * @param sortiert the sortiert
	  * @return the string[]
	  */
	 private String[] compactVonBis(String[][] sortiert){    //vonbis[x][y]
 	 	
 		String vonBis[] = new String[1000];
 		int x = 0; // max 265
 		int y = 0; //max 52
 		int z = 0;
 		
 		while(sortiert[0][y] != null) //solange min. ein Eintrag zu finden ist
 		{
 			x = 0;
 			while(sortiert[x][y] != null)	//zählt Anzahl der Einträge
 			{
 				x++;
 			}
 			if(sortiert[0][y].equals(sortiert[x-1][y]))
 			{
 				vonBis[z] = sortiert[0][y].toString();
 			}else
 			{
 				vonBis[z] = sortiert[0][y].toString()+" - "+sortiert[x-1][y].toString();
 			}
 			z++;
 			y++;
 		}
 		return(mkVonBisString(vonBis,z));		
 	}
 	
 	/**
	  * Mk von bis string.
	  *
	  * @param vonBis the von bis
	  * @param z the z
	  * @return the string[]
	  */
	 //Erstellt einen String der genau die richtige Länge hat
 	private String[] mkVonBisString(String[] vonBis, int z){
 		if (z <= 0){		
 			return null;
 		}else{		
	 		String[] fertig = new String[z];	 		
	 		for (int i = 0; i<= z-1 ; i++){
	 			fertig[i] = vonBis[i];
	 		}	
	 		return fertig;
 		}
 	}

 	/**
	  * Gets the max von bis.
	  *
	  * @param MA the ma
	  * @return the max von bis
	  */
	 public static int getMaxVonBis(Mitarbeiter MA)
 	{	
 		return Math.max(MA.getFehlzeitKrank().getIntervall().length , MA.getFehlzeitUrlaub().getIntervall().length);		
 	}	

 	/**
	  * Gets the _length_vonbis.
	  *
	  * @return the _length_vonbis
	  */
	 public double[] get_length_vonbis(){
 		return length_vonbis_interval;
 	}
 	 	
 	/**
	  * Set_vonbis_ interval.
	  *
	  * @param tag the tag
	  * @param interval the interval
	  * @param stempel the stempel
	  */
	 private void set_vonbis_Interval(int tag, int interval, Stempelung stempel){
 		vonbis_interval[tag][interval] = stempel;
 	}
 	
	/**
	 * Checks if is feiertag.
	 *
	 * @param tag the tag
	 * @return true, if is feiertag
	 */
	private boolean isFeiertag(LocalDate tag){
			for (Date tag2:DBQuery.feiertage)
			{
				Instant inst = Instant.ofEpochMilli(tag2.getTime());
				LocalDate temp = LocalDateTime.ofInstant(inst, ZoneId.systemDefault()).toLocalDate();
				
				if(temp.equals(tag)){
					return true;
				}
			}
		return false;
	}
	
	/**
	 * Gets the flag.
	 *
	 * @return the flag
	 */
	public boolean getFlag()
	{
		return this.flag;
	}
	
	/**
	 * Gets the intervall.
	 *
	 * @return the intervall
	 */
	public Intervall[] getIntervall()
	{		
	 	int interval = 0;
	 	int anzahl = 0;
	 	List<Integer> anzahlStempel = new ArrayList<Integer>();

	 	while(vonbis_interval[0][interval] != null) //Solange das erste Element des Intervalls != null ist
	 	{
	 		while(vonbis_interval[anzahl][interval] != null)
	 		{
	 			anzahl++;
	 		}
	 		anzahlStempel.add(anzahl);
		 	interval++;
		 	anzahl = 0;
	 	}
	 	
	 	Intervall[] intervall = new Intervall[interval];
	 	
	 	if(vonbis_interval[0][0] != null )
	 	{
	 		for( int i = 0; i < interval; i++)
		 	{
		 		Stempelung[] temp = new Stempelung[anzahlStempel.get( i )];
		 		for( int j = 0; j< anzahlStempel.get(i); j++)
		 		{
		 			temp[j] = vonbis_interval[j][i];
		 		}
		 		intervall[i] = new Intervall(temp);
		 	}
	 	}	 	
	 	return intervall;
	}
}


package model;

public class Intervall {

	private Stempelung anfangsStempel; //erstes Stempel in einem Intervall aus Fehlzeiten
	private Stempelung endStempel; // letztes Stempel in einem Intervall aus Fehlzeiten
	private String vonBis; // String welcher das Anfangs und Enddatum eines Fehlzeitenintervalls enthält (z.B. 01.03.2016 - 03.03.2016)
	private Stempelung[] einzelStempelung; // Ein Array aus Stempelungne die in einem Intervall aufeinander folgen
	private double dauer; // gleitkommazahl der Dauer einer Fehlzeit in einem Intervall 
	
	public Intervall(Stempelung[] einzelStempelung)
	{
		this.einzelStempelung = einzelStempelung;
		bereinige();
		dauerFestlegen();
		if(this.einzelStempelung.length != 0)
		{
			anfangsStempel = this.einzelStempelung[0];
			endStempel = this.einzelStempelung[this.einzelStempelung.length-1];
		}else{
			System.out.println("fehler");
		}
		setVonBis();	
	}
	
	private void bereinige() //entfernt Null-Stempelung und gleiche Stempelung aus einem Intervall
	{
		int nullzaehler = 0;
		for( int aussenzaehler = 0 ; aussenzaehler < einzelStempelung.length ; aussenzaehler++)
		{
			for( int innenzaehler = 0; innenzaehler < einzelStempelung.length ; innenzaehler++)
			{
				if( einzelStempelung[aussenzaehler] != null && einzelStempelung[innenzaehler] != null )
				{				
					if( (einzelStempelung[aussenzaehler].getDatum()).equals(einzelStempelung[innenzaehler].getDatum()) && !einzelStempelung[aussenzaehler].equals(einzelStempelung[innenzaehler]) )
					{
						double neueFehlzeit = einzelStempelung[aussenzaehler].getFehlzeit() + einzelStempelung[innenzaehler].getFehlzeit();
						einzelStempelung[aussenzaehler].setFehlzeit(neueFehlzeit);
						einzelStempelung[innenzaehler] = null;
						nullzaehler++;
					}
				}
			}
		}
		int i = 0;
		int j = 0;
		Stempelung[] bereinigt = new Stempelung[einzelStempelung.length - nullzaehler];
		while( i < einzelStempelung.length )
		{
			if( einzelStempelung[i] != null )
			{
				bereinigt[j] = einzelStempelung[i];
				j++;
				i++;
			}else{
				i++;
			}
		}
		setEinzelStempelung(bereinigt);
	}
	
	private void dauerFestlegen() // 
	{
		for( Stempelung einzelTag : einzelStempelung)
		{
			if( einzelTag.getFehlzeit() <= 3 ){
				dauer += 0;
			}else if( einzelTag.getFehlzeit() >= 3 && einzelTag.getFehlzeit() <= 6 ){
				dauer += 0.5;
			}else if( einzelTag.getFehlzeit() > 6 )
			{
				dauer += 1;
			}	
		}
	 }		

	public Stempelung getAnfangsStempel() {
		return anfangsStempel;
	}
	
	public void setAnfangsStempel(Stempelung anfangsStempel) {
		this.anfangsStempel = anfangsStempel;
	}
	
	public Stempelung getEndStempel() {
		return endStempel;
	}
	
	public void setEndStempel(Stempelung endStempel) {
		this.endStempel = endStempel;
	}
	
	public Stempelung[] getEinzelStempelung() {
		return einzelStempelung;
	}
	
	public void setEinzelStempelung(Stempelung[] einzelStempelung) {
		this.einzelStempelung = einzelStempelung;
	}
	
	public double getDauer() {
		return dauer;
	}
	
	public void setDauer(double dauer) {
		this.dauer = dauer;
	}
	
	public String getVonBis() {
		return vonBis;
	}
	
	public void setVonBis() {
		
		if( anfangsStempel != null)
		{
			if(anfangsStempel.getDatum().equals(endStempel.getDatum()))
			{
				this.vonBis = anfangsStempel.getDatumformatiert_lang();
			}else{
				this.vonBis = anfangsStempel.getDatumformatiert_kurz()+" - "+endStempel.getDatumformatiert_kurz();
			}
		}else
		{
			this.vonBis = "-";
		}
		
	}
	
	
	
}

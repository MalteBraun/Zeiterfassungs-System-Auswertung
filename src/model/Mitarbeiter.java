/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


/**
 *
 * @author Malte Braun
 */
public class Mitarbeiter
{    
    private String vorname; // Vorname des Mitarbeiters
    private String nachname; // Nachname des Mitarbeiters
    private String pnr; // Personalnummer des Mitarbeiters
    private Fehlzeit[] fehlzeiten; // Array auf Fehlzeit-Objekten
    private double krankheitSum; // Summe der Krankheitstage
    private double urlaubSum; //Summe der Urlaubstage
    private boolean flag = false; //Flag wird bei Fehlerhaften Stempelungauf True gesetzt
       
	public Mitarbeiter(String vorname, String nachname, String pnr) throws Exception
	{
	//System.out.println(vorname+" "+nachname);
        this.vorname = vorname;
        this.nachname = nachname;
        this.pnr = pnr;
        fehlzeiten = new Fehlzeit[2];
        fehlzeiten[0] = new Fehlzeit("Krankheit",pnr);
        fehlzeiten[1] = new Fehlzeit("Urlaub",pnr);
        this.krankheitSum = fehlzeiten[0].getKrankheitFehlzeit();
        this.urlaubSum = fehlzeiten[1].getUrlaubFehlzeit();
        this.flag = setFlag();
    }
 
	public void print()
	{
		System.out.println();
		System.out.print(vorname+" ");
		System.out.print(nachname+" ");
		System.out.println(pnr+" ");
		System.out.println("Summe Krankheitstage: "+krankheitSum+" ");
		System.out.println("Summe Urlaubstage: "+urlaubSum);
	}

	public String getName(){
		String name = this.vorname+" "+this.nachname;
		return name;
	}
	
 	protected void setVorname(String vorname)
 	{
		this.vorname = vorname;
	}
	
	protected void setNachname(String nachname)
	{
		this.nachname = nachname;
	}
	
	public String getPnr()
	{
		return pnr;
	}

	protected void setPnr(String pnr)
	{
		this.pnr = pnr;
	}

	public double getKrankheitSum()
	{
		return krankheitSum;
	}

	protected void setKrankheitSum(double krankheitSum)
	{
		this.krankheitSum = krankheitSum;
	}

	public double getUrlaubSum()
	{
		return urlaubSum;
	}

	protected void setUrlaubSum(double urlaubSum)
	{
		this.urlaubSum = urlaubSum;
	}
		
	private boolean setFlag() {
		return ( fehlzeiten[0].getFlag() || fehlzeiten[1].getFlag());			
	}
	
	public boolean getFlag(){
		return this.flag;
	}
	
	public Fehlzeit getFehlzeitKrank()
	{
		return fehlzeiten[0];
	}
	
	public Fehlzeit getFehlzeitUrlaub()
	{
		return fehlzeiten[1];
	}
}


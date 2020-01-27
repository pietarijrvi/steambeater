package com.ryhma6.maven.steambeater;

import model.Stat;
import model.StatAccessObject;

//Testing branch
public class App 
{
    public static void main( String[] args )
    {
    	// Testing local database
    	StatAccessObject statDAO = new StatAccessObject();
        String nimi = "Pekka";
		String tunnus = "123";
		Stat stat = new Stat(tunnus,nimi);
        if(statDAO.createStat(stat)) {
        	System.out.println("Tallennettu tietokantaan");
        }
    }
}

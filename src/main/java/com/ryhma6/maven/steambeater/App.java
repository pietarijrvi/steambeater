package com.ryhma6.maven.steambeater;

import model.Stat;
import model.StatAccessObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	StatAccessObject statDAO = new StatAccessObject();
        String nimi = "Pekka";
		String tunnus = "123";
		Stat stat = new Stat(tunnus,nimi);
        if(statDAO.createStat(stat)) {
        	System.out.println("Tallennettu tietokantaan !!!!");
        }
    }
}

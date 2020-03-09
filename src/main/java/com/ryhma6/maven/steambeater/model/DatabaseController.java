package com.ryhma6.maven.steambeater.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseController {
	private SessionFactory sf;
	private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	
	public DatabaseController() {
		sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	public Boolean addGame(String gameID, String userID, Boolean beaten, Boolean unbeatable, Boolean ignored) {
		try {
			Session session = sf.openSession();
			session.beginTransaction();
	
			GameListEntry gle = new GameListEntry(gameID, userID, beaten, unbeatable, ignored);
	
			session.saveOrUpdate(gle);
			session.getTransaction().commit();
			return true;
		} catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public GameListEntry getUserGame(String gameID, String userID) {
		try {
			Session session = sf.openSession();
			session.beginTransaction();
			
			GameListEntry gle = (GameListEntry) session.load(GameListEntry.class, gameID + userID);
			session.getTransaction().commit();
			
			return gle;
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
}

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
	
	public void addGame(String gameID, String userID, Boolean beaten, Boolean unbeatable, Boolean ignored) {
		
		Session session = sf.openSession();
		session.beginTransaction();

		GameListEntry gle = new GameListEntry(gameID, userID, beaten, unbeatable, ignored);

		session.saveOrUpdate(gle);
		session.getTransaction().commit();
	}
}

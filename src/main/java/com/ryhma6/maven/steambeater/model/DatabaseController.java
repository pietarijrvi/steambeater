package com.ryhma6.maven.steambeater.model;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

public class DatabaseController {
	private SessionFactory sf;
	private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	
	public DatabaseController() {
		sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	public Boolean addGame(GameData game) {
		try {
			Session session = sf.openSession();
			session.beginTransaction();
			String gameID = String.valueOf(game.getAppid());
			String userID = UserPreferences.getSteamID();
			Boolean beaten = game.isBeaten();
			Boolean unbeatable = game.isUnbeatable();
			Boolean ignored = game.isIgnored();
			//String gameID, String userID, Boolean beaten, Boolean unbeatable, Boolean ignored
	
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
			
			GameListEntry gle = (GameListEntry) session.get(GameListEntry.class, gameID + userID);
			session.getTransaction().commit();
			
			return gle;
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
	
	public List<GameListEntry> getAllUserGames(String userID) {
		try {
			Session session = sf.openSession();
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<GameListEntry> criteria = builder.createQuery(GameListEntry.class);		
			Root<GameListEntry> gameList = criteria.from(GameListEntry.class);
			Predicate predicate = builder.equal(gameList.get("userID"), userID);
			criteria.where(predicate);	
			List<GameListEntry> entries = session.createQuery(criteria).getResultList();
			
			session.getTransaction().commit();
			
			return entries;
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
		
	}
}

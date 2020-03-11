package com.ryhma6.maven.steambeater.model;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.ryhma6.maven.steambeater.model.steamAPI.GameData;

/**
 * Used for communication between the database and the program
 * @author KimW
 *
 */
public class DatabaseController {
	
	/**
	 * Used for creating sessions for database queries
	 */
	private SessionFactory sf;
	
	/**
	 * Used to build the session factory
	 */
	private StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	
	/**
	 * Builds the session factory for the class
	 */
	public DatabaseController() {
		sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	/**
	 * Adds a game to the database under the players userID
	 * @param game
	 * @param userID
	 * @return true if the game is successfully added, false if adding the game failed
	 */
	public Boolean addGame(GameData game, String userID) {

		/*
		GameListEntry g = getUserGame(Integer.toString(game.getAppid()), userID);
		if(g==null) {
			g=new GameListEntry();
			System.out.println();
		}*/
		try (Session session = sf.openSession()){
			GameListEntry g = new GameListEntry();
			g.setUserID(userID);
			g.setGameID(String.valueOf(game.getAppid()));
			g.setBeaten(game.isBeaten());
			g.setUnbeatable(game.isUnbeatable());
			g.setIgnored(game.isIgnored());
			g.setEntryID();
			session.beginTransaction();
			session.saveOrUpdate(g);
			session.getTransaction().commit();
			return true;
		} catch(Exception e) {
			//System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Fetches a specific game based on the userID and gameID 
	 * @param gameID
	 * @param userID
	 * @return a single GameListEntry object
	 */
	public GameListEntry getUserGame(String gameID, String userID) {
		try (Session session = sf.openSession()){
			session.beginTransaction();
			
			GameListEntry gle = (GameListEntry) session.get(GameListEntry.class, gameID + userID);
			session.getTransaction().commit();
			
			return gle;
		} catch(Exception e) {
			//System.out.println(e);
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/**
	 * Fetches all the games the user has in the database
	 * @param userID
	 * @return list of users games
	 */
	public List<GameListEntry> getAllUserGames(String userID) {
		try (Session session = sf.openSession()){
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

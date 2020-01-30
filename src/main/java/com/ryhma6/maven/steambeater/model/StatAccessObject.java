package com.ryhma6.maven.steambeater.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class StatAccessObject {
	
	private SessionFactory istuntotehdas;
	private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

	public StatAccessObject() {
		try {
			istuntotehdas = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			System.out.println("Istuntotehtaan luonti ei onnistunut");
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	
	public boolean createStat(Stat stat) {
		Transaction transaktio = null;
		try (Session istunto = istuntotehdas.openSession()) {
			transaktio = istunto.beginTransaction();
			stat = new Stat(stat.getTunnus(), stat.getNimi());
			istunto.saveOrUpdate(stat);
			transaktio.commit();
			return true;
		} catch (Exception e) {
			if (transaktio != null)
				transaktio.rollback();
			throw e;
		}
	}

}

package com.lankheet.iot.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.iot.webservice.config.DatabaseConfig;

import io.dropwizard.lifecycle.Managed;

public class DatabaseManager implements Managed, DaoListener {

	private static final String PERSISTENCE_UNIT = "meas-pu";
	private DatabaseConfig dbConfig;
	private EntityManagerFactory emf;
	private EntityManager em;

	public DatabaseManager(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	@Override
	public void start() throws Exception {
		Map<String, String> properties = new HashMap<String, String>();
		
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		em = emf.createEntityManager();
		
	}

	@Override
	public void stop() throws Exception {
		em.close();
		emf.close();
	}

	@Override
	public void newMeasurement(Measurement measurement) {
		em.getTransaction().begin();
		em.persist(measurement);
		em.getTransaction().commit();
	}
	
	protected EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
}
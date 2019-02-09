package com.lankheet.iot.webservice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.webservice.config.DatabaseConfig;
import com.lankheet.iot.webservice.dao.DaoListener;

import io.dropwizard.lifecycle.Managed;

public class DatabaseManager implements Managed, DaoListener {
    private static final Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);

    private static final String PERSISTENCE_UNIT = "meas-pu";
    private DatabaseConfig dbConfig;
    private EntityManagerFactory emf;
    private EntityManager em;

    public DatabaseManager(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void start() {
        Map<String, String> properties = new HashMap();
        properties.put("javax.persistence.jdbc.driver", dbConfig.getDriver());
        properties.put("javax.persistence.jdbc.url", dbConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", dbConfig.getUserName());
        properties.put("javax.persistence.jdbc.password", dbConfig.getPassword());

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        em = emf.createEntityManager();
    }

    @Override
    public void stop() {
        em.close();
        emf.close();
    }


    /**
     * Request measurements of one day for a specific sensor and measurementType.
     * 
     */
    @Override
    public List<Measurement> getMeasurements(DomoticsUser dUser, int sensorId, int measurementType, String startTime, String endTime) {
    	LocalDateTime startTimeJpql = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endTimeJpql = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        		
        return em
                .createQuery("SELECT e, e.sensor.id FROM measurements e WHERE e.sensor.id = " + sensorId
                        + "AND e.measurementType = " + measurementType
                        + "AND e.timeStamp between '" + startTimeJpql + "' AND '" + endTimeJpql + "' ORDER BY e.id ASC")
                .getResultList();
    }

    @Override
    public Measurement getLatestMeasurement(DomoticsUser dUser) {
        return (Measurement) em.createQuery("SELECT e FROM measurements e ORDER BY t.id DESC limit 1").getResultList()
                .get(0);
    }
}

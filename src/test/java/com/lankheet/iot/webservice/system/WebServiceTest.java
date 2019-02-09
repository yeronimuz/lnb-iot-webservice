package com.lankheet.iot.webservice.system;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.datatypes.entities.Location;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.datatypes.entities.MeasurementType;
import com.lankheet.iot.datatypes.entities.Sensor;
import com.lankheet.iot.datatypes.entities.SensorType;
import com.lankheet.iot.webservice.WebService;
import com.lankheet.iot.webservice.config.DatabaseConfig;

/**
 * Test for @link {@link WebService} A database is required An mqtt server is required
 */
public class WebServiceTest {
    private static final String PERSISTENCE_UNIT = "meas-pu";

    private static WebService webService = new WebService();
    
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void doSetup() throws Exception {
        // Start service
         webService.run("server", "src/test/resources/application.yml");

        DatabaseConfig dbConfig = webService.getConfiguration().getDatabaseConfig();
        // Overwrite production persistence unit settings with test values
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", dbConfig.getDriver());
        properties.put("javax.persistence.jdbc.url", dbConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", dbConfig.getUserName());
        properties.put("javax.persistence.jdbc.password", dbConfig.getPassword());
        properties.put("hibernate.hbm2ddl.auto", "create");

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        em = emf.createEntityManager();
        
        // Prepare database
        Location location = new Location("CS1F08");
        DomoticsUser domoticsUser = new DomoticsUser("jeroen", "password");
        location.setUserList(new ArrayList<DomoticsUser>() {{add(domoticsUser);}});
        Sensor sensor1 = new Sensor(SensorType.POWER_METER, "'1', 'gateway', 'B827EB5850FB', 'powermeter', '3', '1'\n" + 
                "", "power-meter", "meterkast");
        location.setSensorList(new ArrayList<Sensor>() {{add(sensor1);}});
        Measurement measurement = new Measurement(sensor1, new Date(), MeasurementType.CONSUMED_POWER_T1, 0.1);
        em.getTransaction().begin();
        em.persist(domoticsUser);
        em.persist(location);
        em.persist(sensor1);
        em.persist(measurement);
        em.getTransaction().commit();
        
    }
    
    @AfterClass
    public static void cleanUp() {
        em.close();
        emf.close();
    }

    @Test
    public void testEndToEndTest() throws Exception {
        Measurement measurement =
                new Measurement(new Sensor(SensorType.POWER_METER, "AB827EB5850FB", "power-meter", "meterkast"),
                        new Date(), MeasurementType.ACTUAL_CONSUMED_POWER, 1.1);
        Thread.sleep(2000);
        Query query = em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + measurement.getSensor().getId()
                + "AND e.type = " + measurement.getMeasurementType() + " AND e.value = " + measurement.getValue());
        List<Measurement> resultList = query.getResultList();
        int numberOfDuplicates = resultList.size();
        assertThat(numberOfDuplicates, is(1));
    }
}

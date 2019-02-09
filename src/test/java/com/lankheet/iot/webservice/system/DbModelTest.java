package com.lankheet.iot.webservice.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.lankheet.iot.datatypes.entities.DomoticsUser;
import com.lankheet.iot.datatypes.entities.Location;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.datatypes.entities.MeasurementType;
import com.lankheet.iot.datatypes.entities.Sensor;
import com.lankheet.iot.datatypes.entities.SensorType;

public class DbModelTest {
    private static final String PERSISTENCE_UNIT = "meas-pu";

    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeClass
    public static void doSetup() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/domotics");
        properties.put("javax.persistence.jdbc.user", "testuser");
        properties.put("javax.persistence.jdbc.password", "p@ssW0rd");
        properties.put("hibernate.hbm2ddl.auto", "create");
        
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void doCleanup() {
        em.close();
        emf.close();
    }
    
    @Test
    public void test() {
        // Prepare database
        Location location = new Location("CS1F08");
        DomoticsUser domoticsUser = new DomoticsUser("jeroen", "password");
        location.setUserList(new ArrayList<DomoticsUser>() {{add(domoticsUser);}});
        Sensor sensor1 = new Sensor(SensorType.POWER_METER, "B827EB5850FB", "power-meter", "meterkast");
        location.setSensorList(new ArrayList<Sensor>() {{add(sensor1);}});
        Measurement measurement = new Measurement(sensor1, new Date(), MeasurementType.CONSUMED_POWER_T1, 0.1);
        em.getTransaction().begin();
        em.persist(location);
        em.persist(sensor1);
        em.persist(domoticsUser);
        em.persist(measurement);
        em.getTransaction().commit();
    }

}
